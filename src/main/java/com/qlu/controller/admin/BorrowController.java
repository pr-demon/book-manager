package com.qlu.controller.admin;

import com.qlu.bean.BorrowInfo;
import com.qlu.bean.User;
import com.qlu.bean.vo.BorrowInfoAndBookAndUserVo;
import com.qlu.common.bean.Page;
import com.qlu.service.IBookService;
import com.qlu.service.IBorrowInfoService;
import com.qlu.service.IPunishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/admin/borrow")
public class BorrowController {

    @Autowired
    private IBorrowInfoService borrowInfoService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IPunishService punishService;

    @GetMapping("/borrowInfo")
    public String to_borrowInfo() {
        return "/admin/borrow/borrowInfo";
    }

    @GetMapping("/orderInfo")
    public String to_orderInfo() {
        return "/admin/borrow/orderInfo";
    }

    @GetMapping("/returnInfo")
    public String to_returnInfo() {
        return "/admin/borrow/returnInfo";
    }

    /*
     *   获取所有已经借出的书籍信息
     * */
    @RequestMapping("/showBorrowList")
    @ResponseBody
    public Page<BorrowInfoAndBookAndUserVo> showBorrowList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize) {
        return new Page<>(pageSize,
                borrowInfoService.getBorrowOutBookInfo(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<BorrowInfoAndBookAndUserVo>(pageNum, pageSize)));
    }

    /*
     *   获取所有当前正在预约的图书信息
     * */
    @RequestMapping("/showOrderList")
    @ResponseBody
    public Page<BorrowInfoAndBookAndUserVo> showOrderList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize) {
        return new Page<>(pageSize,
                borrowInfoService.getBorrowCheckBookInfo(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<BorrowInfoAndBookAndUserVo>(pageNum, pageSize),
                        BorrowInfo.BORROW_CHECK_TIME));
    }

    /*
     *   获取所有已经归还的图书信息
     * */
    @RequestMapping("/showReturnList")
    @ResponseBody
    public Page<BorrowInfoAndBookAndUserVo> showReturnList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize) {
        return new Page<>(pageSize,
                borrowInfoService.getReturnedBookInfo(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<BorrowInfoAndBookAndUserVo>(pageNum, pageSize)));
    }

    @RequestMapping("/borrow")
    @ResponseBody
    @Transactional
    public String borrow(int bid) {
        /*
         *   图书预约成功， bid是borrowinfo表id
         * */
        BorrowInfo borrowInfo = borrowInfoService.getById(bid);
        /* 0 表示借阅成功 */
        borrowInfo.setIsReturn(0);
        borrowInfoService.updateById(borrowInfo);

        /*
        //book 改成 2
        Book book = bookService.getById(bid);
        book.setIsBorrow(2);
        //借书成功借阅量加一
        book.setBorrowTimes(book.getBorrowTimes() + 1);
        bookService.updateById(book);
        */
        return "success";
    }

    @RequestMapping("/returnBook")
    @ResponseBody
    @Transactional
    public String returnBook(int id, int bid) {
        /*
         *  id 是borrowinfo id
         * */
        BorrowInfo borrowInfo = borrowInfoService.getById(id);
        /*
        *   设置书籍应该归还的时间， 用于违规查询
        * */
        borrowInfo.setShouldReturnTime(borrowInfo.getReturnTime());
        /*
        *   设置书籍归还时间
        * */
        borrowInfo.setReturnTime(new Date());
        borrowInfo.setIsReturn(1);
        borrowInfoService.updateById(borrowInfo);

        /*
        *   检查用户是否超时归还
        * */
        if (borrowInfo.getShouldReturnTime().before(borrowInfo.getReturnTime())){
            punishService.addPunish(borrowInfo.getUid());
        }

        return "success";
    }

}
