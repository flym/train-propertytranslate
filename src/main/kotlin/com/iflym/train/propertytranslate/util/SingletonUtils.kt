package com.iflym.train.propertytranslate.util

import com.google.common.cache.CacheBuilder
import io.iflym.core.util.ExceptionUtils

/**
 * Created by flym on 2017/9/13.
 *
 * @author flym
 */
object SingletonUtils {
    private val cache = CacheBuilder.newBuilder().weakKeys().build<Class<*>, Any>()

    /** 返回指定类型的单例对象  */
    @Suppress("UNCHECKED_CAST")
    fun <T> singleton(clazz: Class<T>, initCall: (T) -> Unit = {}): T {
        return ExceptionUtils.doFunRethrowE({
            cache.get(clazz) {
                val value = ClassUtils.newInstance(clazz)
                initCall(value)
                return@get value
            } as T
        })
    }
}
