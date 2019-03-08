package com.iflym.train.propertytranslate.translate.impl.script

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.script.ScriptContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.util.ScriptUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class ScriptMapTransformer : MapTransformer<ScriptContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: ScriptContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!

        val value: Any = ScriptUtils.eval(context.script, input, input, true)

        input[key] = value

        if(globalContext.dataSchemaParted) {
            inputSchema.addProperty(context.requiredKeyProperty())
        }

        return Tuple2(input, inputSchema)
    }
}
