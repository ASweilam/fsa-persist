DROP SCHEMA IF EXISTS `fsa`;

CREATE SCHEMA `fsa`;

use `fsa`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `establishment_detail`;

CREATE TABLE `establishment_detail`
(
    `fhrs_id`                     int(32) NOT NULL,
    `local_authority_business_id` varchar(64)     DEFAULT NULL,
    `business_name`               varchar(64)     DEFAULT NULL,
    `business_type`               varchar(64)     DEFAULT NULL,
    `business_type_id`            int(32)         DEFAULT NULL,
    `address_line_1`              varchar(256)    DEFAULT NULL,
    `address_line_2`              varchar(256)    DEFAULT NULL,
    `address_line_3`              varchar(256)    DEFAULT NULL,
    `address_line_4`              varchar(256)    DEFAULT NULL,
    `postcode`                    varchar(64)     DEFAULT NULL,
    `rating_value`                varchar(64)     DEFAULT NULL,
    `rating_key`                  varchar(64)     DEFAULT NULL,
    `rating_date`                 varchar(64)     DEFAULT NULL,
    `scores`                      varchar(64)     DEFAULT NULL,
    `scheme_type`                 varchar(64)     DEFAULT NULL,
    `new_rating_pending`          varchar(64)     DEFAULT NULL,
    `longitude`                   decimal(20, 17) DEFAULT NULL,
    `latitude`                    decimal(20, 17) DEFAULT null,
    `local_authority_code`        int(32) NOT NULL,


    PRIMARY KEY (`fhrs_id`),

    KEY `FK_ESTDETAIL_idx` (`local_authority_code`),

    CONSTRAINT `FK_ESTDETAIL`
        FOREIGN KEY (`local_authority_code`)
            REFERENCES `local_authority` (`code`)
            ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS `local_authority`;

CREATE TABLE `local_authority`
(
    `code`    int(32) NOT NULL,
    `name`    varchar(128) DEFAULT NULL,
    `website` varchar(128) DEFAULT NULL,
    `email`   varchar(128) DEFAULT NULL,

    PRIMARY KEY (`code`),
    UNIQUE KEY `TITLE_UNIQUE` (`name`)

);


SET FOREIGN_KEY_CHECKS = 1;
