package com.iflym.train.propertytranslate.translate

import com.google.common.base.Charsets
import com.iflym.train.propertytranslate.translate.context.TransformContext
import com.iflym.train.propertytranslate.translate.parser.TransformerParser
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.util.JsonUtils
import com.iflym.train.propertytranslate.util.SaxUtils
import io.iflym.core.util.ExceptionUtils
import java.io.ByteArrayInputStream

/**
 * created at 2018-04-23
 *
 * @author flym
 */
class TransformerInstance<C : TransformContext, T : Transformer<C, *>> {
    private lateinit var transformerClass: Class<T>
    private lateinit var configInfo: String

    @Transient
    private var configContext: C? = null

    fun createContext(): C {
        if(configContext == null) {
            val clazz = TransformerUtils.resovleContext<C>(transformerClass)
            configContext = JsonUtils.parse(configInfo, clazz)
        }

        return configContext!!
    }

    fun createTransformer(): T {
        return TransformerUtils.build(transformerClass)
    }

    companion object {

        const val QNAME_TRANSFORMER_INSTANCE = "t"
        const val QNAME_DATA_PROPERTY = "dp"

        const val QNAME_PROPERTY_CLASS = "class"

        const val QNAME_DATA_PROPERTY_KEY = "key"
        const val QNAME_DATA_PROPERTY_NAME = "name"
        const val QNAME_DATA_PROPERTY_TITLE = "title"
        const val QNAME_DATA_PROPERTY_DATA_TYPE = "dataType"


        fun build(xmlConfig: String): List<TransformerInstance<*, *>> {
            return ExceptionUtils.doFunRethrowE({
                val sax = SaxUtils.notValidFactory()

                val parser = TransformerParser()
                ByteArrayInputStream(xmlConfig.toByteArray(Charsets.UTF_8)).let { sax.newSAXParser().parse(it, parser) }

                return@doFunRethrowE parser.resolvedTrasformerInstanceList()
            })
        }

        fun <C : TransformContext, T : Transformer<C, *>> build(clazz: Class<T>, configInfo: String): TransformerInstance<*, *> {
            val vo = TransformerInstance<C, T>()
            vo.transformerClass = clazz
            vo.configInfo = configInfo

            return vo
        }

        fun <C : TransformContext, T : Transformer<C, *>> build(clazz: Class<T>, context: C): TransformerInstance<*, *> {
            val vo = TransformerInstance<C, T>()
            vo.transformerClass = clazz
            vo.configInfo = JsonUtils.toJsonStr(context)
            vo.configContext = context

            return vo
        }
    }
}
