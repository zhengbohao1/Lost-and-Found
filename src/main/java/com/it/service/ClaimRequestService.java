package com.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.it.entity.ClaimRequest;

import java.util.List;

public interface ClaimRequestService extends IService<ClaimRequest> {
    List<ClaimRequest> getClaimMessage();
}
