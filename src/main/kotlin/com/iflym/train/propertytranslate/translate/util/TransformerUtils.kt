package com.iflym.train.propertytranslate.translate.util

import com.google.common.collect.Maps
import com.iflym.train.propertytranslate.translate.Transformer
import com.iflym.train.propertytranslate.translate.context.TransformContext
import com.iflym.train.propertytranslate.util.ApplicationContextUtils
import com.iflym.train.propertytranslate.util.ClassScanner
import com.iflym.train.propertytranslate.util.ClassUtils
import com.iflym.train.propertytranslate.util.SingletonUtils
import org.springframework.core.ResolvableType
import java.util.*

/**
 * created at 2018-08-31
 *
 * @author flym
 */
object TransformerUtils {
    fun <T : Transformer<*, *>> build(clazz: Class<T>): T {
        return SingletonUtils.singleton(clazz, { ApplicationContextUtils.injectReadyBean(it) })
    }

    @Suppress("UNCHECKED_CAST")
    fun <C : TransformContext> resovleContext(transformerClass: Class<*>): Class<C> {
        return ResolvableType.forClass(transformerClass).`as`(Transformer::class.java)
                .getGeneric(0).resolve() as Class<C>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Transformer<*, *>> resolve(className: String): Class<T> {
        return Optional.ofNullable(TransformerUtilsHolder.classMap[className] as Class<T>)
                .orElseGet { ClassUtils.forName(className, ClassUtils.APP_CLASS_LOADER) }
    }

    private object TransformerUtilsHolder {
        val classMap = Maps.newHashMap<String, Class<*>>()!!

        init {
            val classList = ClassScanner.defaultInstance.scanSubClass(Transformer::class.java)
            classList.forEach { c ->
                val simpleName = c.simpleName
                val key = Character.toLowerCase(simpleName[0]) + simpleName.substring(1)
                classMap[key] = c
            }
        }
    }
}
