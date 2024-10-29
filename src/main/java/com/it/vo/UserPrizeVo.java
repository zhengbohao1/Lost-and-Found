package com.it.vo;

import com.it.entity.UserPrize;
import lombok.Data;

/**
 * ClassName: UserPrizeVo
 * Description:
 * UserPrize的Vo类
 * @Author Joel
 * @Create 2024/10/26 13:31
 * @Version 1.0
 */

@Data
public class UserPrizeVo extends UserPrize {
    private String nickName;
}
