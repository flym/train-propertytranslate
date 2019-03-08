package com.iflym.train.propertytranslate.translate.context.retain

import com.iflym.train.propertytranslate.translate.context.TransformContext

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiRetainKeyContext : TransformContext {
    lateinit var retainList: List<RetainKeyContext>

    fun setRetainListWithStrArray(vararg retainKeys: String) {
        val retainList = retainKeys.map { RetainKeyContext.build(it) }
        this.retainList = retainList
    }
}
