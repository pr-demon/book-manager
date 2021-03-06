package com.qlu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qlu.bean.BookDonation;
import com.qlu.common.bean.Page;
import com.qlu.service.IBookDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookDonation")
public class BookDonationController {

    @Autowired
    private IBookDonationService bookDonationService;

    @GetMapping("/add")
    public String to_add() {
        return "/bookDonation/add";
    }

    @GetMapping("/list")
    public String to_list() {
        return "/bookDonation/list";
    }

    @PostMapping("/add")
    public String add(BookDonation bookDonation) {
        // 验证书籍数量
        if (bookDonation.getBookCount() == null || bookDonation.getBookCount() < 1) {
            bookDonation.setBookCount(1);
        }

        bookDonation.setPicture("/pic/library");
        bookDonation.setStatus(0);
        bookDonationService.save(bookDonation);
        return "/bookDonation/list";
    }

    @RequestMapping("/showBookDonationList")
    @ResponseBody
    public Page<BookDonation> historyList(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "5") int pageSize,
                                          @RequestParam("uid") int uid) {
        QueryWrapper<BookDonation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);

        return new Page<>(pageSize,
                bookDonationService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), queryWrapper));
    }


}
