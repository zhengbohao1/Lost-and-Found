package com.it.controller;

import com.it.common.R;
import com.it.constants.Constants;
import com.it.entity.Prize;
import com.it.entity.UserPrize;
import com.it.mapper.UserInfoMapper;
import com.it.service.PrizeService;
import com.it.utils.ThreadLocalUtil;
import com.it.vo.UserPrizeVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
public class PrizeController extends CommonController {

    @Resource
    private PrizeService prizeService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Value("${project.folder}")
    private String baseFolder;

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
     * 获取奖品图片
     * @param response
     * @param prizeImageUrl
     */
    @GetMapping("/image")
    public void getPrizeImage(HttpServletResponse response, @RequestParam String prizeImageUrl){
        String prizeImagePath = baseFolder + Constants.FILE_FOLDER_PRIZE_NAME + prizeImageUrl;
        response.setContentType("image/jpeg");
        readFile(response, prizeImagePath);
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

    /**
     * 获取所有用户兑换奖品的记录信息
     * @return
     */
    @GetMapping("/userPrizeList")
    public R<List<UserPrizeVo>> getUserPrizeList() {
        List<UserPrize> userPrizeList = prizeService.getUserPrizeList();
        List<UserPrizeVo> userPrizeVoList = new ArrayList<>();
        for (UserPrize userPrize : userPrizeList) {
            UserPrizeVo userPrizeVo = new UserPrizeVo();
            BeanUtils.copyProperties(userPrize, userPrizeVo);
            userPrizeVoList.add(userPrizeVo);
        }
        userPrizeVoList.forEach(userPrizeVo ->
            userPrizeVo.setNickName(userInfoMapper.selectNickNameByUserId(userPrizeVo.getUserId()))
        );
        return R.success(userPrizeVoList);
    }
}
