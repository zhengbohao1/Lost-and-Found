package com.it.controller;

import com.it.common.R;
import com.it.entity.Advises;
import com.it.entity.LostFound;
import com.it.exception.BusinessException;
import com.it.service.LostFoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lost_found")
public class LostFoundController{
    @Autowired
    private LostFoundService lostFoundService;

    /**
     * 管理员端的获取所有失物招领
     * @return
     */
    @GetMapping("/list")
    public R<List<LostFound>> list(){
        try {
            List<LostFound> lostFounds = lostFoundService.list();
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
    public R<List<LostFound>> legalList(){
        try {
            List<LostFound> lostFounds = lostFoundService.legalList();
            return R.success(lostFounds);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    @PostMapping
    public R<LostFound> save(@RequestBody LostFound lostFound) {

        try {
            System.out.println(lostFound);
            lostFoundService.save(lostFound);
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
        return R.success("审核成功~");
    }
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
        return R.success("拒绝成功~");
    }
    @GetMapping("/query")
    public R<List<LostFound>> query(String content){
        List<LostFound> advises = null;
        try {
            advises = lostFoundService.selectByCondition(content);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(advises);
    }
}
