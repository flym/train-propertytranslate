package com.iflym.train.propertytranslate.translate.impl.rollup

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.rollup.MultiRollupKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class MultiRollupKeyMapTransformer : MapTransformer<MultiRollupKeyContext> {
    @Suppress("UNCHECKED_CAST")
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiRollupKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(RollupKeyMapTransformer::class.java)

        var tuple2 = Tuple2<Any, DataSchemaVO>(input, inputSchema)
        for(rollup in context.rollupList) {
            tuple2 = instance.transform(tuple2.t1 as MutableMap<String, Any?>, tuple2.t2, rollup, globalContext)
        }

        return tuple2
    }
}
