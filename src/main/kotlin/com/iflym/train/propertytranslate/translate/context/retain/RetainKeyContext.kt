package com.iflym.train.propertytranslate.translate.context.retain

import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * 仅保留指定的key值
 * created at 2018-08-31
 *
 * @author flym
 */
class RetainKeyContext : KeyedTransformContext {
    companion object {

        fun build(key: String): RetainKeyContext {
            val context = RetainKeyContext()
            context.key = (key)

            return context
        }
    }
}
