package com.it.controller;

import com.it.common.R;
import com.it.entity.LostFound;
import com.it.entity.MissingNotices;
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
    @GetMapping("/list")
    public R<List<MissingNotices>> list(){
        try {
            List<MissingNotices> missingNotices = missingNoticesService.list();
            return R.success(missingNotices);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    @PostMapping
    public R<MissingNotices> save(@RequestBody MissingNotices missingNotices) {
        try {
            missingNoticesService.save(missingNotices);
            return R.success(missingNotices);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
