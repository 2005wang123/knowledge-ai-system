package com.zhiyou.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("document")
public class Document {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String filePath; // 对应数据库的 file_path

    private Long userId; // 对应数据库的 user_id

    private Date createTime; // 对应数据库的 create_time
}