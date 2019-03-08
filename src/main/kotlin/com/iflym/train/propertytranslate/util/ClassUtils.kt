/* Created by flym at 2014/4/28 */
package com.iflym.train.propertytranslate.util

/**
 * 类工具类,用于提供简化的各项操作
 *
 * @author flym
 */
object ClassUtils {
    val APP_CLASS_LOADER = ClassUtils::class.java.classLoader!!

    /** class.forName的无异常版，增加带加载classLoader  */
    @Suppress("UNCHECKED_CAST")
    fun <T> forName(clazz: String, classLoader: ClassLoader?): Class<T> {
        try {
            return if(classLoader == null) {
                Class.forName(clazz) as Class<T>
            } else Class.forName(clazz, true, classLoader) as Class<T>

        } catch(e: ClassNotFoundException) {
            throw RuntimeException(e.message, e)
        }

    }

    /** 对指定的类进行初始化  */
    fun <T> newInstance(clazz: Class<T>): T {
        try {
            return clazz.newInstance()
        } catch(e: IllegalAccessException) {
            throw RuntimeException(e.message, e)
        } catch(e: InstantiationException) {
            throw RuntimeException(e.message, e)
        }

    }
}
