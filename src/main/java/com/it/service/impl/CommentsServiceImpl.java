package com.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.Comments;
import com.it.entity.Image;
import com.it.entity.LostFound;
import com.it.mapper.CommentsMapper;
import com.it.mapper.ImageMapper;
import com.it.service.CommentsService;
import com.it.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {
    @Override
    public void sendLostParentComments(Comments comments) {
        comments.setIsParent(1);
        save(comments);
    }
    @Override
    public void sendMissingParentComments(Comments comments) {
        comments.setIsParent(1);
        save(comments);
    }
    @Override
    public void sendMissingChildComments(Comments comments) {
        save(comments);
    }
    @Override
    public void sendLostChildComments(Comments comments) {
        save(comments);
    }
    @Override
    public List<Comments> getLostParentCommentsByid(int id,int category) {
        List<Comments> commentsList = this.list().stream().filter(Comments -> Comments.getIsParent() == 1&& Comments.getPostId() == id&& Comments.getCategory() == category).toList();
        return commentsList;
    }

    @Override
    public List<Comments> getMissingParentCommentsByid(int id,int category) {
        List<Comments> commentsList = this.list().stream().filter(Comments -> Comments.getIsParent() == 1&& Comments.getPostId() == id&& Comments.getCategory() == category).toList();
        return commentsList;
    }

    @Override
    public List<Comments> getLostChildComments(int parentId) {
        List<Comments> commentsList = this.list().stream().filter(Comments -> Comments.getParentId() == parentId).toList();
        return commentsList;
    }

    @Override
    public List<Comments> getMissingChildComments(int parentId) {
        List<Comments> commentsList = this.list().stream().filter(Comments -> Comments.getParentId() == parentId).toList();
        return commentsList;
    }
}
