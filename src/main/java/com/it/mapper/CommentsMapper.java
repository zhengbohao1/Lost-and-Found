package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.ClaimRequest;
import com.it.entity.Comments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {
}
