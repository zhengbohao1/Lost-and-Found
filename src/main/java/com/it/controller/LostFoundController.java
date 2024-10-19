package com.it.controller;

import com.it.common.R;
import com.it.dto.LostFoundDto;
import com.it.entity.Advises;
import com.it.entity.LostFound;
import com.it.exception.BusinessException;
import com.it.service.LostFoundService;
import com.it.service.MessageNotificationService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lost_found")
public class LostFoundController{
    @Autowired
    private LostFoundService lostFoundService;
    @Resource
    MessageNotificationService messageNotificationService;
    /**
     * 管理员端的获取所有失物招领
     * @return
     */
    @GetMapping("/list")
    public R<List<LostFoundDto>> list(){
        try {
            List<LostFoundDto> lostFounds = lostFoundService.get_list();
            return R.success(lostFounds);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    /**
     * 获取审核成功的失物招领
     * @return
     */
    @GetMapping("/legal_list")
    public R<List<LostFoundDto>> legalList(){
        try {
            List<LostFoundDto> lostFoundsdto = lostFoundService.legalList();
            return R.success(lostFoundsdto);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 上传新帖（待审核）
     * @param lostFound
     * @return
     */
    @PostMapping
    public R<LostFound> save(@RequestBody LostFound lostFound) {

        try {
            System.out.println(lostFound);
            lostFoundService.my_save(lostFound);
            return R.success(lostFound);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    @DeleteMapping("/deleteIds")
    public R<String> deleteBatch(@RequestParam List<Integer> ids){
        boolean result = lostFoundService.removeByIds(ids);
        if (result){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
    @DeleteMapping("/deleteSingleid")
    public R<String> deleteSingleid(@RequestParam Integer id){
        boolean result = lostFoundService.removeById(id);
        if (result){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
    /**
     * 对招领信息进行审核，审核成功后，将审核状态置为1
     */
    @PutMapping("/approve_by_id")
    public R<String> review(@RequestParam Integer id){
        LostFound lostFound = lostFoundService.getById(id);
        int reviewProcess = lostFound.getReviewProcess();
        if (reviewProcess == 1){
            return R.error("该信息已审核通过，不可重复通过哦~");
        }
        lostFound.setReviewProcess(1);
        lostFoundService.updateById(lostFound);
        messageNotificationService.sendApprovedMessage(String.valueOf(lostFound.getFinderId()),lostFound.getId(),0);
        return R.success("审核成功~");
    }

    /**
     * 审核不通过
     * @param id
     * @return
     */
    @PutMapping("/reject_by_id")
    public R<String> reject(@RequestParam Integer id){
        LostFound lostFound = lostFoundService.getById(id);
        int reviewProcess = lostFound.getReviewProcess();
        if (reviewProcess == 1){
            return R.error("该建议已审核通过，请选择删除~");
        }
        if (reviewProcess == 2){
            return R.error("该信息已审核拒绝，不可重复拒绝哦~");
        }
        lostFound.setReviewProcess(2);
        lostFoundService.updateById(lostFound);
        messageNotificationService.sendRejectedMessage(String.valueOf(lostFound.getFinderId()),lostFound.getId(),0);
        return R.success("拒绝成功~");
    }

    /**
     * 模糊查询，用于查询与搜索内容相似的帖子
     * @param content
     * @return
     */
    @GetMapping("/query")
    public R<List<LostFoundDto>> query(String content){
        List<LostFoundDto> advises = null;
        try {
            advises = lostFoundService.selectByCondition(content);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(advises);
    }
    @GetMapping("/getbyid")
    public R<LostFoundDto> getById(@RequestParam Integer id){
        LostFoundDto lostFound = lostFoundService.getBypostId(id);
        return R.success(lostFound);
    }
    @GetMapping("/getLegalPostByUserId")
    public R<List<LostFoundDto>> getByUserId(@RequestParam String userId){
        List<LostFoundDto> byUserId = lostFoundService.getByUserId(userId);
        return R.success(byUserId);
    }
    @GetMapping("/getIllegalByUserId")
    public R<List<LostFoundDto>> getIllegalByUserId(@RequestParam String userId){
        List<LostFoundDto> illegalByUserId = lostFoundService.getIllegalByUserId(userId);
        return R.success(illegalByUserId);
    }
    @GetMapping("/getWaitByUserId")
    public R<List<LostFoundDto>> getWaitByUserId(@RequestParam String userId){
        List<LostFoundDto> waitByUserId = lostFoundService.getWaitByUserId(userId);
        return R.success(waitByUserId);
    }

    /**
     * 编辑更改lostfound属性
     * @return
     */
    @PutMapping("/updateLostFound")
    public R<String> updateLostFound(@RequestBody LostFound lostFound){
        lostFoundService.UpdateLostfound(lostFound);
        return R.success("修改成功");
    }

    /**
     * 用户查询审核状态
     */
}
