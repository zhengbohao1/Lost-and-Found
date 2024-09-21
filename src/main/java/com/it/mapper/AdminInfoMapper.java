package com.it.mapper;

import com.it.entity.AdminInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author Yu
* @description 针对表【admin_info】的数据库操作Mapper
* @createDate 2024-09-19 19:34:19
* @Entity com.it.entity.AdminInfo
*/

@Mapper
public interface AdminInfoMapper extends BaseMapper<AdminInfo> {

    // 根据邮箱查询管理员信息
    @Select("SELECT * FROM admin_info WHERE email = #{email}")
    AdminInfo selectByEmail(String email);
}




