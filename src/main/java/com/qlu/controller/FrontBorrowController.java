package com.qlu.controller;

import com.qlu.bean.BorrowInfo;
import com.qlu.bean.vo.BorrowInfoAndBookAndUserVo;
import com.qlu.common.bean.Page;
import com.qlu.service.IBorrowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/borrow")
public class FrontBorrowController {

    @Autowired
    private IBorrowInfoService borrowInfoService;


    @GetMapping("/borrowList")
    public String to_BorrowList() {
        return "/user/borrowList";
    }

    @GetMapping("/historyList")
    public String to_historyList() {
        return "/user/historyList";
    }


    /*
     *   显示所有 正在借阅 和 正在预约的图书
     * */
    @RequestMapping("/showBorrowList")
    @ResponseBody
    public Page<BorrowInfo> borrowList(@RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "5") int pageSize,
                                       @RequestParam("uid") int uid) {
        return new Page<>(pageSize, borrowInfoService.getBorrowCheckAndOut(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), uid, BorrowInfo.BORROW_CHECK_TIME));
    }

    /*
     *   显示所有已经归还的图书
     * */
    @RequestMapping("/showHistoryList")
    @ResponseBody
    public Page<BorrowInfoAndBookAndUserVo> historyList(@RequestParam(defaultValue = "1") int pageNum,
                                                        @RequestParam(defaultValue = "5") int pageSize,
                                                        @RequestParam("uid") int uid) {
        return new Page<>(pageSize, borrowInfoService.getReturnedHistory(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), uid));
    }

    @PostMapping("/delayApply")
    @ResponseBody
    @Transactional
    public String delayApply(int id) {
        BorrowInfo borrowInfo = borrowInfoService.getById(id);
        Date returnTime = borrowInfo.getReturnTime();
        long time = returnTime.getTime();
        //延长七天
        long time2 = time + 24 * 60 * 60 * 7 * 1000;
        returnTime = new Date(time2);
        borrowInfo.setReturnTime(returnTime);
        borrowInfoService.updateById(borrowInfo);
        return "success";
    }

}
