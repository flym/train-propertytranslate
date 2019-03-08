package com.iflym.train.propertytranslate.service

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.domain.TransformDefine
import com.iflym.train.propertytranslate.mapper.TransformDefineMapper
import com.iflym.train.propertytranslate.translate.DefaultTransformerInvocation
import com.iflym.train.propertytranslate.translate.TransformerInstance
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.util.TransformerInvocationUtils
import com.iflym.train.propertytranslate.util.DeepCloneUtils
import com.iflym.train.propertytranslate.util.FreemarkerUtils
import com.iflym.train.propertytranslate.vo.DataPropertyVO
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2
import io.iflym.core.util.ObjectUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

/**
 * created at 2018-09-06
 *
 * @author flym
 */
@Service
@Primary
open class PropertyTransformService {
    @Autowired
    private lateinit var transformDefineMapper: TransformDefineMapper

    private fun findSchemaXml(groupKey: String, transformDefineList: List<TransformDefine>): String {
        return transformDefineList.stream()
                .map({ it.schemaXml })
                .filter({ t -> !ObjectUtils.isEmpty(t) })
                .findFirst()
                .orElseThrow({ RuntimeException("并没有找到相应的schemaXml信息.组:$groupKey") })!!
    }

    private fun findGroupFtlContent(groupKey: String, transformDefineList: List<TransformDefine>): String {
        return transformDefineList.stream()
                .map({ it.groupFtlContent })
                .filter({ t -> !ObjectUtils.isEmpty(t) })
                .findFirst()
                .orElseThrow({ RuntimeException("并没有找到相应的groupFtl信息.组:$groupKey") })!!
    }

    private fun findByGroupKeyAndKey(groupKey: String, key: String): TransformDefine {
        return transformDefineMapper.getByGroupKeyAndKey(groupKey, key)
    }

    private fun findByGroupKey(groupKey: String): List<TransformDefine> {
        return transformDefineMapper.listByGroupKey(groupKey)
    }

    private fun getValidByKey(groupKey: String, key: String): TransformDefine {
        val define = findByGroupKeyAndKey(groupKey, key)

        if(ObjectUtils.isEmpty(define.schemaXml)) {
            val list = findByGroupKey(groupKey)
            define.schemaXml = findSchemaXml(groupKey, list)
        }

        return define
    }

    private fun getGroupValidByGroupKey(groupKey: String): Tuple2<TransformDefine, List<TransformDefine>> {
        val list = findByGroupKey(groupKey)
        val schemaXml = findSchemaXml(groupKey, list)
        list.stream().filter { t -> ObjectUtils.isEmpty(t.schemaXml) }.forEach { t -> t.schemaXml = schemaXml }

        val groupDefine = TransformDefine()
        groupDefine.groupFtlContent = findGroupFtlContent(groupKey, list)

        return Tuple2(groupDefine, list)
    }

    private fun buildAndProcess(schemaXml: String, transformerXml: String, inputData: Map<String, Any?>): DefaultTransformerInvocation {
        val inputSchema = DataSchemaVO.build(schemaXml)
        val transformerList = TransformerInstance.build(transformerXml)

        val invocation = DefaultTransformerInvocation.build(transformerList, inputData, inputSchema)

        //构建默认全局上下文，默认schema参与
        val globalContext = GlobalTransformContext()
        globalContext.dataSchemaParted = true

        invocation.globalContext = globalContext
        invocation.init()

        invocation.proceed()

        return invocation
    }

    private fun findSchemaPropertyList(invocation: DefaultTransformerInvocation): List<DataPropertyVO> {
        return invocation.resultSchema()!!.fetchOrderedPropertySet()
    }

    private fun transformAsListUseInstance(define: TransformDefine, inputData: Map<String, Any?>): Tuple2<List<Map<String, Any>>, List<DataPropertyVO>> {
        val invocation = buildAndProcess(define.schemaXml!!, define.transformerXml!!, inputData)

        return Tuple2(TransformerInvocationUtils.getResultAsList(invocation), findSchemaPropertyList(invocation))
    }

    private fun transformListUseInstance(define: TransformDefine, inputDataList: List<Map<String, Any>>): Tuple2<List<Map<String, Any>>, List<DataPropertyVO>> {
        val inputSchema = DataSchemaVO.build(define.schemaXml!!)
        val transformerList = TransformerInstance.build(define.transformerXml!!)

        val schema = inputSchema.clone()

        val resultList = Lists.newArrayList<Map<String, Any>>()
        var first = true
        var propertyList = emptyList<DataPropertyVO>()
        for(inputData in inputDataList) {
            //因为后面的转换中不再需要schema，因此这里不再对schema作处理，仅当一个参数传递
            val invocation = DefaultTransformerInvocation.build(transformerList, inputData, schema)

            val globalContext = GlobalTransformContext()
            //仅在第一次使用带schema处理

            if(first) {
                globalContext.dataSchemaParted = true
            }

            invocation.globalContext = (globalContext)
            invocation.init()
            invocation.proceed()

            if(first) {
                propertyList = findSchemaPropertyList(invocation)
            }

            first = false
        }

        return Tuple2(resultList, propertyList)
    }

    fun transformAsMap(defineGroupKey: String, defineKey: String, inputData: Map<String, Any>): Tuple2<Map<String, Any>, List<DataPropertyVO>> {
        val define = getValidByKey(defineGroupKey, defineKey)

        val invocation = buildAndProcess(define.schemaXml!!, define.transformerXml!!, inputData)

        return Tuple2(TransformerInvocationUtils.getResultAsMap(invocation)!!, findSchemaPropertyList(invocation))
    }

    fun transformAsList(defineGroupKey: String, defineKey: String, inputData: Map<String, Any>): Tuple2<List<Map<String, Any>>, List<DataPropertyVO>> {
        val define = getValidByKey(defineGroupKey, defineKey)

        return transformAsListUseInstance(define, inputData)
    }

    fun transformAndPrint(defineGroupKey: String, defineKey: String, inputData: Map<String, Any?>): String {
        val define = getValidByKey(defineGroupKey, defineKey)

        val tuple2 = transformAsListUseInstance(define, inputData)
        val resultList = tuple2.t1
        val resultMap = firstOrEmpty(tuple2.t1)

        val modelMap = Maps.newHashMap<String, Any>()
        modelMap["resultMap"] = resultMap
        modelMap["resultList"] = resultList
        modelMap["resultPropertyList"] = tuple2.t2

        return FreemarkerUtils.formatWithBody(define.ftlContent!!, modelMap)
    }

    fun transformList(defineGroupKey: String, defineKey: String, inputDataList: List<Map<String, Any>>): Tuple2<List<Map<String, Any>>, List<DataPropertyVO>> {
        val define = getValidByKey(defineGroupKey, defineKey)

        return transformListUseInstance(define, inputDataList)
    }

    fun transformGroupAndPrint(defineGroupkey: String, inputData: Map<String, Any?>): String {
        val groupTuple2 = getGroupValidByGroupKey(defineGroupkey)

        val modelMap = Maps.newHashMap<String, Any>()

        groupTuple2.t2.forEach { t ->
            val input = DeepCloneUtils.deepClone(inputData)
            val tuple2 = transformAsListUseInstance(t, input)

            modelMap["resultList" + t.key] = tuple2.t1
            modelMap["resultMap" + t.key] = firstOrEmpty(tuple2.t1)
            modelMap.put("resultPropertyList" + t.key, tuple2.t2)
        }

        return FreemarkerUtils.formatWithBody(groupTuple2.t1.groupFtlContent!!, modelMap)
    }

    fun transformListAndPrint(defineGroupKey: String, defineKey: String, inputDataList: List<Map<String, Any>>): String {
        val define = getValidByKey(defineGroupKey, defineKey)
        val tuple2 = transformListUseInstance(define, inputDataList)

        val modelMap = Maps.newHashMap<String, Any>()
        modelMap["resultList"] = tuple2.t1
        modelMap["resultPropertyList"] = tuple2.t2

        return FreemarkerUtils.formatWithBody(define.ftlContent!!, modelMap)
    }

    fun transformGroupListAndPrint(defineGroupKey: String, inputDataList: List<Map<String, Any>>): String {
        val groupTuple2 = getGroupValidByGroupKey(defineGroupKey)

        val modelMap = Maps.newHashMap<String, Any>()

        groupTuple2.t2.forEach { t ->
            val inputList = DeepCloneUtils.deepClone(inputDataList)
            val tuple2 = transformListUseInstance(t, inputList)

            modelMap["resultList" + t.key] = tuple2.t1
            modelMap["resultPropertyList" + t.key] = tuple2.t2
        }

        return FreemarkerUtils.formatWithBody(groupTuple2.t1.groupFtlContent!!, modelMap)
    }

    private fun firstOrEmpty(list: List<Map<String, Any>>): Map<String, Any> {
        return if(list.isEmpty()) emptyMap() else list[0]
    }
}
