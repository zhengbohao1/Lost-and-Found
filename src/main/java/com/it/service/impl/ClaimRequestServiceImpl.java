package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.ClaimRequest;
import com.it.mapper.ClaimRequestMapper;
import com.it.service.ClaimRequestService;
import com.it.utils.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClaimRequestServiceImpl extends ServiceImpl<ClaimRequestMapper, ClaimRequest> implements ClaimRequestService {


    @Override
    public List<ClaimRequest> getClaimMessage() {
        Map<String,Object> claims = ThreadLocalUtil.get();
        String userId = (String) claims.get("userId");
        //String userId = "39";
        QueryWrapper<ClaimRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("finder_id",userId);
        List<ClaimRequest> claimRequestList = this.list(queryWrapper);
        List<ClaimRequest> sortedCommentsList = claimRequestList.stream()
                .sorted(Comparator.comparingInt(ClaimRequest::getReadStatus)) // 根据 readStatus 从低到高排序
                .collect(Collectors.toList());
        return sortedCommentsList;
    }

    @Override
    public void MarkClaimAsRead(int id) {
        QueryWrapper<ClaimRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        ClaimRequest claimRequest = this.getOne(queryWrapper);
        claimRequest.setReadStatus(1);
        this.updateById(claimRequest);
    }
    @Override
    public int getUnreadCount(String userId) {
        return this.baseMapper.getUnreadCount(userId);
    }
}
