package com.iflym.train.propertytranslate.translate.parser

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.domain.enums.DataPropertyDataType
import com.iflym.train.propertytranslate.translate.Transformer
import com.iflym.train.propertytranslate.translate.TransformerInstance
import com.iflym.train.propertytranslate.translate.context.TransformContext
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.util.Asserts
import com.iflym.train.propertytranslate.util.ClassUtils
import com.iflym.train.propertytranslate.util.Loggers
import com.iflym.train.propertytranslate.vo.DataPropertyVO
import io.iflym.core.util.ConvertUtils
import io.iflym.core.util.ObjectUtils
import io.iflym.core.util.StringUtils
import org.springframework.util.ReflectionUtils
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.lang.reflect.Method
import java.util.*

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class TransformerParser : DefaultHandler() {


    private val refMap = Maps.newHashMap<Class<*>, Map<String, Any>>()

    private val instanceList = Lists.newArrayList<TransformerInstance<*, *>>()

    private val startConsumerMap = Maps.newHashMap<String, (Attributes) -> Unit>()
    private val endConsumerMap = Maps.newHashMap<String, () -> Unit>()

    private var currentTransfomerClass: Class<Transformer<*, *>>? = null
    private var currentContext: TransformContext? = null

    init {
        startConsumerMap[TransformerInstance.QNAME_TRANSFORMER_INSTANCE] = { this.startTransformer(it) }
        startConsumerMap[TransformerInstance.QNAME_DATA_PROPERTY] = { this.startDataProperty(it) }
        startConsumerMap[QNAME_ROOT] = {}

        endConsumerMap[TransformerInstance.QNAME_TRANSFORMER_INSTANCE] = ({ this.endTransformer() })
        endConsumerMap[TransformerInstance.QNAME_DATA_PROPERTY] = { this.endDataProperty() }
        endConsumerMap[QNAME_ROOT] = {}
    }

    private fun startTransformer(attributes: Attributes) {
        val classSimpleName = attributes.getValue(TransformerInstance.QNAME_PROPERTY_CLASS)
        currentTransfomerClass = TransformerUtils.resolve(classSimpleName)

        val contextClass = TransformerUtils.resovleContext<TransformContext>(currentTransfomerClass!!)
        currentContext = ClassUtils.newInstance(contextClass)

        val currentContextClass = currentContext!!.javaClass

        //解析属性信息
        for(i in 0 until attributes.length) {
            val qname = attributes.getQName(i)
            val qvalue = attributes.getValue(i)

            //保留属性，跳过
            if(qname == TransformerInstance.QNAME_PROPERTY_CLASS) {
                continue
            }

            val arraySet = qname.endsWith(ATTRIBUTE_PROPERTY_ARRAY_SET_POSTFIX)
            if(arraySet) {
                transformerArraySet(qname, qvalue, currentContextClass)
                continue
            }

            val refSet = qname.endsWith(ATTRIBUTE_PROPERTY_REF_SET_POSTFIX)
            if(refSet) {
                transformerRefSet(qname, qvalue, currentContextClass)
                continue
            }

            transformerNormalSet(qname, qvalue, currentContextClass)
        }
    }

    private fun transformerNormalSet(qname: String, qvalue: String, currentContextClass: Class<out TransformContext>) {
        val method = findSetMethod(currentContextClass, qname)

        if(method != null) {
            val argTypes = method.parameterTypes
            val argType = argTypes[0]
            val arg = ConvertUtils.convert(qvalue, argType)
            ReflectionUtils.invokeMethod(method, currentContext, arg)
        }
    }

    private fun transformerRefSet(qname: String, qvalue: String, currentContextClass: Class<out TransformContext>) {
        val property = qname.substring(0, qname.length - ATTRIBUTE_PROPERTY_REF_SET_POSTFIX.length)
        val method = findSetMethod(currentContextClass, property)
        if(method != null) {
            val argType = method.parameterTypes[0]
            val refInstance = Optional.ofNullable(refMap[argType]).map<Any> { t -> t[qvalue] }.orElse(null)
            Asserts.notEmpty(refInstance, "并没有找到引用的对象。当前类:{},引用属性:{},引用值:{}", currentContextClass, property, qvalue)

            ReflectionUtils.invokeMethod(method, currentContext, refInstance)
        }
    }

    private fun transformerArraySet(qname: String, qvalue: String, currentContextClass: Class<out TransformContext>) {
        val property = qname.substring(0, qname.length - ATTRIBUTE_PROPERTY_ARRAY_SET_POSTFIX.length) + ATTRIBUTE_PROPERTY_ARRAY_SET_METHOD_POSTFIX
        val method = findSetMethod(currentContextClass, property)

        if(method != null) {
            val arg = com.iflym.train.propertytranslate.util.StringUtils.splitByComma(qvalue)

            ReflectionUtils.invokeMethod(method, currentContext, arg)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun endTransformer() {
        instanceList.add(TransformerInstance.build(currentTransfomerClass as Class<Transformer<TransformContext, *>>, currentContext as TransformContext))

        currentTransfomerClass = null
        currentContext = null
    }

    private fun startDataProperty(attributes: Attributes) {
        val name = attributes.getValue(TransformerInstance.QNAME_DATA_PROPERTY_NAME)
        val title = attributes.getValue(TransformerInstance.QNAME_DATA_PROPERTY_TITLE)
        val dataTypeStr = attributes.getValue(TransformerInstance.QNAME_DATA_PROPERTY_DATA_TYPE)

        val dataType = if(ObjectUtils.isEmpty(dataTypeStr)) DataPropertyDataType.STRING else DataPropertyDataType.valueOf(dataTypeStr)

        val dataProperty = DataPropertyVO.build(name, title, dataType)

        val orderStr = attributes.getValue("order")
        dataProperty.order = orderStr?.toInt() ?: Int.MAX_VALUE

        val key = attributes.getValue(TransformerInstance.QNAME_DATA_PROPERTY_KEY)

        @Suppress("UNCHECKED_CAST")
        val map = (refMap as MutableMap<Class<*>, MutableMap<String, Any>>)
                .computeIfAbsent(DataPropertyVO::class.java, { mutableMapOf() })
        map[key] = dataProperty
    }

    private fun endDataProperty() {
        //nothing to do
    }

    @Throws(SAXException::class)
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        super.startElement(uri, localName, qName, attributes)

        Optional.ofNullable(startConsumerMap[qName])
                .orElse { t -> log.warn("收到不认识的开始节点:{}", qName) }
                .invoke(attributes!!)
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)

        Optional.ofNullable(endConsumerMap[qName])
                .orElse { log.warn("收到不认识的结束节点:{}", qName) }
                .invoke()
    }

    fun resolvedTrasformerInstanceList(): List<TransformerInstance<*, *>> {
        return instanceList
    }

    companion object {
        private val log = Loggers.loggerFor<TransformerParser>()

        private const val QNAME_ROOT = "root"
        private const val ATTRIBUTE_PROPERTY_ARRAY_SET_POSTFIX = "-array"
        private const val ATTRIBUTE_PROPERTY_ARRAY_SET_METHOD_POSTFIX = "WithStrArray"
        private const val ATTRIBUTE_PROPERTY_REF_SET_POSTFIX = ":ref"

        private fun findSetMethod(currentClass: Class<*>, property: String): Method? {
            val methodName = "set" + Character.toUpperCase(property[0]) + property.substring(1)
            val method = findFirstMethod(currentClass, methodName) ?: return null

            val argTypes = method.parameterTypes
            if(argTypes.size != 1) {
                throw RuntimeException(StringUtils.format("转换器方法查找set方法参数类型数目不为1.类名:{},方法名:{},类型数:{}", currentClass, methodName, argTypes.size))
            }

            return method
        }

        private fun findFirstMethod(clazz: Class<*>, name: String): Method? {
            var searchType: Class<*>? = clazz
            while(searchType != null) {
                val methods = searchType.declaredMethods
                for(method in methods) {
                    if(name == method.name) {
                        return method
                    }
                }
                searchType = searchType.superclass
            }
            return null
        }
    }
}
