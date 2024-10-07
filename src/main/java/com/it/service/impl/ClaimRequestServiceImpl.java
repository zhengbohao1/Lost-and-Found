package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.entity.ClaimRequest;
import com.it.mapper.ClaimRequestMapper;
import com.it.service.ClaimRequestService;
import com.it.utils.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.List;
import java.util.Map;

@Service
public class ClaimRequestServiceImpl extends ServiceImpl<ClaimRequestMapper, ClaimRequest> implements ClaimRequestService {


    @Override
    public List<ClaimRequest> getClaimMessage() {
//        Map<String,Object> claims = ThreadLocalUtil.get();
//        String userId = (String) claims.get("userId");
        String userId = "1";
        QueryWrapper<ClaimRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("finder_id",userId);
        return this.list(queryWrapper);
    }
}
