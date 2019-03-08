package com.iflym.train.propertytranslate.translate.context.tostring

import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * 描述将值转换为字符串的上下文
 * created at 2018-09-11
 *
 * @author flym
 */
class ToStringContext : KeyedTransformContext {
    companion object {

        fun build(key: String): ToStringContext {
            val context = ToStringContext()
            context.key = key

            return context
        }
    }
}
