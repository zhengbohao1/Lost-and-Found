package com.it.mapper;

import com.it.common.R;
import com.it.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
* @author Yu
* @description 针对表【user_info】的数据库操作Mapper
* @createDate 2024-09-19 19:34:19
* @Entity com.it.entity.UserInfo
*/

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 根据email查询用户信息
     * @param email
     * @return
     */
    @Select("SELECT * FROM user_info WHERE email = #{email}")
    UserInfo selectByEmail(String email);

    /**
     * 根据nickName查询用户信息
     * @param nickName
     * @return
     */
    @Select("SELECT * FROM user_info WHERE nick_name = #{nickName}")
    UserInfo selectByNickName(String nickName);


    /**
     * 更新用户最后登录时间
     * @param userId
     * @param date
     */
    @Update("UPDATE user_info SET last_login_time = #{date} WHERE user_id = #{userId}")
    void updateLastLoginTimeById(String userId, Date date);

    /**
     * 更新用户密码
     * @param encodeByMD5 加密后的密码
     * @param userId
     */
    @Update("UPDATE user_info SET password = #{encodeByMD5} WHERE user_id = #{userId}")
    void updatePwd(String encodeByMD5, String userId);

    /**
     * 根据email重置用户密码
     * @param encodeByMD5
     * @param email
     */
    @Update("UPDATE user_info SET password = #{encodeByMD5} WHERE email = #{email}")
    void updatePwdByEmail(String encodeByMD5, String email);

    /**
     * 查询所有用户信息
     * @return
     */
    @Select("SELECT * FROM user_info")
    List<UserInfo> selectAllUserInfo();

    /**
     * 根据用户id禁用用户
     * @param userId
     */
    @Update("UPDATE user_info SET status = 0 WHERE user_id = #{userId}")
    void disableUser(String  userId);

    /**
     * 根据用户id查询用户昵称
     * @param userId
     * @return
     */
    @Select("SELECT nick_name FROM user_info WHERE user_id = #{userId}")
    String selectNickNameByUserId(String userId);
}




