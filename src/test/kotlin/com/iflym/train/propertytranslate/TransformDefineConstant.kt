package com.iflym.train.propertytranslate


object TransformDefineConstant {
    const val TABLE_NAME = "transform_define"

    const val TABLE_SQL = """
        CREATE TABLE `transform_define` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_key` varchar(50) DEFAULT NULL COMMENT '组key',
  `key` varchar(50) DEFAULT NULL COMMENT '惟一key',
  `schema_xml` text COMMENT '原数据格式xml',
  `transformer_xml` text COMMENT '转换器xml',
  `ftl_content` text COMMENT '输出数据内容',
  `group_ftl_content` text COMMENT '组输出数据内容',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8
        """

}