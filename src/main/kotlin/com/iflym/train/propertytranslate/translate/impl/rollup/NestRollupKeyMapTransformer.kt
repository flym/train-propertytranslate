package com.iflym.train.propertytranslate.translate.impl.rollup

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.rollup.NestRollupKeyContext
import com.iflym.train.propertytranslate.translate.context.rollup.RollupKeyContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.KeyedContextUtils
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-08-31
 *
 * @author flym
 */
class NestRollupKeyMapTransformer : MapTransformer<NestRollupKeyContext> {
    @Suppress("UNCHECKED_CAST")
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: NestRollupKeyContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(RollupKeyMapTransformer::class.java)

        val nestKeys = KeyedContextUtils.resolveNest(context.key!!)

        var tuple2 = Tuple2<Any, DataSchemaVO>(input, inputSchema)
        for(key in nestKeys) {
            val rollupKeyContext = RollupKeyContext.build(key, context.clear)
            tuple2 = instance.transform(tuple2.t1 as MutableMap<String, Any?>, tuple2.t2, rollupKeyContext, globalContext)
        }

        return tuple2
    }
}
