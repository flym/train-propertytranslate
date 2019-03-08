package com.iflym.train.propertytranslate.domain

import io.iflym.mybatis.domain.annotation.Column
import io.iflym.mybatis.domain.annotation.Table
import io.iflym.mybatis.domain.annotation.UniqueId
import org.apache.ibatis.type.JdbcType

/**
 * 一个转换的任务定义
 * created at 2018-09-06
 *
 * @author flym
 */
@Table(name = "transform_define")
class TransformDefine() : IdEntity<TransformDefine>() {
    /** 组key  */
    @UniqueId
    @Column(type = JdbcType.VARCHAR)
    var groupKey: String? = null

    /** 惟一key  */
    @UniqueId
    @Column(type = JdbcType.VARCHAR, name = "`key`")
    var key: String? = null

    /**
     * 原数据格式xml
     * 在一个组内，只需要1条记录有些值即可
     */
    @Column(type = JdbcType.VARCHAR)
    var schemaXml: String? = null

    /** 转换器xml  */
    @Column(type = JdbcType.VARCHAR)
    var transformerXml: String? = null

    /** 输出数据内容(如果有)  */
    @Column(type = JdbcType.CLOB)
    var ftlContent: String? = null

    /**
     * 组输出数据内容(如果有)
     * 在一个组内，只需要有1条记录有此值即可
     */
    @Column(type = JdbcType.CLOB)
    var groupFtlContent: String? = null

    constructor(groupKey: String? = null, key: String? = null, schemaXml: String? = null, transformerXml: String? = null, ftlContent: String? = null, groupFtlContent: String? = null) : this() {
        this.groupKey = groupKey
        this.key = key
        this.schemaXml = schemaXml
        this.transformerXml = transformerXml
        this.ftlContent = ftlContent
        this.groupFtlContent = groupFtlContent
    }
}
