package com.iflym.train.propertytranslate.translate.context.rollup

import com.iflym.train.propertytranslate.translate.context.ClearedTransformContext
import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * 处理上钻的转换上下文
 * created at 2018-08-31
 *
 * @author flym
 */
class RollupKeyContext : KeyedTransformContext, ClearedTransformContext {
    companion object {

        fun build(key: String, clear: Boolean): RollupKeyContext {
            val context = RollupKeyContext()
            context.key = key
            context.clear = clear

            return context
        }
    }
}
