CREATE TABLE `message`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `service_name` varchar(128) DEFAULT '',
    `name`         varchar(128) NOT NULL,
    `key`          varchar(128) NOT NULL,
    `response`     blob,
    `created_time` datetime(3)  NOT NULL,
    `expired_time` datetime(3)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `domain_value_index` (`service_name`, `name`, `key`, `expired_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4;