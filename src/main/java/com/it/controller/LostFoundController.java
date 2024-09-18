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
            return R.error(e.getMessage());
        }
    }
    @PostMapping
    public R<LostFound> save(@RequestBody LostFound lostFound) {

        try {
            System.out.println(lostFound);
            lostFoundService.save(lostFound);
            return R.success(lostFound);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
