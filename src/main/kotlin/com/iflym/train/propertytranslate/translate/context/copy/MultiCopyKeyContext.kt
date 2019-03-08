package com.iflym.train.propertytranslate.translate.context.copy

import com.iflym.train.propertytranslate.translate.context.ClearedTransformContext
import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiCopyKeyContext : KeyedTransformContext, ClearedTransformContext {
    lateinit var copyList: List<CopyKeyContext>
}
