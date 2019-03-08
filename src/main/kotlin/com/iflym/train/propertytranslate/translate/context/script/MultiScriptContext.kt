package com.iflym.train.propertytranslate.translate.context.script

import com.iflym.train.propertytranslate.translate.context.TransformContext

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class MultiScriptContext : TransformContext {
    lateinit var scriptList: List<ScriptContext>
}
