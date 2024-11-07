package com.it.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.it.common.R;
import com.it.dto.LostFoundDto;
import com.it.dto.MissingNoticesDto;
import com.it.entity.LostFound;
import com.it.entity.Matching;
import com.it.entity.MissingNotices;
import com.it.service.ILostFoundService;
import com.it.service.IMissingNoticesService;
import com.it.service.MatchingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/matching")
public class MatchingController {
    @Resource
    private MatchingService matchingService;
    @GetMapping("/getMissingMatchPosts")
    public R<List<MissingNoticesDto>> getMatchPosts(String userId){
        return matchingService.getMatchPosts(userId);
    }
    @GetMapping("/getLostMatchPosts")
    public R<List<LostFoundDto>> getLostMatchPosts(String userId){
        return matchingService.getLostMatchPosts(userId);
    }
}
