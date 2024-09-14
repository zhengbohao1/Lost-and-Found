package com.it.controller;

import com.it.common.R;
import com.it.entity.LostFound;
import com.it.service.LostFoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lost_found")
public class LostFoundController{
    @Autowired
    private LostFoundService lostFoundService;
    @GetMapping("/list")
    public R<List<LostFound>> list(){
        try {
            List<LostFound> lostFounds = lostFoundService.list();
            return R.success(lostFounds);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    @PostMapping
    public R<LostFound> save(@RequestBody LostFound lostFound) {
        try {
            lostFoundService.save(lostFound);
            return R.success(lostFound);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
