package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.Comments;
import com.it.entity.Image;

import java.util.List;

public interface CommentsService extends IService<Comments> {
    void sendParentComments(Comments comments);
    void sendChildComments(Comments comments);
    List<Comments>getParentComments();
    List<Comments>getChildComments(int parentId);
}
