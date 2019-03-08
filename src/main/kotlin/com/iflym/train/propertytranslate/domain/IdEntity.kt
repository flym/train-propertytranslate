package com.iflym.train.propertytranslate.domain

import io.iflym.mybatis.domain.Entity
import io.iflym.mybatis.domain.Key
import io.iflym.mybatis.domain.annotation.Column
import io.iflym.mybatis.domain.annotation.Id

/**
 * created at 2018-04-18
 *
 * @author flym
 */
open class IdEntity<T : IdEntity<T>> : Entity<T> {
    /** 主键  */
    @Id
    @Column(comment = "主键")
    var id: Long = 0

    override fun key(): Key {
        return Key.of(id)
    }
}
