-- ============================================================
-- Knowledge AI system: create table document
-- Note: this project uses MyBatis-Plus, which does NOT create tables
-- automatically. Spring Boot runs this file on startup via
-- spring.sql.init. CREATE TABLE IF NOT EXISTS makes repeated
-- execution safe. You can also run it manually in MySQL.
-- ============================================================

CREATE TABLE IF NOT EXISTS document (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    file_name   VARCHAR(255) NOT NULL COMMENT 'original file name',
    file_path   VARCHAR(500) NOT NULL COMMENT 'stored file path',
    file_size   BIGINT       DEFAULT 0 COMMENT 'file size in bytes',
    content     LONGTEXT     COMMENT 'parsed text content',
    create_time DATETIME     DEFAULT NULL COMMENT 'created time',
    update_time DATETIME     DEFAULT NULL COMMENT 'updated time',
    is_deleted  TINYINT      NOT NULL DEFAULT 0 COMMENT 'logic delete flag: 0-active, 1-deleted',
    PRIMARY KEY (id),
    KEY idx_create_time (create_time)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'knowledge document table';
