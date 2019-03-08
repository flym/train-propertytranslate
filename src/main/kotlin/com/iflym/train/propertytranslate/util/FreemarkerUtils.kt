package com.iflym.train.propertytranslate.util

import freemarker.cache.StringTemplateLoader
import freemarker.template.Configuration
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler
import org.springframework.util.ReflectionUtils
import java.io.IOException
import java.io.StringWriter

/**
 * 用于更方便的生成和处理模板信息
 * created at 2018-05-22
 *
 * @author flym
 */
object FreemarkerUtils {
    private const val ENCODING = "UTF-8"

    private var strTemplateProcessor: FreeMarkerStrTemplateProcessor? = null

    private fun init() {
        if(strTemplateProcessor == null) {
            strTemplateProcessor = FreeMarkerStrTemplateProcessor()
        }
    }

    /** 使用直接的字符串模板内容生成结果  */
    fun formatWithBody(templateBody: String, modelMap: Map<String, Any>): String {
        init()

        return strTemplateProcessor!!.generate(templateBody, modelMap)
    }

    /** 生成相应的内容信息  */
    private fun doGenerate(configuration: Configuration, templateName: String, model: Any): String {
        val writer = StringWriter()
        try {
            val template = configuration.getTemplate(templateName)
            template.process(model, writer)
        } catch(e: IOException) {
            throw RuntimeException(e.message, e)
        } catch(e: TemplateException) {
            throw RuntimeException(e.message, e)
        }

        return writer.toString()
    }

    private class FreeMarkerStrTemplateProcessor {
        private var configuration: Configuration = Configuration(Configuration.VERSION_2_3_23)
        private var templateLoader: StringTemplateLoader = StringTemplateLoader()
        private var templateMap: MutableMap<String, Any>

        init {
            configuration.defaultEncoding = ENCODING
            configuration.templateExceptionHandler = TemplateExceptionHandler.IGNORE_HANDLER
            configuration.logTemplateExceptions = true
            configuration.templateLoader = templateLoader

            @Suppress("UNCHECKED_CAST")
            templateMap = ReflectionUtils.getField(FIELD_MAP!!, templateLoader) as MutableMap<String, Any>
        }

        private fun removeTemplate(key: String) {
            templateMap.remove(key)
        }

        /** 生成相应的内容信息  */
        fun generate(templateBody: String, model: Any): String {
            val randomKey = "tmp" + Math.random()
            try {
                templateLoader.putTemplate(randomKey, templateBody)
                return doGenerate(configuration, randomKey, model)
            } finally {
                removeTemplate(randomKey)
            }
        }

        companion object {

            private val FIELD_MAP = ReflectionUtils.findField(StringTemplateLoader::class.java, "templates")

            init {
                ReflectionUtils.makeAccessible(FIELD_MAP!!)
            }
        }
    }
}
