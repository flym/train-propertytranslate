package com.iflym.train.propertytranslate.translate.context.copy

import com.iflym.train.propertytranslate.translate.context.ClearedTransformContext
import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class CopyKeyContext : KeyedTransformContext, ClearedTransformContext {
    lateinit var source: String
}
