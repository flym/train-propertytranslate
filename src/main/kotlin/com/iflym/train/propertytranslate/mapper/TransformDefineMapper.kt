package com.iflym.train.propertytranslate.mapper

import com.iflym.train.propertytranslate.domain.TransformDefine
import io.iflym.mybatis.mapperx.Mapper

/**
 * created at 2018-09-06
 *
 * @author flym
 */
interface TransformDefineMapper : Mapper<TransformDefine> {
    /**
     * 根据groupkey获取相应的列表
     *
     * @param groupKey 组名
     * @return 此组下的所有转换定义
     */
    fun listByGroupKey(groupKey: String): List<TransformDefine>

    /**
     * 根据惟一key获取数据
     *
     * @param groupKey 组名
     * @param key      每个组下更具体的编码值
     * @return 惟一的的转换定义
     */
    fun getByGroupKeyAndKey(groupKey: String, key: String): TransformDefine

}
