package com.iflym.train.propertytranslate.util

import io.iflym.mybatis.domain.Attribute

/**
 * 工具类，方便获取属性
 * created at 2018-08-31
 *
 * @author flym
 */
object AttributeUtils {
    @Suppress("UNCHECKED_CAST")
    fun <V> attr(attribute: Attribute, key: String, supplier: () -> V = { null as V }): V {
        val map = attribute.attr()
        return map?.get(key) as V ?: supplier()
    }
}
