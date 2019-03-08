package com.iflym.train.propertytranslate.translate.context.cases

import com.iflym.train.propertytranslate.translate.context.ClearedTransformContext
import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class MultiCaseLowerUnderscore2CamelKeyContext : KeyedTransformContext, ClearedTransformContext {
    lateinit var caseList: List<CaseLowerUnderscore2CamelKeyContext>
}
