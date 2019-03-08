package com.iflym.train.propertytranslate.translate.impl.copy

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.copy.CopyKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class CopyKeyMapTransformer : MapTransformer<CopyKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: CopyKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val source = context.source

        input[context.key!!] = input[source]

        if(context.clear) {
            input.remove(source)
        }

        if(globalContext.dataSchemaParted) {
            inputSchema.addOrChangeSchemaOrPropertyName(source, context.key!!, context.clear)
        }

        return Tuple2(input, inputSchema)
    }
}
