package com.iflym.train.propertytranslate.translate.impl.cases

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.cases.MultiCaseLowerUnderscore2CamelKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-09-03
 *
 * @author flym
 */
class MultiCaseLowerUnderscore2CamelKeyMapTransformer : MapTransformer<MultiCaseLowerUnderscore2CamelKeyContext> {
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiCaseLowerUnderscore2CamelKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(CaseLowerUnderscore2CamelKeyMapTransformer::class.java)

        context.caseList.forEach { t -> instance.transform(input, inputSchema, t, globalContext) }

        return Tuple2(input, inputSchema)
    }
}
