package com.it.controller;

import com.it.common.R;
import com.it.dto.MissingNoticesDto;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
import com.it.exception.BusinessException;
import com.it.service.LostFoundService;
import com.it.service.MissingNoticesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missing_notices")
public class MissingNoticesController {
    @Autowired
    private MissingNoticesService missingNoticesService;

    /**
     * 用于获取所有失物招领信息
     * @return
     */
    @GetMapping("/list")
    public R<List<MissingNoticesDto>> list(){
        try {
            List<MissingNoticesDto> missingNotices = missingNoticesService.get_list();
            return R.success(missingNotices);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 用于获取审核通过的招领信息
     * @return
     */
    @GetMapping("/legal_list")
    public R<List<MissingNoticesDto>> legalList() {
        try {
            List<MissingNoticesDto> missingNotices = missingNoticesService.legalList();
            return R.success(missingNotices);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    @PostMapping
    public R<MissingNotices> save(@RequestBody MissingNotices missingNotices) {
        try {
            missingNoticesService.my_save(missingNotices);
            return R.success(missingNotices);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    @DeleteMapping("/deleteIds")
    public R<String> deleteBatch(@RequestParam List<Integer> ids){
        boolean result = missingNoticesService.removeByIds(ids);
        if (result){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
    @DeleteMapping("/deleteSingleid")
    public R<String> deleteSingleid(@RequestParam Integer id){
        boolean result = missingNoticesService.removeById(id);
        if (result){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
    @PutMapping("/approve_by_id")
    public R<String> review(@RequestParam Integer id){
        MissingNotices missingNotices = missingNoticesService.getById(id);
        int reviewProcess = missingNotices.getReviewProcess();
        if (reviewProcess == 1){
            return R.error("该信息已审核通过，不可重复通过哦~");
        }
        missingNotices.setReviewProcess(1);
        missingNoticesService.updateById(missingNotices);
        return R.success("审核成功~");
    }
    @PutMapping("/reject_by_id")
    public R<String> reject(@RequestParam Integer id){
        MissingNotices missingNotices = missingNoticesService.getById(id);
        int reviewProcess = missingNotices.getReviewProcess();
        if (reviewProcess == 1){
            return R.error("该建议已审核通过，请选择删除~");
        }
        if (reviewProcess == 2){
            return R.error("该信息已审核拒绝，不可重复拒绝哦~");
        }
        missingNotices.setReviewProcess(2);
        missingNoticesService.updateById(missingNotices);
        return R.success("拒绝成功~");
    }
    @GetMapping("/query")
    public R<List<MissingNoticesDto>> query(String content){
        List<MissingNoticesDto> missingNotices = null;
        try {
            missingNotices = missingNoticesService.selectByCondition(content);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return R.success(missingNotices);
    }
}
