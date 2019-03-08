package com.iflym.train.propertytranslate.translate.context.remove

import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class RemoveKeyContext : KeyedTransformContext {
    companion object {
        fun build(key: String): RemoveKeyContext {
            val value = RemoveKeyContext()
            value.key = (key)

            return value
        }
    }
}
