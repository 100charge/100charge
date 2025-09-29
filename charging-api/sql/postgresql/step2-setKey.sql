DO
$$
    DECLARE
table_rec RECORD;
        column_rec
RECORD;
        sequence_name
TEXT;
        max_id
BIGINT;
BEGIN
        -- 循环遍历当前数据库中的所有表
FOR table_rec IN
SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema NOT IN ('information_schema', 'pg_catalog')
  AND table_type = 'BASE TABLE' LOOP
                -- 循环遍历每个表的所有列
                FOR column_rec IN
SELECT column_name
FROM information_schema.columns
WHERE table_schema = table_rec.table_schema
  AND table_name = table_rec.table_name LOOP
                        -- 检查列是否是自增主键
                        EXECUTE format('
                SELECT pg_get_serial_sequence(%L, %L)',
                                       table_rec.table_schema || '.' || table_rec.table_name,
                                       column_rec.column_name
                            )
INTO sequence_name;

IF
sequence_name IS NOT NULL THEN
                            -- 获取当前表中主键的最大值
                            EXECUTE format('
                    SELECT MAX(%I)
                    FROM %I.%I',
                                           column_rec.column_name,
                                           table_rec.table_schema,
                                           table_rec.table_name
                                ) INTO max_id;

                            -- 设置序列的起始值为当前表中主键的最大值+1
                            IF
max_id IS NOT NULL THEN
                                EXECUTE format('
                        ALTER SEQUENCE %s RESTART WITH %s CACHE 10',
                                               sequence_name, max_id + 1
                                    );
END IF;
END IF;
END LOOP;
END LOOP;
    END$$;