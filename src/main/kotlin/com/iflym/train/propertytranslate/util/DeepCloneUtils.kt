package com.iflym.train.propertytranslate.util

/**
 * 工具类，对对象进行深度clone
 * created at 2018-09-06
 *
 * @author flym
 */
object DeepCloneUtils {
    @Suppress("UNCHECKED_CAST")
    fun <T> deepClone(t: T): T {
        if(t == null)
            return null as T

        val str = JsonUtils.toJsonStr(t)
        return JsonUtils.parse(str, (t as Any)::class.java) as T
    }
}
