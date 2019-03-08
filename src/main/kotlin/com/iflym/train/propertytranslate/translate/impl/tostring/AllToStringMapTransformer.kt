package com.iflym.train.propertytranslate.translate.impl.tostring

import com.google.common.collect.Sets
import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.tostring.AllToStringContext
import com.iflym.train.propertytranslate.translate.context.tostring.ToStringContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-09-11
 *
 * @author flym
 */
class AllToStringMapTransformer : MapTransformer<AllToStringContext> {
    @Suppress("UNCHECKED_CAST")
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: AllToStringContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(ToStringMapTransformer::class.java)

        val keys = Sets.newHashSet(input.keys)

        var tuple2 = Tuple2<Any, DataSchemaVO>(input, inputSchema)
        for(key in keys) {
            val toStringContext = ToStringContext.build(key)
            tuple2 = instance.transform(tuple2.t1 as MutableMap<String, Any?>, tuple2.t2, toStringContext, globalContext)
        }

        return tuple2
    }

}
