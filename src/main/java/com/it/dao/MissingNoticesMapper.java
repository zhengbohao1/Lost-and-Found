package com.it.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MissingNoticesMapper extends BaseMapper<MissingNotices> {
}
