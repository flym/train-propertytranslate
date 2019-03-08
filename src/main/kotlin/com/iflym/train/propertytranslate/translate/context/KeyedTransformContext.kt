package com.iflym.train.propertytranslate.translate.context

import com.iflym.train.propertytranslate.util.AttributeUtils

/**
 * 可以设置单个key的上下文
 * created at 2018-04-23
 *
 * @author flym
 */
interface KeyedTransformContext : TransformContext {

    var key: String?
        get() = AttributeUtils.attr(this, PROPERTY_KEY)
        set(key) = attr(PROPERTY_KEY, key)

    companion object {
        const val PROPERTY_KEY = "key"
    }
}
