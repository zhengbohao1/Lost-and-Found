package com.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.it.common.R;
import com.it.entity.AdminInfo;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
import com.it.entity.TradingVolume;
import com.it.exception.BusinessException;
import com.it.service.AdminInfoService;
import com.it.mapper.AdminInfoMapper;
import com.it.service.LostFoundService;
import com.it.service.MissingNoticesService;
import com.it.service.TradingVolumeService;
import com.it.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Yu
* @description 针对表【admin_info】的数据库操作Service实现
* @createDate 2024-09-19 19:34:19
*/

@Service
public class AdminInfoServiceImpl extends ServiceImpl<AdminInfoMapper, AdminInfo>
    implements AdminInfoService{

    @Resource
    private TradingVolumeService tradingVolumeService;
    @Resource
    private LostFoundService lostFoundService;
    @Resource
    private MissingNoticesService missingNoticesService;
    @Override
    public R<String> adminLogin(String email, String password) {
        // TODO: 校验管理员是否存在、密码是否正确
        AdminInfo adminInfo = this.baseMapper.selectByEmail(email);
        if(adminInfo == null || !adminInfo.getPassword().equals(password)){
            throw new BusinessException("邮箱或密码错误");
        }
        // TODO: 将管理员信息存储到token令牌中
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId",adminInfo.getAdminId());
        claims.put("nickName",adminInfo.getNickName());
        String token = JwtUtil.genToken(claims);
        return R.success(token);
    }

    @Override
    public int get_todayMissing_trade() {
        QueryWrapper<TradingVolume> queryWrapper = new QueryWrapper<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        queryWrapper.eq("date", currentDate)
                .eq("category", "寻物启事");
        return tradingVolumeService.getOne(queryWrapper).getNum();
    }

    @Override
    public int get_todayLost_trade() {
        QueryWrapper<TradingVolume> queryWrapper = new QueryWrapper<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        queryWrapper.eq("date", currentDate)
                .eq("category", "失物招领");
        return tradingVolumeService.getOne(queryWrapper).getNum();
    }

    @Override
    public Long get_todayLost_post() {
        QueryWrapper<LostFound> queryWrapper = new QueryWrapper<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        queryWrapper.like("created_at", currentDate);
        return lostFoundService.getBaseMapper().selectCount(queryWrapper);
    }

    @Override
    public Long get_todayMissing_post() {
        QueryWrapper<MissingNotices> queryWrapper = new QueryWrapper<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        queryWrapper.like("created_at", currentDate);
        return missingNoticesService.getBaseMapper().selectCount(queryWrapper);
    }

    @Override
    public List<TradingVolume> get_LostTrading_volume() {
        QueryWrapper<TradingVolume> queryWrapper = new QueryWrapper<>();
        return tradingVolumeService.list(queryWrapper.eq("category", "失物招领"));
    }

    @Override
    public List<TradingVolume> get_MissingTrading_volume() {
        QueryWrapper<TradingVolume> queryWrapper = new QueryWrapper<>();
        return tradingVolumeService.list(queryWrapper.eq("category", "寻物启事"));
    }
}




