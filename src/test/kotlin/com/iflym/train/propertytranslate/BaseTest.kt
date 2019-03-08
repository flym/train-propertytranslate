package com.iflym.train.propertytranslate

import com.alibaba.druid.pool.DruidDataSource
import io.iflym.mybatis.mybatis.ext.ConfigurationExt
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.mybatis.spring.mapper.MapperScannerConfigurer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


/**
 * created at 2019-03-04
 *
 * @author flym
 */
@TestPropertySource("classpath:application-test.properties")
@ComponentScan("com.iflym.train", "io.iflym.mybatis")
@ContextConfiguration(classes = [BaseTest::class])
@PropertySource("classpath:application.properties")
open class BaseTest : AbstractTransactionalTestNGSpringContextTests() {
    @Bean
    fun dataSource(@Value("\${test.ds.url}") url: String, @Value("\${test.ds.username}") username: String,
                   @Value("\${test.ds.password:}") password: String): DataSource {
        val ds = DruidDataSource()
        ds.url = url
        ds.username = username
        ds.password = password

        return ds
    }

    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        val tx = DataSourceTransactionManager()
        tx.dataSource = dataSource

        return tx
    }

    @Bean
    @Throws(Exception::class)
    fun sqlSessionFactory(dataSource: DataSource): SqlSessionFactory {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(dataSource)
        bean.setConfiguration(ConfigurationExt())

        return bean.`object`!!
    }

    @Bean
    @Throws(Exception::class)
    fun sqlSessionTemplate(sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(sqlSessionFactory)
    }

    companion object {
        @Bean
        fun mapperScanner(): MapperScannerConfigurer {
            val mapperScannerConfigurer = MapperScannerConfigurer()
            mapperScannerConfigurer.setSqlSessionTemplateBeanName("sqlSessionTemplate")
            mapperScannerConfigurer.setBasePackage("com.iflym.train.**.mapper")
            return mapperScannerConfigurer
        }
    }
}
