package com.iflym.train.propertytranslate.translate.impl.script

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.script.MultiScriptContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiScriptMapTransformer : MapTransformer<MultiScriptContext> {
    @Suppress("UNCHECKED_CAST")
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiScriptContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(ScriptMapTransformer::class.java)

        var tuple2 = Tuple2<Any, DataSchemaVO>(input, inputSchema)
        for(script in context.scriptList) {
            tuple2 = instance.transform(tuple2.t1 as MutableMap<String, Any?>, tuple2.t2, script, globalContext)
        }

        return tuple2
    }
}
