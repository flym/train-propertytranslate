package com.iflym.train.propertytranslate.translate.impl

import com.iflym.train.propertytranslate.translate.Transformer
import com.iflym.train.propertytranslate.translate.context.TransformContext

/**
 * 基于map类型的转换
 * created at 2018-04-23
 *
 * @author flym
 */
interface MapTransformer<in C : TransformContext> : Transformer<C, MutableMap<String, Any?>>
