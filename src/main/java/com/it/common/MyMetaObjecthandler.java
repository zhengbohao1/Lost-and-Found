package com.it.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
        metaObject.setValue("createdAt", LocalDateTime.now());
        metaObject.setValue("updatedAt", LocalDateTime.now());
    }
    //修改时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updatedAt", LocalDateTime.now());
    }
}
