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
    private Long id;           // 文档ID
    private String fileName;   // 文件名
    private String filePath;   // 文件存储路径
    private Long fileSize;     // 文件大小（字节）
    private Date createTime;   // 创建时间
    private Date updateTime;   // 更新时间
    private Integer isDeleted; // 是否删除：0-未删，1-已删

    // 新增：文档的文本内容字段
    private String content;    // 文档内容（用于AI问答）

    // 下面的 getContent() 和 setContent() 方法可以保留，
    // 也可以删除，因为 @Data 注解会自动生成
    // public String getContent() { return content; }
    // public void setContent(String content) { this.content = content; }
}