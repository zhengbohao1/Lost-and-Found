package com.it.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.it.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 干的事是公共字段自动注入
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    //插入时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());
        if (metaObject.hasSetter("createdAt")) {
            metaObject.setValue("createdAt", LocalDateTime.now());
        }

        if (metaObject.hasSetter("updatedAt")) {
            metaObject.setValue("updatedAt", LocalDateTime.now());
        }

        if (metaObject.hasSetter("userId")) {
            // 从ThreadLocal中获取用户id
//            Map<String,Object> claims = ThreadLocalUtil.get();
//            String userId = (String) claims.get("userId");
            String userId = "123（记得改）";
            metaObject.setValue("userId",userId);
        }
        if(metaObject.hasSetter("userName")){
//            Map<String,Object> map = ThreadLocalUtil.get();
//            String nickName = (String) map.get("nickName");
            String nickName = "test(记得改)";
            metaObject.setValue("userName",nickName);
        }
    }
    //修改时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updatedAt", LocalDateTime.now());
    }
}
