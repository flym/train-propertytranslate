package com.iflym.train.propertytranslate.translate.context.rollup

import com.iflym.train.propertytranslate.translate.context.TransformContext

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiRollupKeyContext : TransformContext {
    lateinit var rollupList: List<RollupKeyContext>
}
