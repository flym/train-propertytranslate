package com.iflym.train.propertytranslate.translate.impl.remove

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.remove.RemoveKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class RemoveKeyMapTransformer : MapTransformer<RemoveKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: RemoveKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val key = context.key!!
        input.remove(key)

        if (globalContext.dataSchemaParted) {
            inputSchema.removeSchemaOrProperty(key)
        }

        return Tuple2(input, inputSchema)
    }
}
