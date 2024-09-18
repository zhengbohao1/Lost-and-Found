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
}
