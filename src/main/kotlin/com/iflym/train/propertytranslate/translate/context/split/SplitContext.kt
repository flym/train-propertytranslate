package com.iflym.train.propertytranslate.translate.context.split

import com.iflym.train.propertytranslate.translate.context.ClearedTransformContext
import com.iflym.train.propertytranslate.translate.context.KeyedTransformContext
import com.iflym.train.propertytranslate.translate.context.MergedTransformContext

/**
 * 描述拆分子key的转换器,即将一个集合属性拆分为多个map的功能
 * created at 2018-08-31
 *
 * @author flym
 */
class SplitContext : KeyedTransformContext, ClearedTransformContext, MergedTransformContext
