package com.it.controller;

import com.it.common.R;
import com.it.entity.Prize;
import com.it.mapper.UserInfoMapper;
import com.it.service.PrizeService;
import com.it.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName: PrizeController
 * Description:
 * 奖品相关的控制层
 * @Author Joel
 * @Create 2024/10/9 14:24
 * @Version 1.0
 */

@RestController
@RequestMapping("/prize")
public class PrizeController {

    @Resource
    private PrizeService prizeService;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 获取所有奖品信息
     * @return
     */
    @GetMapping("/list")
    public R<List<Prize>> selectPrizeList() {
        List<Prize> prizeList = prizeService.selectPrizeList();
        return R.success(prizeList);
    }

    /**
     * 管理员删除奖品
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public R<String> deletePrize(@RequestParam Integer id) {
        prizeService.deletePrize(id);
        return R.success(null);
    }

    /**
     * 管理员更新奖品库存
     * @param id 奖品id
     * @param num 更新后的库存
     * @return
     */
    @PutMapping("/updateNum")
    public R<String> addPrizeNum(@RequestParam Integer id, @RequestParam Integer num) {
        prizeService.updatePrizeNum(id, num);
        return R.success(null);
    }

    /**
     * 管理员增加新的奖品
     * @param prize
     * @return
     */
    @PostMapping("/add")
    public R<String> addPrize(@RequestBody Prize prize) {
        prizeService.addPrize(prize);
        return R.success(null);
    }

    /**
     * 兑换奖品
     * @param id
     * @return
     */
    @PutMapping("/exchange")
    public R<String> exchangePrize(@RequestParam Integer id) {
        // 从ThreadLocal中获取用户userId
        Map<String,Object> claims = ThreadLocalUtil.get();
        String userId = (String) claims.get("userId");
        prizeService.exchangePrize(id, userId);
        return R.success(null);
    }
}
