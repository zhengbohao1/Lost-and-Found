package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.Comments;
import com.it.entity.Image;

import java.util.List;

public interface CommentsService extends IService<Comments> {
    void sendLostParentComments(Comments comments);
    void sendMissingParentComments(Comments comments);
    void sendLostChildComments(Comments comments);
    void sendMissingChildComments(Comments comments);
    List<Comments>getLostParentCommentsByid(int id);
    List<Comments>getMissingParentCommentsByid(int Id);
    List<Comments>getLostChildComments(int parentId);
    List<Comments>getMissingChildComments(int parentId);
}
