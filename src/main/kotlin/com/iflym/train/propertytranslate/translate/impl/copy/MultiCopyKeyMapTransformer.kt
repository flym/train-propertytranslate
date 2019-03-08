package com.iflym.train.propertytranslate.translate.impl.copy

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.copy.MultiCopyKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiCopyKeyMapTransformer : MapTransformer<MultiCopyKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiCopyKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(CopyKeyMapTransformer::class.java)

        context.copyList.forEach { k -> instance.transform(input, inputSchema, k, globalContext) }

        return Tuple2(input, inputSchema)
    }
}
