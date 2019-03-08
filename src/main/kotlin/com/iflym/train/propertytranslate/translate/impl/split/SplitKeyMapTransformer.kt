package com.iflym.train.propertytranslate.translate.impl.split

import com.google.common.collect.Lists
import com.iflym.train.propertytranslate.translate.DefaultTransformerInvocation
import com.iflym.train.propertytranslate.translate.Transformer
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.split.SplitContext
import com.iflym.train.propertytranslate.translate.enums.TransformerResultType
import com.iflym.train.propertytranslate.translate.util.TransformerInvocationUtils
import com.iflym.train.propertytranslate.util.Asserts
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple1Holder
import io.iflym.core.tuple.Tuple2
import io.iflym.core.util.CloneUtils
import io.iflym.core.util.ObjectUtils

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class SplitKeyMapTransformer : Transformer<SplitContext, MutableMap<String, Any>> {
    @Suppress("UNCHECKED_CAST")
    override fun transformChained(input: MutableMap<String, Any>, inputSchema: DataSchemaVO, context: SplitContext, invocation: DefaultTransformerInvocation, globalTransformContext: GlobalTransformContext) {
        val tuple2 = transform(input, inputSchema, context, globalTransformContext)
        val mapList = tuple2.t1 as List<Map<String, Any>>
        val schema = tuple2.t2

        //先提前绑定输出，以避免如果mapList为empty时无schema信息
        invocation.bindCurrentOutputSchema(schema)

        val resultList = Lists.newArrayListWithCapacity<Map<String, Any>>(mapList.size)
        val resultSchema = Tuple1Holder<DataSchemaVO>()

        val notPartedGlobalContext = globalTransformContext.changeDataSchemaParted(false)

        mapList.forEach { m ->
            val newInvocation = CloneUtils.clone(invocation)
            newInvocation.bindCurrentOutput(m)
            newInvocation.bindCurrentOutputSchema(schema.clone())
            if(resultSchema.t1 != null) {
                newInvocation.globalContext = notPartedGlobalContext
            }
            newInvocation.proceed()

            TransformerInvocationUtils.addResult(resultList as MutableList<Any>, newInvocation)
            if(resultSchema.t1 == null) {
                resultSchema.t1 = newInvocation.resultSchema()
            }
        }

        invocation.bindCurrentOutput(resultList)
        invocation.bindCurrentResultType(TransformerResultType.LIST)
        if(resultSchema.t1 != null) {
            invocation.bindCurrentOutputSchema(resultSchema.t1)
        }
    }

    override fun transform(input: MutableMap<String, Any>, inputSchema: DataSchemaVO, context: SplitContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val merge = context.merge
        val key = context.key!!
        var schema = inputSchema

        if(globalContext.dataSchemaParted) {
            schema = inputSchema.findSchema(key)!!
            inputSchema.removeSchema(schema)

            if(merge) {
                inputSchema.findSchemaSetOrEmpty().forEach({ schema.addSchema(it) })
                inputSchema.findPropertySetOrEmpty().forEach({ schema.addProperty(it) })
            }
        }

        val v = input[key]
        if(ObjectUtils.isEmpty(v)) {
            return Tuple2(Lists.newArrayList<Any>(), schema)
        }

        Asserts.assertTrue(v is List<*>, "相应的转换值并不是List.key:{},实际类型:{}", key, v!!.javaClass)

        input.remove(key)

        @Suppress("UNCHECKED_CAST")
        val mapList = v as List<MutableMap<String, Any>>
        if(context.merge) {
            mapList.forEach { it.putAll(input) }
        }

        return Tuple2(mapList, schema)
    }
}
