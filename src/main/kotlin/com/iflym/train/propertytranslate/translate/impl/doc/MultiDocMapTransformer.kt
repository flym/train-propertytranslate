package com.iflym.train.propertytranslate.translate.impl.doc

import com.iflym.train.propertytranslate.translate.context.GlobalTransformContext
import com.iflym.train.propertytranslate.translate.context.doc.MultiDocContext
import com.iflym.train.propertytranslate.translate.impl.MapTransformer
import com.iflym.train.propertytranslate.translate.util.TransformerUtils
import com.iflym.train.propertytranslate.vo.DataSchemaVO
import io.iflym.core.tuple.Tuple2

/**
 * created at 2018-09-11
 *
 * @author flym
 */
class MultiDocMapTransformer : MapTransformer<MultiDocContext> {
    @Suppress("UNCHECKED_CAST")
    override fun transform(input: MutableMap<String, Any?>, inputSchema: DataSchemaVO, context: MultiDocContext, globalContext: GlobalTransformContext): Tuple2<Any, DataSchemaVO> {
        val instance = TransformerUtils.build(DocMapTransformer::class.java)

        var tuple2 = Tuple2<Any, DataSchemaVO>(input, inputSchema)
        for(t in context.docList) {
            tuple2 = instance.transform(tuple2.t1 as MutableMap<String, Any?>, tuple2.t2, t, globalContext)
        }

        return tuple2
    }
}
