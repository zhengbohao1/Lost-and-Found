package com.it.mapper;

import com.it.entity.EmailCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author Yu
* @description 针对表【email_code】的数据库操作Mapper
* @createDate 2024-09-19 19:34:19
* @Entity com.it.entity.EmailCode
*/

@Mapper
public interface EmailCodeMapper extends BaseMapper<EmailCode> {

    /**
     * 使邮箱验证码失效
     * @param email
     */
    @Update("UPDATE email_code SET status = 1 WHERE email = #{email} and status = 0")
    void disableEmailCode(String email);

    /**
     * 邮箱验证码是否存在
     * @param email
     * @return
     */
    @Select("SELECT * FROM email_code WHERE email = #{email} and code = #{emailCode}")
    EmailCode selectByEmailAndCode(String email, String emailCode);

}




