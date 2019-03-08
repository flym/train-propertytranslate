package com.iflym.train.propertytranslate.translate.impl.remove

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.remove.MultiRemoveKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiRemoveKeyMapTransformer : MapTransformer<MultiRemoveKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiRemoveKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        context.removeList.stream()
                .map { it.key!! }
                .forEach {
                    input.remove(it)

                    if(globalContext.dataSchemaParted) {
                        inputSchema.removeSchemaOrProperty(it)
                    }
                }

        return Tuple2(input, inputSchema)
    }
}
