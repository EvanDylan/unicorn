CREATE TABLE `record`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `flag`         bigint(20)   NOT NULL,
    `application_name` varchar(128) DEFAULT '',
    `name`         varchar(128) NOT NULL,
    `key`          varchar(128) NOT NULL,
    `class_name`   varchar(256) DEFAULT '',
    `response`     blob,
    `created_time` datetime(3)  NOT NULL,
    `expired_time` datetime(3)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `domain_value_index` (`application_name`, `name`, `key`, `expired_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4;