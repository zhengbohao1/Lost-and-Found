package com.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.it.entity.Image;
import com.it.entity.Matching;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchingMapper extends BaseMapper<Matching> {
}
