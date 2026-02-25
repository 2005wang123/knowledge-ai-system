package com.zhiyou.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiyou.knowledge.entity.Document;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文档Mapper（仅基础CRUD）
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {
    // 无需自定义方法，BaseMapper已包含所有基础CRUD
}