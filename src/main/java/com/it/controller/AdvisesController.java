package com.it.controller;

import com.it.common.R;
import com.it.entity.Advises;
import com.it.service.AdivisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advises")
public class AdvisesController {
    @Autowired
    private AdivisesService adivisesService;

    /**
     * 管理员端的获取所有建议
     * @return
     */
    @GetMapping("/list")
    public R<List<Advises>> list(){
        try {
            List<Advises> advises = adivisesService.list();
            return R.success(advises);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    @PostMapping("/insert")
    public R<Advises> save(@RequestBody Advises advises){
        try {
            System.out.println(advises);
            adivisesService.save(advises);
            return R.success(advises);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    @DeleteMapping("/deleteIds")
    public R<String> deleteBatch(@RequestParam List<Integer> ids){
        boolean result = adivisesService.removeByIds(ids);
        if (result){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
    @DeleteMapping("/deleteSingleid")
    public R<String> deleteSingleid(@RequestParam Integer id){
        boolean result = adivisesService.removeById(id);
        if (result){
            return R.success("删除成功");
        }else {
            return R.error("删除失败");
        }
    }
}
