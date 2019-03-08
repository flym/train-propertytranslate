package com.iflym.train.propertytranslate.translate.context.remove

import com.iflym.train.propertytranslate.translate.context.TransformContext

/**
 * 描述需要清除key的上下文
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiRemoveKeyContext : TransformContext {
    lateinit var removeList: List<RemoveKeyContext>

    fun setRemoveListWithStrArray(strKeys: Array<String>) {
        val list = strKeys.map { RemoveKeyContext.build(it) }
        removeList = list
    }
}
