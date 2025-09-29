
-- ----------------------------
-- Sequence structure for sys_config_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_config_config_id_seq";
CREATE SEQUENCE "public"."sys_config_config_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_dept_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dept_dept_id_seq";
CREATE SEQUENCE "public"."sys_dept_dept_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_dict_data_dict_code_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_data_dict_code_seq";
CREATE SEQUENCE "public"."sys_dict_data_dict_code_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_dict_type_dict_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_type_dict_id_seq";
CREATE SEQUENCE "public"."sys_dict_type_dict_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_job_log_job_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_log_job_log_id_seq";
CREATE SEQUENCE "public"."sys_job_log_job_log_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_login_info_info_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_login_info_info_id_seq";
CREATE SEQUENCE "public"."sys_login_info_info_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_menu_menu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_menu_menu_id_seq";
CREATE SEQUENCE "public"."sys_menu_menu_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_notice_notice_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_notice_notice_id_seq";
CREATE SEQUENCE "public"."sys_notice_notice_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_oper_log_oper_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_oper_log_oper_id_seq";
CREATE SEQUENCE "public"."sys_oper_log_oper_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_post_post_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_post_post_id_seq";
CREATE SEQUENCE "public"."sys_post_post_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_dept_id_seq";
CREATE SEQUENCE "public"."sys_role_dept_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_role_menu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_menu_id_seq";
CREATE SEQUENCE "public"."sys_role_menu_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_role_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_role_id_seq";
CREATE SEQUENCE "public"."sys_role_role_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_user_post_post_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_post_post_id_seq";
CREATE SEQUENCE "public"."sys_user_post_post_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_role_id_seq";
CREATE SEQUENCE "public"."sys_user_role_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Sequence structure for sys_user_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_user_id_seq";
CREATE SEQUENCE "public"."sys_user_user_id_seq"
    INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table";
CREATE TABLE "public"."gen_table"
(
    "table_id"          int8 NOT NULL,
    "table_name"        varchar(200) COLLATE "pg_catalog"."default",
    "table_comment"     varchar(500) COLLATE "pg_catalog"."default",
    "sub_table_name"    varchar(64) COLLATE "pg_catalog"."default",
    "sub_table_fk_name" varchar(64) COLLATE "pg_catalog"."default",
    "class_name"        varchar(100) COLLATE "pg_catalog"."default",
    "tpl_category"      varchar(200) COLLATE "pg_catalog"."default",
    "tpl_web_type"      varchar(30) COLLATE "pg_catalog"."default",
    "package_name"      varchar(100) COLLATE "pg_catalog"."default",
    "module_name"       varchar(30) COLLATE "pg_catalog"."default",
    "business_name"     varchar(30) COLLATE "pg_catalog"."default",
    "function_name"     varchar(50) COLLATE "pg_catalog"."default",
    "function_author"   varchar(50) COLLATE "pg_catalog"."default",
    "gen_type"          char(1) COLLATE "pg_catalog"."default",
    "gen_path"          varchar(200) COLLATE "pg_catalog"."default",
    "options"           varchar(1000) COLLATE "pg_catalog"."default",
    "create_by"         varchar(64) COLLATE "pg_catalog"."default",
    "create_time"       timestamp(6),
    "update_by"         varchar(64) COLLATE "pg_catalog"."default",
    "update_time"       timestamp(6),
    "remark"            varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."gen_table"."table_id" IS '编号';
COMMENT
ON COLUMN "public"."gen_table"."table_name" IS '表名称';
COMMENT
ON COLUMN "public"."gen_table"."table_comment" IS '表描述';
COMMENT
ON COLUMN "public"."gen_table"."sub_table_name" IS '关联子表的表名';
COMMENT
ON COLUMN "public"."gen_table"."sub_table_fk_name" IS '子表关联的外键名';
COMMENT
ON COLUMN "public"."gen_table"."class_name" IS '实体类名称';
COMMENT
ON COLUMN "public"."gen_table"."tpl_category" IS '使用的模板（crud单表操作 tree树表操作）';
COMMENT
ON COLUMN "public"."gen_table"."tpl_web_type" IS '前端模板类型（element-ui模版 element-plus模版）';
COMMENT
ON COLUMN "public"."gen_table"."package_name" IS '生成包路径';
COMMENT
ON COLUMN "public"."gen_table"."module_name" IS '生成模块名';
COMMENT
ON COLUMN "public"."gen_table"."business_name" IS '生成业务名';
COMMENT
ON COLUMN "public"."gen_table"."function_name" IS '生成功能名';
COMMENT
ON COLUMN "public"."gen_table"."function_author" IS '生成功能作者';
COMMENT
ON COLUMN "public"."gen_table"."gen_type" IS '生成代码方式（0zip压缩包 1自定义路径）';
COMMENT
ON COLUMN "public"."gen_table"."gen_path" IS '生成路径（不填默认项目路径）';
COMMENT
ON COLUMN "public"."gen_table"."options" IS '其它生成选项';
COMMENT
ON COLUMN "public"."gen_table"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."gen_table"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."gen_table"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."gen_table"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."gen_table"."remark" IS '备注';
COMMENT
ON TABLE "public"."gen_table" IS '代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table_column";
CREATE TABLE "public"."gen_table_column"
(
    "column_id"      int8 NOT NULL,
    "table_id"       int8,
    "column_name"    varchar(200) COLLATE "pg_catalog"."default",
    "column_comment" varchar(500) COLLATE "pg_catalog"."default",
    "column_type"    varchar(100) COLLATE "pg_catalog"."default",
    "java_type"      varchar(500) COLLATE "pg_catalog"."default",
    "java_field"     varchar(200) COLLATE "pg_catalog"."default",
    "is_pk"          char(1) COLLATE "pg_catalog"."default",
    "is_increment"   char(1) COLLATE "pg_catalog"."default",
    "is_required"    char(1) COLLATE "pg_catalog"."default",
    "is_insert"      char(1) COLLATE "pg_catalog"."default",
    "is_edit"        char(1) COLLATE "pg_catalog"."default",
    "is_list"        char(1) COLLATE "pg_catalog"."default",
    "is_query"       char(1) COLLATE "pg_catalog"."default",
    "query_type"     varchar(200) COLLATE "pg_catalog"."default",
    "html_type"      varchar(200) COLLATE "pg_catalog"."default",
    "dict_type"      varchar(200) COLLATE "pg_catalog"."default",
    "sort"           int4,
    "create_by"      varchar(64) COLLATE "pg_catalog"."default",
    "create_time"    timestamp(6),
    "update_by"      varchar(64) COLLATE "pg_catalog"."default",
    "update_time"    timestamp(6)
)
;
COMMENT
ON COLUMN "public"."gen_table_column"."column_id" IS '编号';
COMMENT
ON COLUMN "public"."gen_table_column"."table_id" IS '归属表编号';
COMMENT
ON COLUMN "public"."gen_table_column"."column_name" IS '列名称';
COMMENT
ON COLUMN "public"."gen_table_column"."column_comment" IS '列描述';
COMMENT
ON COLUMN "public"."gen_table_column"."column_type" IS '列类型';
COMMENT
ON COLUMN "public"."gen_table_column"."java_type" IS 'JAVA类型';
COMMENT
ON COLUMN "public"."gen_table_column"."java_field" IS 'JAVA字段名';
COMMENT
ON COLUMN "public"."gen_table_column"."is_pk" IS '是否主键（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."is_increment" IS '是否自增（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."is_required" IS '是否必填（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."is_insert" IS '是否为插入字段（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."is_edit" IS '是否编辑字段（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."is_list" IS '是否列表字段（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."is_query" IS '是否查询字段（1是）';
COMMENT
ON COLUMN "public"."gen_table_column"."query_type" IS '查询方式（等于、不等于、大于、小于、范围）';
COMMENT
ON COLUMN "public"."gen_table_column"."html_type" IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
COMMENT
ON COLUMN "public"."gen_table_column"."dict_type" IS '字典类型';
COMMENT
ON COLUMN "public"."gen_table_column"."sort" IS '排序';
COMMENT
ON COLUMN "public"."gen_table_column"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."gen_table_column"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."gen_table_column"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."gen_table_column"."update_time" IS '更新时间';
COMMENT
ON TABLE "public"."gen_table_column" IS '代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_blob_triggers";
CREATE TABLE "public"."qrtz_blob_triggers"
(
    "sched_name"    varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_name"  varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "blob_data"     bytea
)
;
COMMENT
ON COLUMN "public"."qrtz_blob_triggers"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_blob_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT
ON COLUMN "public"."qrtz_blob_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT
ON COLUMN "public"."qrtz_blob_triggers"."blob_data" IS '存放持久化Trigger对象';
COMMENT
ON TABLE "public"."qrtz_blob_triggers" IS 'Blob类型的触发器表';

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_calendars";
CREATE TABLE "public"."qrtz_calendars"
(
    "sched_name"    varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "calendar_name" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "calendar"      bytea                                       NOT NULL
)
;
COMMENT
ON COLUMN "public"."qrtz_calendars"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_calendars"."calendar_name" IS '日历名称';
COMMENT
ON COLUMN "public"."qrtz_calendars"."calendar" IS '存放持久化calendar对象';
COMMENT
ON TABLE "public"."qrtz_calendars" IS '日历信息表';

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_cron_triggers";
CREATE TABLE "public"."qrtz_cron_triggers"
(
    "sched_name"      varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_name"    varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group"   varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "cron_expression" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "time_zone_id"    varchar(80) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."qrtz_cron_triggers"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_cron_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT
ON COLUMN "public"."qrtz_cron_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT
ON COLUMN "public"."qrtz_cron_triggers"."cron_expression" IS 'cron表达式';
COMMENT
ON COLUMN "public"."qrtz_cron_triggers"."time_zone_id" IS '时区';
COMMENT
ON TABLE "public"."qrtz_cron_triggers" IS 'Cron类型的触发器表';

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_fired_triggers";
CREATE TABLE "public"."qrtz_fired_triggers"
(
    "sched_name"        varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "entry_id"          varchar(95) COLLATE "pg_catalog"."default"  NOT NULL,
    "trigger_name"      varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group"     varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "instance_name"     varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "fired_time"        int8                                        NOT NULL,
    "sched_time"        int8                                        NOT NULL,
    "priority"          int4                                        NOT NULL,
    "state"             varchar(16) COLLATE "pg_catalog"."default"  NOT NULL,
    "job_name"          varchar(200) COLLATE "pg_catalog"."default",
    "job_group"         varchar(200) COLLATE "pg_catalog"."default",
    "is_nonconcurrent"  varchar(1) COLLATE "pg_catalog"."default",
    "requests_recovery" varchar(1) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."entry_id" IS '调度器实例id';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."instance_name" IS '调度器实例名';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."fired_time" IS '触发的时间';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."sched_time" IS '定时器制定的时间';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."priority" IS '优先级';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."state" IS '状态';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."job_name" IS '任务名称';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."job_group" IS '任务组名';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."is_nonconcurrent" IS '是否并发';
COMMENT
ON COLUMN "public"."qrtz_fired_triggers"."requests_recovery" IS '是否接受恢复执行';
COMMENT
ON TABLE "public"."qrtz_fired_triggers" IS '已触发的触发器表';

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_job_details";
CREATE TABLE "public"."qrtz_job_details"
(
    "sched_name"        varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "job_name"          varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "job_group"         varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "description"       varchar(250) COLLATE "pg_catalog"."default",
    "job_class_name"    varchar(250) COLLATE "pg_catalog"."default" NOT NULL,
    "is_durable"        varchar(1) COLLATE "pg_catalog"."default"   NOT NULL,
    "is_nonconcurrent"  varchar(1) COLLATE "pg_catalog"."default"   NOT NULL,
    "is_update_data"    varchar(1) COLLATE "pg_catalog"."default"   NOT NULL,
    "requests_recovery" varchar(1) COLLATE "pg_catalog"."default"   NOT NULL,
    "job_data"          bytea
)
;
COMMENT
ON COLUMN "public"."qrtz_job_details"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_job_details"."job_name" IS '任务名称';
COMMENT
ON COLUMN "public"."qrtz_job_details"."job_group" IS '任务组名';
COMMENT
ON COLUMN "public"."qrtz_job_details"."description" IS '相关介绍';
COMMENT
ON COLUMN "public"."qrtz_job_details"."job_class_name" IS '执行任务类名称';
COMMENT
ON COLUMN "public"."qrtz_job_details"."is_durable" IS '是否持久化';
COMMENT
ON COLUMN "public"."qrtz_job_details"."is_nonconcurrent" IS '是否并发';
COMMENT
ON COLUMN "public"."qrtz_job_details"."is_update_data" IS '是否更新数据';
COMMENT
ON COLUMN "public"."qrtz_job_details"."requests_recovery" IS '是否接受恢复执行';
COMMENT
ON COLUMN "public"."qrtz_job_details"."job_data" IS '存放持久化job对象';
COMMENT
ON TABLE "public"."qrtz_job_details" IS '任务详细信息表';

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_locks";
CREATE TABLE "public"."qrtz_locks"
(
    "sched_name" varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "lock_name"  varchar(40) COLLATE "pg_catalog"."default"  NOT NULL
)
;
COMMENT
ON COLUMN "public"."qrtz_locks"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_locks"."lock_name" IS '悲观锁名称';
COMMENT
ON TABLE "public"."qrtz_locks" IS '存储的悲观锁信息表';

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_paused_trigger_grps";
CREATE TABLE "public"."qrtz_paused_trigger_grps"
(
    "sched_name"    varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT
ON COLUMN "public"."qrtz_paused_trigger_grps"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_paused_trigger_grps"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT
ON TABLE "public"."qrtz_paused_trigger_grps" IS '暂停的触发器表';

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_scheduler_state";
CREATE TABLE "public"."qrtz_scheduler_state"
(
    "sched_name"        varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "instance_name"     varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "last_checkin_time" int8                                        NOT NULL,
    "checkin_interval"  int8                                        NOT NULL
)
;
COMMENT
ON COLUMN "public"."qrtz_scheduler_state"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_scheduler_state"."instance_name" IS '实例名称';
COMMENT
ON COLUMN "public"."qrtz_scheduler_state"."last_checkin_time" IS '上次检查时间';
COMMENT
ON COLUMN "public"."qrtz_scheduler_state"."checkin_interval" IS '检查间隔时间';
COMMENT
ON TABLE "public"."qrtz_scheduler_state" IS '调度器状态表';

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_simple_triggers";
CREATE TABLE "public"."qrtz_simple_triggers"
(
    "sched_name"      varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_name"    varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group"   varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "repeat_count"    int8                                        NOT NULL,
    "repeat_interval" int8                                        NOT NULL,
    "times_triggered" int8                                        NOT NULL
)
;
COMMENT
ON COLUMN "public"."qrtz_simple_triggers"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_simple_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT
ON COLUMN "public"."qrtz_simple_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT
ON COLUMN "public"."qrtz_simple_triggers"."repeat_count" IS '重复的次数统计';
COMMENT
ON COLUMN "public"."qrtz_simple_triggers"."repeat_interval" IS '重复的间隔时间';
COMMENT
ON COLUMN "public"."qrtz_simple_triggers"."times_triggered" IS '已经触发的次数';
COMMENT
ON TABLE "public"."qrtz_simple_triggers" IS '简单触发器的信息表';

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_simprop_triggers";
CREATE TABLE "public"."qrtz_simprop_triggers"
(
    "sched_name"    varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_name"  varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "str_prop_1"    varchar(512) COLLATE "pg_catalog"."default",
    "str_prop_2"    varchar(512) COLLATE "pg_catalog"."default",
    "str_prop_3"    varchar(512) COLLATE "pg_catalog"."default",
    "int_prop_1"    int4,
    "int_prop_2"    int4,
    "long_prop_1"   int8,
    "long_prop_2"   int8,
    "dec_prop_1"    numeric(13, 4),
    "dec_prop_2"    numeric(13, 4),
    "bool_prop_1"   varchar(1) COLLATE "pg_catalog"."default",
    "bool_prop_2"   varchar(1) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."trigger_name" IS 'qrtz_triggers表trigger_name的外键';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."trigger_group" IS 'qrtz_triggers表trigger_group的外键';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."str_prop_1" IS 'String类型的trigger的第一个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."str_prop_2" IS 'String类型的trigger的第二个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."str_prop_3" IS 'String类型的trigger的第三个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."int_prop_1" IS 'int类型的trigger的第一个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."int_prop_2" IS 'int类型的trigger的第二个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."long_prop_1" IS 'long类型的trigger的第一个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."long_prop_2" IS 'long类型的trigger的第二个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."dec_prop_1" IS 'decimal类型的trigger的第一个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."dec_prop_2" IS 'decimal类型的trigger的第二个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."bool_prop_1" IS 'Boolean类型的trigger的第一个参数';
COMMENT
ON COLUMN "public"."qrtz_simprop_triggers"."bool_prop_2" IS 'Boolean类型的trigger的第二个参数';
COMMENT
ON TABLE "public"."qrtz_simprop_triggers" IS '同步机制的行锁表';

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS "public"."qrtz_triggers";
CREATE TABLE "public"."qrtz_triggers"
(
    "sched_name"     varchar(120) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_name"   varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "trigger_group"  varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "job_name"       varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "job_group"      varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
    "description"    varchar(250) COLLATE "pg_catalog"."default",
    "next_fire_time" int8,
    "prev_fire_time" int8,
    "priority"       int4,
    "trigger_state"  varchar(16) COLLATE "pg_catalog"."default"  NOT NULL,
    "trigger_type"   varchar(8) COLLATE "pg_catalog"."default"   NOT NULL,
    "start_time"     int8                                        NOT NULL,
    "end_time"       int8,
    "calendar_name"  varchar(200) COLLATE "pg_catalog"."default",
    "misfire_instr"  int2,
    "job_data"       bytea
)
;
COMMENT
ON COLUMN "public"."qrtz_triggers"."sched_name" IS '调度名称';
COMMENT
ON COLUMN "public"."qrtz_triggers"."trigger_name" IS '触发器的名字';
COMMENT
ON COLUMN "public"."qrtz_triggers"."trigger_group" IS '触发器所属组的名字';
COMMENT
ON COLUMN "public"."qrtz_triggers"."job_name" IS 'qrtz_job_details表job_name的外键';
COMMENT
ON COLUMN "public"."qrtz_triggers"."job_group" IS 'qrtz_job_details表job_group的外键';
COMMENT
ON COLUMN "public"."qrtz_triggers"."description" IS '相关介绍';
COMMENT
ON COLUMN "public"."qrtz_triggers"."next_fire_time" IS '上一次触发时间（毫秒）';
COMMENT
ON COLUMN "public"."qrtz_triggers"."prev_fire_time" IS '下一次触发时间（默认为-1表示不触发）';
COMMENT
ON COLUMN "public"."qrtz_triggers"."priority" IS '优先级';
COMMENT
ON COLUMN "public"."qrtz_triggers"."trigger_state" IS '触发器状态';
COMMENT
ON COLUMN "public"."qrtz_triggers"."trigger_type" IS '触发器的类型';
COMMENT
ON COLUMN "public"."qrtz_triggers"."start_time" IS '开始时间';
COMMENT
ON COLUMN "public"."qrtz_triggers"."end_time" IS '结束时间';
COMMENT
ON COLUMN "public"."qrtz_triggers"."calendar_name" IS '日程表名称';
COMMENT
ON COLUMN "public"."qrtz_triggers"."misfire_instr" IS '补偿执行的策略';
COMMENT
ON COLUMN "public"."qrtz_triggers"."job_data" IS '存放持久化job对象';
COMMENT
ON TABLE "public"."qrtz_triggers" IS '触发器详细信息表';

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_config";
CREATE TABLE "public"."sys_config"
(
    "config_id"    int4 NOT NULL GENERATED ALWAYS AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 10
),
    "config_name"  varchar(100) COLLATE "pg_catalog"."default",
    "config_key"   varchar(100) COLLATE "pg_catalog"."default",
    "config_value" varchar(500) COLLATE "pg_catalog"."default",
    "config_type"  char(1) COLLATE "pg_catalog"."default",
    "create_by"    varchar(64) COLLATE "pg_catalog"."default",
    "create_time"  timestamp(6),
    "update_by"    varchar(64) COLLATE "pg_catalog"."default",
    "update_time"  timestamp(6),
    "remark"       varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_config"."config_id" IS '参数主键';
COMMENT
ON COLUMN "public"."sys_config"."config_name" IS '参数名称';
COMMENT
ON COLUMN "public"."sys_config"."config_key" IS '参数键名';
COMMENT
ON COLUMN "public"."sys_config"."config_value" IS '参数键值';
COMMENT
ON COLUMN "public"."sys_config"."config_type" IS '系统内置（Y是 N否）';
COMMENT
ON COLUMN "public"."sys_config"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_config"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_config"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_config"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_config"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_config" IS '参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "public"."sys_config" OVERRIDING SYSTEM VALUE
VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-05-27 10:15:50', '', NULL,
        '初始化密码 123456');
INSERT INTO "public"."sys_config" OVERRIDING SYSTEM VALUE
VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-05-27 10:15:50', '', NULL,
        '深色主题theme-dark，浅色主题theme-light');
INSERT INTO "public"."sys_config" OVERRIDING SYSTEM VALUE
VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2024-05-27 10:15:50', '', NULL,
        '是否开启验证码功能（true开启，false关闭）');
INSERT INTO "public"."sys_config" OVERRIDING SYSTEM VALUE
VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-05-27 10:15:50',
        '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO "public"."sys_config" OVERRIDING SYSTEM VALUE
VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-05-27 10:15:50', '', NULL,
        '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO "public"."sys_config" OVERRIDING SYSTEM VALUE
VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-05-27 10:15:49', 'admin',
        '2024-06-14 14:07:09.10857', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept"
(
    "dept_id"     int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
),
    "parent_id"   int8,
    "ancestors"   varchar(50) COLLATE "pg_catalog"."default",
    "dept_name"   varchar(30) COLLATE "pg_catalog"."default",
    "order_num"   int4,
    "leader"      varchar(20) COLLATE "pg_catalog"."default",
    "phone"       varchar(11) COLLATE "pg_catalog"."default",
    "email"       varchar(50) COLLATE "pg_catalog"."default",
    "status"      char(1) COLLATE "pg_catalog"."default",
    "del_flag"    char(1) COLLATE "pg_catalog"."default",
    "create_by"   varchar(64) COLLATE "pg_catalog"."default",
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6)
)
;
COMMENT
ON COLUMN "public"."sys_dept"."dept_id" IS '部门id';
COMMENT
ON COLUMN "public"."sys_dept"."parent_id" IS '父部门id';
COMMENT
ON COLUMN "public"."sys_dept"."ancestors" IS '祖级列表';
COMMENT
ON COLUMN "public"."sys_dept"."dept_name" IS '部门名称';
COMMENT
ON COLUMN "public"."sys_dept"."order_num" IS '显示顺序';
COMMENT
ON COLUMN "public"."sys_dept"."leader" IS '负责人';
COMMENT
ON COLUMN "public"."sys_dept"."phone" IS '联系电话';
COMMENT
ON COLUMN "public"."sys_dept"."email" IS '邮箱';
COMMENT
ON COLUMN "public"."sys_dept"."status" IS '部门状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_dept"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT
ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_dept"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT
ON TABLE "public"."sys_dept" IS '部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept"
VALUES (1, 0, '0', '充电桩科技', 0, '行川', '', '', '0', '0', 'admin', '2024-05-27 10:15:32', 'admin',
        '2024-05-27 11:09:29');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_data";
CREATE TABLE "public"."sys_dict_data"
(
    "dict_code"   int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
),
    "dict_sort"   int4,
    "dict_label"  varchar(100) COLLATE "pg_catalog"."default",
    "dict_value"  varchar(100) COLLATE "pg_catalog"."default",
    "dict_type"   varchar(100) COLLATE "pg_catalog"."default",
    "css_class"   varchar(100) COLLATE "pg_catalog"."default",
    "list_class"  varchar(100) COLLATE "pg_catalog"."default",
    "is_default"  char(1) COLLATE "pg_catalog"."default",
    "status"      char(1) COLLATE "pg_catalog"."default",
    "create_by"   varchar(64) COLLATE "pg_catalog"."default",
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_dict_data"."dict_code" IS '字典编码';
COMMENT
ON COLUMN "public"."sys_dict_data"."dict_sort" IS '字典排序';
COMMENT
ON COLUMN "public"."sys_dict_data"."dict_label" IS '字典标签';
COMMENT
ON COLUMN "public"."sys_dict_data"."dict_value" IS '字典键值';
COMMENT
ON COLUMN "public"."sys_dict_data"."dict_type" IS '字典类型';
COMMENT
ON COLUMN "public"."sys_dict_data"."css_class" IS '样式属性（其他样式扩展）';
COMMENT
ON COLUMN "public"."sys_dict_data"."list_class" IS '表格回显样式';
COMMENT
ON COLUMN "public"."sys_dict_data"."is_default" IS '是否默认（Y是 N否）';
COMMENT
ON COLUMN "public"."sys_dict_data"."status" IS '状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_dict_data"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_dict_data"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_dict_data"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_dict_data"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_dict_data"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_dict_data" IS '字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "public"."sys_dict_data"
VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-05-27 10:15:47', '', NULL,
        '显示菜单');
INSERT INTO "public"."sys_dict_data"
VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:47', '', NULL,
        '隐藏菜单');
INSERT INTO "public"."sys_dict_data"
VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '停用状态');
INSERT INTO "public"."sys_dict_data"
VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '停用状态');
INSERT INTO "public"."sys_dict_data"
VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '默认分组');
INSERT INTO "public"."sys_dict_data"
VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '系统分组');
INSERT INTO "public"."sys_dict_data"
VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '系统默认是');
INSERT INTO "public"."sys_dict_data"
VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL, '系统默认否');
INSERT INTO "public"."sys_dict_data"
VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '通知');
INSERT INTO "public"."sys_dict_data"
VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '公告');
INSERT INTO "public"."sys_dict_data"
VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '关闭状态');
INSERT INTO "public"."sys_dict_data"
VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-05-27 10:15:48', '', NULL,
        '其他操作');
INSERT INTO "public"."sys_dict_data"
VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '新增操作');
INSERT INTO "public"."sys_dict_data"
VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '修改操作');
INSERT INTO "public"."sys_dict_data"
VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '删除操作');
INSERT INTO "public"."sys_dict_data"
VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '授权操作');
INSERT INTO "public"."sys_dict_data"
VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '导出操作');
INSERT INTO "public"."sys_dict_data"
VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '导入操作');
INSERT INTO "public"."sys_dict_data"
VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '强退操作');
INSERT INTO "public"."sys_dict_data"
VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '生成操作');
INSERT INTO "public"."sys_dict_data"
VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '清空操作');
INSERT INTO "public"."sys_dict_data"
VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-05-27 10:15:49', '', NULL,
        '停用状态');
INSERT INTO "public"."sys_dict_data"
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '性别男');
INSERT INTO "public"."sys_dict_data"
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '性别女');
INSERT INTO "public"."sys_dict_data"
VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '性别未知');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_type";
CREATE TABLE "public"."sys_dict_type"
(
    "dict_id"     int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
),
    "dict_name"   varchar(100) COLLATE "pg_catalog"."default",
    "dict_type"   varchar(100) COLLATE "pg_catalog"."default",
    "status"      char(1) COLLATE "pg_catalog"."default",
    "create_by"   varchar(64) COLLATE "pg_catalog"."default",
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_dict_type"."dict_id" IS '字典主键';
COMMENT
ON COLUMN "public"."sys_dict_type"."dict_name" IS '字典名称';
COMMENT
ON COLUMN "public"."sys_dict_type"."dict_type" IS '字典类型';
COMMENT
ON COLUMN "public"."sys_dict_type"."status" IS '状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_dict_type"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_dict_type"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_dict_type"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_dict_type"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_dict_type"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_dict_type" IS '字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "public"."sys_dict_type"
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-05-27 10:15:46', '', NULL, '菜单状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '系统开关列表');
INSERT INTO "public"."sys_dict_type"
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '任务状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '任务分组列表');
INSERT INTO "public"."sys_dict_type"
VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '系统是否列表');
INSERT INTO "public"."sys_dict_type"
VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '通知类型列表');
INSERT INTO "public"."sys_dict_type"
VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '通知状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '操作类型列表');
INSERT INTO "public"."sys_dict_type"
VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-05-27 10:15:47', '', NULL, '登录状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-05-27 10:15:46', 'admin', '2024-06-13 11:09:02.986131',
        '用户性别列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job"
(
    "job_id"          int8                                        NOT NULL,
    "job_name"        varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
    "job_group"       varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
    "invoke_target"   varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "cron_expression" varchar(255) COLLATE "pg_catalog"."default",
    "misfire_policy"  varchar(20) COLLATE "pg_catalog"."default",
    "concurrent"      char(1) COLLATE "pg_catalog"."default",
    "status"          char(1) COLLATE "pg_catalog"."default",
    "create_by"       varchar(64) COLLATE "pg_catalog"."default",
    "create_time"     timestamp(6),
    "update_by"       varchar(64) COLLATE "pg_catalog"."default",
    "update_time"     timestamp(6),
    "remark"          varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_job"."job_id" IS '任务ID';
COMMENT
ON COLUMN "public"."sys_job"."job_name" IS '任务名称';
COMMENT
ON COLUMN "public"."sys_job"."job_group" IS '任务组名';
COMMENT
ON COLUMN "public"."sys_job"."invoke_target" IS '调用目标字符串';
COMMENT
ON COLUMN "public"."sys_job"."cron_expression" IS 'cron执行表达式';
COMMENT
ON COLUMN "public"."sys_job"."misfire_policy" IS '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';
COMMENT
ON COLUMN "public"."sys_job"."concurrent" IS '是否并发执行（0允许 1禁止）';
COMMENT
ON COLUMN "public"."sys_job"."status" IS '状态（0正常 1暂停）';
COMMENT
ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_job"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_job"."remark" IS '备注信息';
COMMENT
ON TABLE "public"."sys_job" IS '定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job_log";
CREATE TABLE "public"."sys_job_log"
(
    "job_log_id"     int8                                        NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
    "job_name"       varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
    "job_group"      varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
    "invoke_target"  varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "job_message"    varchar(500) COLLATE "pg_catalog"."default",
    "status"         char(1) COLLATE "pg_catalog"."default",
    "exception_info" varchar(2000) COLLATE "pg_catalog"."default",
    "create_time"    timestamp(6)
)
;
COMMENT
ON COLUMN "public"."sys_job_log"."job_log_id" IS '任务日志ID';
COMMENT
ON COLUMN "public"."sys_job_log"."job_name" IS '任务名称';
COMMENT
ON COLUMN "public"."sys_job_log"."job_group" IS '任务组名';
COMMENT
ON COLUMN "public"."sys_job_log"."invoke_target" IS '调用目标字符串';
COMMENT
ON COLUMN "public"."sys_job_log"."job_message" IS '日志信息';
COMMENT
ON COLUMN "public"."sys_job_log"."status" IS '执行状态（0正常 1失败）';
COMMENT
ON COLUMN "public"."sys_job_log"."exception_info" IS '异常信息';
COMMENT
ON COLUMN "public"."sys_job_log"."create_time" IS '创建时间';
COMMENT
ON TABLE "public"."sys_job_log" IS '定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_login_info";
CREATE TABLE "public"."sys_login_info"
(
    "info_id"        int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
    "user_name"      varchar(50) COLLATE "pg_catalog"."default",
    "ipaddr"         varchar(128) COLLATE "pg_catalog"."default",
    "login_location" varchar(255) COLLATE "pg_catalog"."default",
    "browser"        varchar(50) COLLATE "pg_catalog"."default",
    "os"             varchar(50) COLLATE "pg_catalog"."default",
    "status"         char(1) COLLATE "pg_catalog"."default",
    "msg"            varchar(255) COLLATE "pg_catalog"."default",
    "login_time"     timestamp(6)
)
;
COMMENT
ON COLUMN "public"."sys_login_info"."info_id" IS '访问ID';
COMMENT
ON COLUMN "public"."sys_login_info"."user_name" IS '用户账号';
COMMENT
ON COLUMN "public"."sys_login_info"."ipaddr" IS '登录IP地址';
COMMENT
ON COLUMN "public"."sys_login_info"."login_location" IS '登录地点';
COMMENT
ON COLUMN "public"."sys_login_info"."browser" IS '浏览器类型';
COMMENT
ON COLUMN "public"."sys_login_info"."os" IS '操作系统';
COMMENT
ON COLUMN "public"."sys_login_info"."status" IS '登录状态（0成功 1失败）';
COMMENT
ON COLUMN "public"."sys_login_info"."msg" IS '提示消息';
COMMENT
ON COLUMN "public"."sys_login_info"."login_time" IS '访问时间';
COMMENT
ON TABLE "public"."sys_login_info" IS '系统访问记录';

-- ----------------------------
-- Records of sys_login_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu"
(
    "menu_id"     int8                                       NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
),
    "menu_name"   varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "parent_id"   int8,
    "order_num"   int4,
    "path"        varchar(200) COLLATE "pg_catalog"."default",
    "component"   varchar(255) COLLATE "pg_catalog"."default",
    "query"       varchar(255) COLLATE "pg_catalog"."default",
    "is_frame"    char(1) COLLATE "pg_catalog"."default",
    "is_cache"    char(1) COLLATE "pg_catalog"."default",
    "menu_type"   char(1) COLLATE "pg_catalog"."default",
    "visible"     char(1) COLLATE "pg_catalog"."default",
    "status"      char(1) COLLATE "pg_catalog"."default",
    "perms"       varchar(100) COLLATE "pg_catalog"."default",
    "icon"        varchar(100) COLLATE "pg_catalog"."default",
    "create_by"   varchar(64) COLLATE "pg_catalog"."default",
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_menu"."menu_id" IS '菜单ID';
COMMENT
ON COLUMN "public"."sys_menu"."menu_name" IS '菜单名称';
COMMENT
ON COLUMN "public"."sys_menu"."parent_id" IS '父菜单ID';
COMMENT
ON COLUMN "public"."sys_menu"."order_num" IS '显示顺序';
COMMENT
ON COLUMN "public"."sys_menu"."path" IS '路由地址';
COMMENT
ON COLUMN "public"."sys_menu"."component" IS '组件路径';
COMMENT
ON COLUMN "public"."sys_menu"."query" IS '路由参数';
COMMENT
ON COLUMN "public"."sys_menu"."is_frame" IS '是否为外链（0是 1否）';
COMMENT
ON COLUMN "public"."sys_menu"."is_cache" IS '是否缓存（0缓存 1不缓存）';
COMMENT
ON COLUMN "public"."sys_menu"."menu_type" IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT
ON COLUMN "public"."sys_menu"."visible" IS '菜单状态（0显示 1隐藏）';
COMMENT
ON COLUMN "public"."sys_menu"."status" IS '菜单状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_menu"."perms" IS '权限标识';
COMMENT
ON COLUMN "public"."sys_menu"."icon" IS '菜单图标';
COMMENT
ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_menu"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_menu"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_menu" IS '菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu"
VALUES (13, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '1', '0', 'C', '0', '0', 'monitor:online:list',
        'online', 'admin', '2024-05-27 10:15:34', 'admin', '2024-06-14 14:05:26.115487', '在线用户菜单');
INSERT INTO "public"."sys_menu"
VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '1', '0', 'M', '0', '0', '', 'monitor', 'admin',
        '2024-05-27 10:15:33', '', NULL, '系统监控目录');
INSERT INTO "public"."sys_menu"
VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '1', '0', 'M', '0', '0', '', 'tool', 'admin', '2024-05-27 10:15:33', '',
        NULL, '系统工具目录');
INSERT INTO "public"."sys_menu"
VALUES (4, '用户管理', 1, 1, 'user', 'system/user/index', '', '1', '0', 'C', '0', '0', 'system:user:list', 'user',
        'admin', '2024-05-27 10:15:34', '', NULL, '用户管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (25, '用户新增', 4, 2, '', '', '', '1', '0', 'F', '0', '0', 'system:user:add', '#', 'admin',
        '2024-05-27 10:15:35', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (26, '用户修改', 4, 3, '', '', '', '1', '0', 'F', '0', '0', 'system:user:edit', '#', 'admin',
        '2024-05-27 10:15:35', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (27, '用户删除', 4, 4, '', '', '', '1', '0', 'F', '0', '0', 'system:user:remove', '#', 'admin',
        '2024-05-27 10:15:35', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (28, '用户导出', 4, 5, '', '', '', '1', '0', 'F', '0', '0', 'system:user:export', '#', 'admin',
        '2024-05-27 10:15:35', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (29, '用户导入', 4, 6, '', '', '', '1', '0', 'F', '0', '0', 'system:user:import', '#', 'admin',
        '2024-05-27 10:15:35', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (30, '重置密码', 4, 7, '', '', '', '1', '0', 'F', '0', '0', 'system:user:resetPwd', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (31, '角色查询', 5, 1, '', '', '', '1', '0', 'F', '0', '0', 'system:role:query', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (5, '角色管理', 1, 2, 'role', 'system/role/index', '', '1', '0', 'C', '0', '0', 'system:role:list', 'peoples',
        'admin', '2024-05-27 10:15:34', '', NULL, '角色管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (32, '角色新增', 5, 2, '', '', '', '1', '0', 'F', '0', '0', 'system:role:add', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (33, '角色修改', 5, 3, '', '', '', '1', '0', 'F', '0', '0', 'system:role:edit', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (34, '角色删除', 5, 4, '', '', '', '1', '0', 'F', '0', '0', 'system:role:remove', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (35, '角色导出', 5, 5, '', '', '', '1', '0', 'F', '0', '0', 'system:role:export', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (36, '菜单查询', 6, 1, '', '', '', '1', '0', 'F', '0', '0', 'system:menu:query', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (37, '菜单新增', 6, 2, '', '', '', '1', '0', 'F', '0', '0', 'system:menu:add', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (38, '菜单修改', 6, 3, '', '', '', '1', '0', 'F', '0', '0', 'system:menu:edit', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (39, '菜单删除', 6, 4, '', '', '', '1', '0', 'F', '0', '0', 'system:menu:remove', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (40, '部门查询', 7, 1, '', '', '', '1', '0', 'F', '0', '0', 'system:dept:query', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (7, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '1', '0', 'C', '0', '0', 'system:dept:list', 'tree',
        'admin', '2024-05-27 10:15:34', '', NULL, '部门管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (41, '部门新增', 7, 2, '', '', '', '1', '0', 'F', '0', '0', 'system:dept:add', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (42, '部门修改', 7, 3, '', '', '', '1', '0', 'F', '0', '0', 'system:dept:edit', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (43, '部门删除', 7, 4, '', '', '', '1', '0', 'F', '0', '0', 'system:dept:remove', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (44, '岗位查询', 8, 1, '', '', '', '1', '0', 'F', '0', '0', 'system:post:query', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (8, '岗位管理', 1, 5, 'post', 'system/post/index', '', '1', '0', 'C', '0', '0', 'system:post:list', 'post',
        'admin', '2024-05-27 10:15:34', '', NULL, '岗位管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (45, '岗位新增', 8, 2, '', '', '', '1', '0', 'F', '0', '0', 'system:post:add', '#', 'admin',
        '2024-05-27 10:15:36', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (46, '岗位修改', 8, 3, '', '', '', '1', '0', 'F', '0', '0', 'system:post:edit', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (47, '岗位删除', 8, 4, '', '', '', '1', '0', 'F', '0', '0', 'system:post:remove', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (48, '岗位导出', 8, 5, '', '', '', '1', '0', 'F', '0', '0', 'system:post:export', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (49, '字典查询', 9, 1, '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:query', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (9, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '1', '0', 'C', '0', '0', 'system:dict:list', 'dict',
        'admin', '2024-05-27 10:15:34', '', NULL, '字典管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (50, '字典新增', 9, 2, '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:add', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (51, '字典修改', 9, 3, '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:edit', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (52, '字典删除', 9, 4, '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:remove', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (53, '字典导出', 9, 5, '#', '', '', '1', '0', 'F', '0', '0', 'system:dict:export', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (54, '参数查询', 10, 1, '#', '', '', '1', '0', 'F', '0', '0', 'system:config:query', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (10, '参数设置', 1, 7, 'config', 'system/config/index', '', '1', '0', 'C', '0', '0', 'system:config:list',
        'edit', 'admin', '2024-05-27 10:15:34', '', NULL, '参数设置菜单');
INSERT INTO "public"."sys_menu"
VALUES (55, '参数新增', 10, 2, '#', '', '', '1', '0', 'F', '0', '0', 'system:config:add', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (11, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '1', '0', 'C', '0', '0', 'system:notice:list',
        'message', 'admin', '2024-05-27 10:15:34', '', NULL, '通知公告菜单');
INSERT INTO "public"."sys_menu"
VALUES (12, '日志管理', 1, 9, 'log', '', '', '1', '0', 'M', '0', '0', '', 'log', 'admin', '2024-05-27 10:15:34', '',
        NULL, '日志管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (23, '登录日志', 12, 2, 'logininfor', 'monitor/logininfor/index', '', '1', '0', 'C', '0', '0',
        'monitor:logininfor:list', 'logininfor', 'admin', '2024-05-27 10:15:35', '', NULL, '登录日志菜单');
INSERT INTO "public"."sys_menu"
VALUES (24, '用户查询', 4, 1, '', '', '', '1', '0', 'F', '0', '0', 'system:user:query', '#', 'admin',
        '2024-05-27 10:15:35', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (14, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '1', '0', 'C', '0', '0', 'monitor:job:list', 'job',
        'admin', '2024-05-27 10:15:34', '', NULL, '定时任务菜单');
INSERT INTO "public"."sys_menu"
VALUES (15, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '1', '0', 'C', '0', '0', 'monitor:druid:list',
        'druid', 'admin', '2024-05-27 10:15:34', '', NULL, '数据监控菜单');
INSERT INTO "public"."sys_menu"
VALUES (16, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '1', '0', 'C', '0', '0', 'monitor:server:list',
        'server', 'admin', '2024-05-27 10:15:34', '', NULL, '服务监控菜单');
INSERT INTO "public"."sys_menu"
VALUES (17, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '1', '0', 'C', '0', '0', 'monitor:cache:list',
        'redis', 'admin', '2024-05-27 10:15:34', '', NULL, '缓存监控菜单');
INSERT INTO "public"."sys_menu"
VALUES (18, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '1', '0', 'C', '0', '0', 'monitor:cache:list',
        'redis-list', 'admin', '2024-05-27 10:15:35', '', NULL, '缓存列表菜单');
INSERT INTO "public"."sys_menu"
VALUES (19, '表单构建', 3, 1, 'build', 'tool/build/index', '', '1', '0', 'C', '0', '0', 'tool:build:list', 'build',
        'admin', '2024-05-27 10:15:35', '', NULL, '表单构建菜单');
INSERT INTO "public"."sys_menu"
VALUES (20, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '1', '0', 'C', '0', '0', 'tool:gen:list', 'code', 'admin',
        '2024-05-27 10:15:35', '', NULL, '代码生成菜单');
INSERT INTO "public"."sys_menu"
VALUES (21, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '1', '0', 'C', '0', '0', 'tool:swagger:list',
        'swagger', 'admin', '2024-05-27 10:15:35', '', NULL, '系统接口菜单');
INSERT INTO "public"."sys_menu"
VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '1', '0', 'M', '0', '0', '', 'system', 'admin', '2024-05-27 10:15:33',
        'admin', '2024-06-13 10:29:41.652613', '系统管理目录');
INSERT INTO "public"."sys_menu"
VALUES (6, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '1', '0', 'C', '0', '0', 'system:menu:list', 'tree-table',
        'admin', '2024-05-27 10:15:34', '', NULL, '菜单管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (70, '在线查询', 13, 1, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:online:query', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (71, '批量强退', 13, 2, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (72, '单条强退', 13, 3, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (73, '任务查询', 14, 1, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:job:query', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (74, '任务新增', 14, 2, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:job:add', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (75, '任务修改', 14, 3, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:job:edit', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (76, '任务删除', 14, 4, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:job:remove', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (22, '操作日志', 12, 1, 'operlog', 'monitor/operlog/index', '', '1', '0', 'C', '0', '0', 'monitor:operlog:list',
        'form', 'admin', '2024-05-27 10:15:35', '', NULL, '操作日志菜单');
INSERT INTO "public"."sys_menu"
VALUES (77, '状态修改', 14, 5, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (78, '任务导出', 14, 6, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:job:export', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (79, '生成查询', 20, 1, '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:query', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (80, '生成修改', 20, 2, '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:edit', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (81, '生成删除', 20, 3, '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:remove', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (82, '导入代码', 20, 4, '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:import', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (83, '预览代码', 20, 5, '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:preview', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (56, '参数修改', 10, 3, '#', '', '', '1', '0', 'F', '0', '0', 'system:config:edit', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (57, '参数删除', 10, 4, '#', '', '', '1', '0', 'F', '0', '0', 'system:config:remove', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (58, '参数导出', 10, 5, '#', '', '', '1', '0', 'F', '0', '0', 'system:config:export', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (59, '公告查询', 11, 1, '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:query', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (60, '公告新增', 11, 2, '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:add', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (61, '公告修改', 11, 3, '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:edit', '#', 'admin',
        '2024-05-27 10:15:37', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (62, '公告删除', 11, 4, '#', '', '', '1', '0', 'F', '0', '0', 'system:notice:remove', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (63, '操作查询', 22, 1, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:operlog:query', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (64, '操作删除', 22, 2, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (65, '日志导出', 22, 3, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:operlog:export', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (66, '登录查询', 23, 1, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (67, '登录删除', 23, 2, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (68, '日志导出', 23, 3, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (69, '账户解锁', 23, 4, '#', '', '', '1', '0', 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin',
        '2024-05-27 10:15:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (84, '生成代码', 20, 6, '#', '', '', '1', '0', 'F', '0', '0', 'tool:gen:code', '#', 'admin',
        '2024-05-27 10:15:39', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice";
CREATE TABLE "public"."sys_notice"
(
    "notice_id"      int4                                       NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
    "notice_title"   varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "notice_type"    char(1) COLLATE "pg_catalog"."default"     NOT NULL,
    "notice_content" text COLLATE "pg_catalog"."default",
    "status"         char(1) COLLATE "pg_catalog"."default",
    "create_by"      varchar(64) COLLATE "pg_catalog"."default",
    "create_time"    timestamp(6),
    "update_by"      varchar(64) COLLATE "pg_catalog"."default",
    "update_time"    timestamp(6),
    "remark"         varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_notice"."notice_id" IS '公告ID';
COMMENT
ON COLUMN "public"."sys_notice"."notice_title" IS '公告标题';
COMMENT
ON COLUMN "public"."sys_notice"."notice_type" IS '公告类型（1通知 2公告）';
COMMENT
ON COLUMN "public"."sys_notice"."notice_content" IS '公告内容';
COMMENT
ON COLUMN "public"."sys_notice"."status" IS '公告状态（0正常 1关闭）';
COMMENT
ON COLUMN "public"."sys_notice"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_notice"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_notice"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_notice"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_notice"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_notice" IS '通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_oper_log";
CREATE TABLE "public"."sys_oper_log"
(
    "oper_id"        int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
    "title"          varchar(50) COLLATE "pg_catalog"."default",
    "business_type"  int4,
    "method"         varchar(100) COLLATE "pg_catalog"."default",
    "request_method" varchar(10) COLLATE "pg_catalog"."default",
    "operator_type"  int4,
    "oper_name"      varchar(50) COLLATE "pg_catalog"."default",
    "dept_name"      varchar(50) COLLATE "pg_catalog"."default",
    "oper_url"       varchar(255) COLLATE "pg_catalog"."default",
    "oper_ip"        varchar(128) COLLATE "pg_catalog"."default",
    "oper_location"  varchar(255) COLLATE "pg_catalog"."default",
    "oper_param"     varchar(2000) COLLATE "pg_catalog"."default",
    "json_result"    varchar(2000) COLLATE "pg_catalog"."default",
    "status"         int4,
    "error_msg"      varchar(2000) COLLATE "pg_catalog"."default",
    "oper_time"      timestamp(6),
    "cost_time"      int8
)
;
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_id" IS '日志主键';
COMMENT
ON COLUMN "public"."sys_oper_log"."title" IS '模块标题';
COMMENT
ON COLUMN "public"."sys_oper_log"."business_type" IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT
ON COLUMN "public"."sys_oper_log"."method" IS '方法名称';
COMMENT
ON COLUMN "public"."sys_oper_log"."request_method" IS '请求方式';
COMMENT
ON COLUMN "public"."sys_oper_log"."operator_type" IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_name" IS '操作人员';
COMMENT
ON COLUMN "public"."sys_oper_log"."dept_name" IS '部门名称';
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_url" IS '请求URL';
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_ip" IS '主机地址';
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_location" IS '操作地点';
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_param" IS '请求参数';
COMMENT
ON COLUMN "public"."sys_oper_log"."json_result" IS '返回参数';
COMMENT
ON COLUMN "public"."sys_oper_log"."status" IS '操作状态（0正常 1异常）';
COMMENT
ON COLUMN "public"."sys_oper_log"."error_msg" IS '错误消息';
COMMENT
ON COLUMN "public"."sys_oper_log"."oper_time" IS '操作时间';
COMMENT
ON COLUMN "public"."sys_oper_log"."cost_time" IS '消耗时间';
COMMENT
ON TABLE "public"."sys_oper_log" IS '操作日志记录';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post"
(
    "post_id"     int8                                       NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
    "post_code"   varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
    "post_name"   varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "post_sort"   int4                                       NOT NULL,
    "status"      char(1) COLLATE "pg_catalog"."default"     NOT NULL,
    "create_by"   varchar(64) COLLATE "pg_catalog"."default",
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default",
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_post"."post_id" IS '岗位ID';
COMMENT
ON COLUMN "public"."sys_post"."post_code" IS '岗位编码';
COMMENT
ON COLUMN "public"."sys_post"."post_name" IS '岗位名称';
COMMENT
ON COLUMN "public"."sys_post"."post_sort" IS '显示顺序';
COMMENT
ON COLUMN "public"."sys_post"."status" IS '状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_post"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_post"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_post"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_post"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_post"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_post" IS '岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role"
(
    "role_id"             int8                                        NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
),
    "role_name"           varchar(30) COLLATE "pg_catalog"."default"  NOT NULL,
    "role_key"            varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "role_sort"           int4                                        NOT NULL,
    "data_scope"          char(1) COLLATE "pg_catalog"."default",
    "menu_check_strictly" bool,
    "dept_check_strictly" bool,
    "status"              char(1) COLLATE "pg_catalog"."default"      NOT NULL,
    "del_flag"            char(1) COLLATE "pg_catalog"."default" DEFAULT 0,
    "create_by"           varchar(64) COLLATE "pg_catalog"."default",
    "create_time"         timestamp(6),
    "update_by"           varchar(64) COLLATE "pg_catalog"."default",
    "update_time"         timestamp(6),
    "remark"              varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_role"."role_id" IS '角色ID';
COMMENT
ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT
ON COLUMN "public"."sys_role"."role_key" IS '角色权限字符串';
COMMENT
ON COLUMN "public"."sys_role"."role_sort" IS '显示顺序';
COMMENT
ON COLUMN "public"."sys_role"."data_scope" IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
COMMENT
ON COLUMN "public"."sys_role"."menu_check_strictly" IS '菜单树选择项是否关联显示';
COMMENT
ON COLUMN "public"."sys_role"."dept_check_strictly" IS '部门树选择项是否关联显示';
COMMENT
ON COLUMN "public"."sys_role"."status" IS '角色状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_role"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT
ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_role"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_role" IS '角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role"
VALUES (1, '超级管理员', 'super-admin', 1, '1', 't', 't', '0', '0', 'admin', '2024-05-27 10:15:33', '', NULL,
        '超级管理员');
INSERT INTO "public"."sys_role"
VALUES (2, '普通管理员', 'admin', 2, '2', 't', 't', '0', '0', 'admin', '2024-05-27 10:15:33', 'admin',
        '2024-06-13 11:07:59.016822', '普通管理员角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept";
CREATE TABLE "public"."sys_role_dept"
(
    "role_id" int8 NOT NULL,
    "dept_id" int8 NOT NULL,
    "id"      int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
)
)
;
COMMENT
ON COLUMN "public"."sys_role_dept"."role_id" IS '角色ID';
COMMENT
ON COLUMN "public"."sys_role_dept"."dept_id" IS '部门ID';
COMMENT
ON COLUMN "public"."sys_role_dept"."id" IS '主键';
COMMENT
ON TABLE "public"."sys_role_dept" IS '角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO "public"."sys_role_dept"
VALUES (2, 1, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu"
(
    "role_id" int8 NOT NULL,
    "menu_id" int8 NOT NULL,
    "id"      int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
)
)
;
COMMENT
ON COLUMN "public"."sys_role_menu"."role_id" IS '角色ID';
COMMENT
ON COLUMN "public"."sys_role_menu"."menu_id" IS '菜单ID';
COMMENT
ON COLUMN "public"."sys_role_menu"."id" IS '主键';
COMMENT
ON TABLE "public"."sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "public"."sys_role_menu"
VALUES (2, 1, 1);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 4, 2);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 24, 3);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 25, 4);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 26, 5);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 27, 6);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 28, 7);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 29, 8);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 30, 9);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 5, 10);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 31, 11);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 32, 12);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 33, 13);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 34, 14);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 35, 15);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 7, 16);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 40, 17);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 41, 18);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 42, 19);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 43, 20);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 8, 21);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 44, 22);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 45, 23);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 46, 24);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 47, 25);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 48, 26);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 11, 27);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 59, 28);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 60, 29);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 61, 30);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 62, 31);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user"
(
    "user_id"      int8                                       NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
),
    "dept_id"      int8,
    "user_name"    varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "nick_name"    varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "user_type"    varchar(2) COLLATE "pg_catalog"."default" DEFAULT 0,
    "email"        varchar(50) COLLATE "pg_catalog"."default",
    "phone_number" varchar(11) COLLATE "pg_catalog"."default",
    "sex"          char(1) COLLATE "pg_catalog"."default",
    "avatar"       varchar(100) COLLATE "pg_catalog"."default",
    "password"     varchar(100) COLLATE "pg_catalog"."default",
    "status"       char(1) COLLATE "pg_catalog"."default",
    "del_flag"     char(1) COLLATE "pg_catalog"."default"    DEFAULT 0,
    "login_ip"     varchar(128) COLLATE "pg_catalog"."default",
    "login_date"   timestamp(6),
    "create_by"    varchar(64) COLLATE "pg_catalog"."default",
    "create_time"  timestamp(6),
    "update_by"    varchar(64) COLLATE "pg_catalog"."default",
    "update_time"  timestamp(6),
    "remark"       varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT
ON COLUMN "public"."sys_user"."user_id" IS '用户ID';
COMMENT
ON COLUMN "public"."sys_user"."dept_id" IS '部门ID';
COMMENT
ON COLUMN "public"."sys_user"."user_name" IS '用户账号';
COMMENT
ON COLUMN "public"."sys_user"."nick_name" IS '用户昵称';
COMMENT
ON COLUMN "public"."sys_user"."user_type" IS '用户类型（00系统用户）';
COMMENT
ON COLUMN "public"."sys_user"."email" IS '用户邮箱';
COMMENT
ON COLUMN "public"."sys_user"."phone_number" IS '手机号码';
COMMENT
ON COLUMN "public"."sys_user"."sex" IS '用户性别（0男 1女 2未知）';
COMMENT
ON COLUMN "public"."sys_user"."avatar" IS '头像地址';
COMMENT
ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT
ON COLUMN "public"."sys_user"."status" IS '帐号状态（0正常 1停用）';
COMMENT
ON COLUMN "public"."sys_user"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT
ON COLUMN "public"."sys_user"."login_ip" IS '最后登录IP';
COMMENT
ON COLUMN "public"."sys_user"."login_date" IS '最后登录时间';
COMMENT
ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT
ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT
ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT
ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT
ON COLUMN "public"."sys_user"."remark" IS '备注';
COMMENT
ON TABLE "public"."sys_user" IS '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user"
VALUES (1, 103, 'admin', '充电管理员', '00', '123456@163.com', '13000000000', '1', '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1',
        '2024-06-14 14:04:42.997', 'admin', '2024-05-27 10:15:32', 'admin', '2024-06-14 14:04:42.704369', '管理员');
INSERT INTO "public"."sys_user"
VALUES (2, 100, '测试用户', '测试用户', NULL, NULL, '13012341234', '1', NULL,
        '$2a$10$HM3rLyBGiflNTo/p1RWqvua90ZhcXjHmrJbdUs/GzoH7OKzDg3bJi', '0', '2', NULL, NULL, 'admin',
        '2024-06-13 10:07:30.041454', 'admin', '2024-06-13 10:26:16.762749', '1111');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_post";
CREATE TABLE "public"."sys_user_post"
(
    "user_id" int8 NOT NULL,
    "post_id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1
),
    "id"      int8 NOT NULL
)
;
COMMENT
ON COLUMN "public"."sys_user_post"."user_id" IS '用户ID';
COMMENT
ON COLUMN "public"."sys_user_post"."post_id" IS '岗位ID';
COMMENT
ON COLUMN "public"."sys_user_post"."id" IS '主键ID';
COMMENT
ON TABLE "public"."sys_user_post" IS '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role"
(
    "user_id" int8 NOT NULL,
    "role_id" int8 NOT NULL,
    "id"      int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
        INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 10
)
)
;
COMMENT
ON COLUMN "public"."sys_user_role"."user_id" IS '用户ID';
COMMENT
ON COLUMN "public"."sys_user_role"."role_id" IS '角色ID';
COMMENT
ON COLUMN "public"."sys_user_role"."id" IS '主键ID';
COMMENT
ON TABLE "public"."sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role"
VALUES (1, 1, 1);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_config_config_id_seq"
    OWNED BY "public"."sys_config"."config_id";
SELECT setval('"public"."sys_config_config_id_seq"', 7, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dept_dept_id_seq"
    OWNED BY "public"."sys_dept"."dept_id";
SELECT setval('"public"."sys_dept_dept_id_seq"', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_data_dict_code_seq"
    OWNED BY "public"."sys_dict_data"."dict_code";
SELECT setval('"public"."sys_dict_data_dict_code_seq"', 39, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_type_dict_id_seq"
    OWNED BY "public"."sys_dict_type"."dict_id";
SELECT setval('"public"."sys_dict_type_dict_id_seq"', 20, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_log_job_log_id_seq"
    OWNED BY "public"."sys_job_log"."job_log_id";
SELECT setval('"public"."sys_job_log_job_log_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_login_info_info_id_seq"
    OWNED BY "public"."sys_login_info"."info_id";
SELECT setval('"public"."sys_login_info_info_id_seq"', 6, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_menu_menu_id_seq"
    OWNED BY "public"."sys_menu"."menu_id";
SELECT setval('"public"."sys_menu_menu_id_seq"', 85, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_notice_notice_id_seq"
    OWNED BY "public"."sys_notice"."notice_id";
SELECT setval('"public"."sys_notice_notice_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_oper_log_oper_id_seq"
    OWNED BY "public"."sys_oper_log"."oper_id";
SELECT setval('"public"."sys_oper_log_oper_id_seq"', 77, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_post_post_id_seq"
    OWNED BY "public"."sys_post"."post_id";
SELECT setval('"public"."sys_post_post_id_seq"', 1, true);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_dept_id_seq"
    OWNED BY "public"."sys_role_dept"."id";
SELECT setval('"public"."sys_role_dept_id_seq"', 2, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_menu_id_seq"
    OWNED BY "public"."sys_role_menu"."id";
SELECT setval('"public"."sys_role_menu_id_seq"', 32, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_role_id_seq"
    OWNED BY "public"."sys_role"."role_id";
SELECT setval('"public"."sys_role_role_id_seq"', 3, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_post_post_id_seq"
    OWNED BY "public"."sys_user_post"."post_id";
SELECT setval('"public"."sys_user_post_post_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_role_id_seq"
    OWNED BY "public"."sys_user_role"."id";
SELECT setval('"public"."sys_user_role_id_seq"', 3, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_user_id_seq"
    OWNED BY "public"."sys_user"."user_id";
SELECT setval('"public"."sys_user_user_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table qrtz_job_details
-- ----------------------------
ALTER TABLE "public"."qrtz_job_details"
    ADD CONSTRAINT "_copy_24" PRIMARY KEY ("sched_name", "job_name", "job_group");

-- ----------------------------
-- Primary Key structure for table qrtz_locks
-- ----------------------------
ALTER TABLE "public"."qrtz_locks"
    ADD CONSTRAINT "_copy_23" PRIMARY KEY ("sched_name", "lock_name");

-- ----------------------------
-- Primary Key structure for table qrtz_paused_trigger_grps
-- ----------------------------
ALTER TABLE "public"."qrtz_paused_trigger_grps"
    ADD CONSTRAINT "_copy_22" PRIMARY KEY ("sched_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_scheduler_state
-- ----------------------------
ALTER TABLE "public"."qrtz_scheduler_state"
    ADD CONSTRAINT "_copy_21" PRIMARY KEY ("sched_name", "instance_name");

-- ----------------------------
-- Primary Key structure for table qrtz_simple_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simple_triggers"
    ADD CONSTRAINT "_copy_20" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Primary Key structure for table qrtz_simprop_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simprop_triggers"
    ADD CONSTRAINT "_copy_19" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Indexes structure for table qrtz_triggers
-- ----------------------------
CREATE INDEX "sched_name" ON "public"."qrtz_triggers" USING btree (
    "sched_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
    "job_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
    "job_group" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table qrtz_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_triggers"
    ADD CONSTRAINT "_copy_18" PRIMARY KEY ("sched_name", "trigger_name", "trigger_group");

-- ----------------------------
-- Auto increment value for sys_config
-- ----------------------------
SELECT setval('"public"."sys_config_config_id_seq"', 7, false);

-- ----------------------------
-- Primary Key structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config"
    ADD CONSTRAINT "_copy_17" PRIMARY KEY ("config_id");

-- ----------------------------
-- Auto increment value for sys_dept
-- ----------------------------
SELECT setval('"public"."sys_dept_dept_id_seq"', 2, false);

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept"
    ADD CONSTRAINT "_copy_16" PRIMARY KEY ("dept_id");

-- ----------------------------
-- Auto increment value for sys_dict_data
-- ----------------------------
SELECT setval('"public"."sys_dict_data_dict_code_seq"', 39, true);

-- ----------------------------
-- Primary Key structure for table sys_dict_data
-- ----------------------------
ALTER TABLE "public"."sys_dict_data"
    ADD CONSTRAINT "_copy_15" PRIMARY KEY ("dict_code");

-- ----------------------------
-- Auto increment value for sys_dict_type
-- ----------------------------
SELECT setval('"public"."sys_dict_type_dict_id_seq"', 20, true);

-- ----------------------------
-- Indexes structure for table sys_dict_type
-- ----------------------------
CREATE UNIQUE INDEX "dict_type" ON "public"."sys_dict_type" USING btree (
    "dict_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "public"."sys_dict_type"
    ADD CONSTRAINT "_copy_14" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job"
    ADD CONSTRAINT "_copy_13" PRIMARY KEY ("job_id", "job_name", "job_group");

-- ----------------------------
-- Auto increment value for sys_job_log
-- ----------------------------
SELECT setval('"public"."sys_job_log_job_log_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table sys_job_log
-- ----------------------------
ALTER TABLE "public"."sys_job_log"
    ADD CONSTRAINT "_copy_12" PRIMARY KEY ("job_log_id");

-- ----------------------------
-- Auto increment value for sys_login_info
-- ----------------------------
SELECT setval('"public"."sys_login_info_info_id_seq"', 6, true);

-- ----------------------------
-- Indexes structure for table sys_login_info
-- ----------------------------
CREATE INDEX "idx_sys_logininfor_lt" ON "public"."sys_login_info" USING btree (
    "login_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_logininfor_s" ON "public"."sys_login_info" USING btree (
    "status" COLLATE "pg_catalog"."default" "pg_catalog"."bpchar_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_login_info
-- ----------------------------
ALTER TABLE "public"."sys_login_info"
    ADD CONSTRAINT "_copy_11" PRIMARY KEY ("info_id");

-- ----------------------------
-- Auto increment value for sys_menu
-- ----------------------------
SELECT setval('"public"."sys_menu_menu_id_seq"', 85, false);

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu"
    ADD CONSTRAINT "_copy_10" PRIMARY KEY ("menu_id");

-- ----------------------------
-- Auto increment value for sys_notice
-- ----------------------------
SELECT setval('"public"."sys_notice_notice_id_seq"', 1, true);

-- ----------------------------
-- Primary Key structure for table sys_notice
-- ----------------------------
ALTER TABLE "public"."sys_notice"
    ADD CONSTRAINT "_copy_9" PRIMARY KEY ("notice_id");

-- ----------------------------
-- Auto increment value for sys_oper_log
-- ----------------------------
SELECT setval('"public"."sys_oper_log_oper_id_seq"', 77, true);

-- ----------------------------
-- Indexes structure for table sys_oper_log
-- ----------------------------
CREATE INDEX "idx_sys_oper_log_bt" ON "public"."sys_oper_log" USING btree (
    "business_type" "pg_catalog"."int4_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_oper_log_ot" ON "public"."sys_oper_log" USING btree (
    "oper_time" "pg_catalog"."timestamp_ops" ASC NULLS LAST
    );
CREATE INDEX "idx_sys_oper_log_s" ON "public"."sys_oper_log" USING btree (
    "status" "pg_catalog"."int4_ops" ASC NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_oper_log
-- ----------------------------
ALTER TABLE "public"."sys_oper_log"
    ADD CONSTRAINT "_copy_8" PRIMARY KEY ("oper_id");

-- ----------------------------
-- Auto increment value for sys_post
-- ----------------------------
SELECT setval('"public"."sys_post_post_id_seq"', 1, true);

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post"
    ADD CONSTRAINT "_copy_7" PRIMARY KEY ("post_id");

-- ----------------------------
-- Auto increment value for sys_role
-- ----------------------------
SELECT setval('"public"."sys_role_role_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role"
    ADD CONSTRAINT "_copy_6" PRIMARY KEY ("role_id");

-- ----------------------------
-- Auto increment value for sys_role_dept
-- ----------------------------
SELECT setval('"public"."sys_role_dept_id_seq"', 2, false);

-- ----------------------------
-- Primary Key structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept"
    ADD CONSTRAINT "sys_role_dept_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for sys_role_menu
-- ----------------------------
SELECT setval('"public"."sys_role_menu_id_seq"', 32, false);

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu"
    ADD CONSTRAINT "_copy_4" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for sys_user
-- ----------------------------
SELECT setval('"public"."sys_user_user_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user"
    ADD CONSTRAINT "_copy_3" PRIMARY KEY ("user_id");

-- ----------------------------
-- Auto increment value for sys_user_post
-- ----------------------------
SELECT setval('"public"."sys_user_post_post_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table sys_user_post
-- ----------------------------
ALTER TABLE "public"."sys_user_post"
    ADD CONSTRAINT "_copy_2" PRIMARY KEY ("id");

-- ----------------------------
-- Auto increment value for sys_user_role
-- ----------------------------
SELECT setval('"public"."sys_user_role_id_seq"', 3, false);

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role"
    ADD CONSTRAINT "_copy_1" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table qrtz_simple_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simple_triggers"
    ADD CONSTRAINT "qrtz_simple_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table qrtz_simprop_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_simprop_triggers"
    ADD CONSTRAINT "qrtz_simprop_triggers_ibfk_1" FOREIGN KEY ("sched_name", "trigger_name", "trigger_group") REFERENCES "public"."qrtz_triggers" ("sched_name", "trigger_name", "trigger_group") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Keys structure for table qrtz_triggers
-- ----------------------------
ALTER TABLE "public"."qrtz_triggers"
    ADD CONSTRAINT "qrtz_triggers_ibfk_1" FOREIGN KEY ("sched_name", "job_name", "job_group") REFERENCES "public"."qrtz_job_details" ("sched_name", "job_name", "job_group") ON DELETE NO ACTION ON UPDATE NO ACTION;
