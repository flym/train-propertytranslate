package com.iflym.train.propertytranslate.translate.context.doc

import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * created at 2018-09-11
 *
 * @author flym
 */
class DocContext : KeyedTransformContext {
    companion object {
        fun build(key: String): DocContext {
            val value = DocContext()
            value.key = key

            return value
        }
    }
}
