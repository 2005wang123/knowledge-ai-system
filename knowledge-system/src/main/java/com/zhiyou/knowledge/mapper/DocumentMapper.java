package com.zhiyou.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiyou.knowledge.entity.Document;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文档Mapper（MyBatis-Plus）
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {
    // 无需写SQL，MyBatis-Plus 自动实现CRUD
}