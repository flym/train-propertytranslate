package com.iflym.train.propertytranslate.translate.context

import com.iflym.train.propertytranslate.util.AttributeUtils

/**
 * created at 2018-04-23
 *
 * @author flym
 */
interface ClearedTransformContext : TransformContext {

    var clear: Boolean
        get() = AttributeUtils.attr(this, PROPERTY_CLEAR, { true })
        set(clear) = attr(PROPERTY_CLEAR, clear)

    companion object {
        const val PROPERTY_CLEAR = "clear"
    }
}
