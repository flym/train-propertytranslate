package com.iflym.train.propertytranslate.translate.context

import com.iflym.train.propertytranslate.util.AttributeUtils

/**
 * created at 2018-04-23
 *
 * @author flym
 */
interface MergedTransformContext : TransformContext {

    /** 返回当前转换是否需要将结果值与原入参进行整合处理 */
    var merge: Boolean
        get() = AttributeUtils.attr(this, PROPERTY_MERGE, { true })
        set(merge) = attr(PROPERTY_MERGE, merge)

    companion object {
        const val PROPERTY_MERGE = "merge"
    }
}
