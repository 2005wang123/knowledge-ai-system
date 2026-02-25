package com.zhiyou.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

/**
 * 文档实体类（无向量相关字段）
 */
@Data
@TableName("document")
public class Document {
    /** 文档主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文件名 */
    @TableField("file_name")
    private String fileName;

    /** 文件存储路径 */
    @TableField("file_path")
    private String filePath;

    /** 文件大小（字节） */
    @TableField("file_size")
    private Long fileSize;

    /** 文档文本内容（仅存储，无向量处理） */
    @TableField("content")
    private String content;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 逻辑删除：0-未删，1-已删 */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}