package com.qlu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qlu.bean.*;
import com.qlu.bean.vo.BookAndUserVo;
import com.qlu.common.bean.Page;
import com.qlu.service.IBookService;
import com.qlu.service.IBorrowInfoService;
import com.qlu.service.IPunishService;
import com.qlu.service.IStackRoomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/book")
public class FrontBookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private IStackRoomService stackRoomService;

    @Autowired
    private IBorrowInfoService borrowInfoService;
    @Autowired
    private IPunishService punishService;

    @RequestMapping("/list")
    @ResponseBody
    public Page<BookAndUserVo> queryList(@RequestParam(defaultValue = "1") int pageNum,
                                @RequestParam(defaultValue = "8") int pageSize,
                                @RequestParam(defaultValue = "") String query
                                ,HttpSession session) {
        System.out.println("=====");
        /*
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Book> mybatisPage =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(book.getBookName())) {
            bookQueryWrapper.like("book_name", book.getBookName());
        }
        if (!StringUtils.isEmpty(book.getAuthor())) {
            bookQueryWrapper.like("author", book.getAuthor());
        }
        if (!StringUtils.isEmpty(book.getType())) {
            bookQueryWrapper.like("type", book.getType());
        }
        if (book.getIsBorrow() != null) {
            if (book.getIsBorrow() == 1) {
                //借出
                bookQueryWrapper.eq("is_borrow", 1);
            } else {
                //未借出
                bookQueryWrapper.eq("is_borrow", 0);
            }
        }
        bookQueryWrapper.orderByDesc("borrow_times");
        bookService.page(mybatisPage, bookQueryWrapper);
        Page<Book> page = new Page<>(pageSize, mybatisPage);
        */

        User user = (User)session.getAttribute("user");
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<BookAndUserVo> books =  bookService.getAllBookInfo(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize)
                ,user.getId()
                ,BorrowInfo.BORROW_CHECK_TIME
                ,query
        );
        /*
        System.out.println("首页查询， 用户ID： " + user.getId());
        for (BookAndUserVo record : books.getRecords()) {
            System.out.println(record.getId() + " " + record.toString());
        }
        */
        Page<BookAndUserVo> page = new Page<>(pageSize, books);
        return page;
    }

    @RequestMapping("/search")
    @ResponseBody
    public List<Book> search(String query) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("book_name", query).or().like("author", query).or().like("type", query);
        List<Book> list = bookService.list(queryWrapper);
        return list;
    }

    /* 正在预约中的图书数量 */
    private int bookBorrowCheckCount(int bookId) {
        List<BorrowInfo> borrowInfos = borrowInfoService.list(new QueryWrapper<BorrowInfo>().eq("bid", bookId).eq("is_return", -1).apply("datediff(now(), borrow_time) <= " + BorrowInfo.BORROW_CHECK_TIME));
        if (borrowInfos != null) {
            return borrowInfos.size();
        }
        return 0;
    }

    /* 已经被借出的图书数量 */
    private int bookBorrowedCount(int bookId) {
        List<BorrowInfo> borrowInfos = borrowInfoService.list(new QueryWrapper<BorrowInfo>().eq("bid", bookId).eq("is_return", 0));
        if (borrowInfos != null) {
            return borrowInfos.size();
        }
        return 0;
    }

    private boolean existNotReturnBook(int uid){
        List<BorrowInfo> borrowInfos = borrowInfoService.list(new QueryWrapper<BorrowInfo>().eq("uid", uid).eq("is_return", 0).apply(" return_time < now()"));
        return borrowInfos != null && borrowInfos.size() > 0;
    }

    public int isBorrowAble(int userId, int bookId) {
        // 查看用户是否已经借到了这本书
        List<BorrowInfo> borrowInfos = borrowInfoService.list(new QueryWrapper<BorrowInfo>().eq("uid", userId).eq("bid", bookId).eq("is_return", 0));
        if (borrowInfos != null && borrowInfos.size() > 0) {
            return -1;
        }
        // 查看用户是否已经预约了这本书
        borrowInfos = borrowInfoService.list(new QueryWrapper<BorrowInfo>().eq("uid", userId).eq("bid", bookId).eq("is_return", -1).apply("datediff(now(), borrow_time) <= " + BorrowInfo.BORROW_CHECK_TIME));
        if (borrowInfos != null && borrowInfos.size() > 0) {
            return -2;
        }
        int bookCount = bookService.getOne(new QueryWrapper<Book>().eq("id", bookId)).getBookCount();
        int borrowCheckCount = bookBorrowCheckCount(bookId);
        int borrowOutCount = bookBorrowedCount(bookId);
        if (bookCount <= borrowCheckCount + borrowOutCount) {
            return -3;
        }
        /*
        *   查看用户是否有：
        *       1. 超时未还的书籍
        *       2. 用户当前处于惩戒期中
        * */
        if (existNotReturnBook(userId)){
            return -4;
        }
        Punish punish = punishService.getPunish(userId);
        if (punish != null && punish.getReleaseTime().after(new Date())){
            return -5;
        }
        return 0;
    }


    @GetMapping("/info")
    public String info(int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        Book book = bookService.getById(id);
        StackRoom stackRoom = stackRoomService.getById(book.getSrid());
        BorrowInfo borrowInfo = borrowInfoService.getOne(new QueryWrapper<BorrowInfo>().eq("bid", id).eq("is_return", 0));

        model.addAttribute("bookInfo", book);
        model.addAttribute("stackRoom", stackRoom);
        model.addAttribute("borrowInfo", borrowInfo);

        /* 查看当前用户是否可以借阅这本书 */
        int code = isBorrowAble(user.getId(), book.getId());
        model.addAttribute("isBorrowAble", code);

        if (code == -5){
            String releaseTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(punishService.getPunish(user.getId()).getReleaseTime());
            model.addAttribute("releaseTime", releaseTime);
        }

        return "/book/info";
    }

    @PostMapping("/order")
    @ResponseBody
    @Transactional
    public String order(int uid, int bid) {
        //查询预约借了几本书  如果预约了5本就不用动
        /*
        int count = borrowInfoService.count(new QueryWrapper<BorrowInfo>().eq("is_return", 0));
        if (count==5){
            return "wrong";
        }
        */
        int code = isBorrowAble(uid, bid);
        if (code != 0) {
            return "wrong";
        }

        // 添加预约信息
        BorrowInfo borrowInfo = new BorrowInfo();
        borrowInfo.setBid(bid);
        borrowInfo.setBorrowTime(new Date());
        borrowInfo.setUid(uid);
        Calendar instance = Calendar.getInstance();
        //归还时间
        instance.add(Calendar.DATE, 14);
        borrowInfo.setReturnTime(instance.getTime());
        //初始为-1表示, 用户已经预约，正在等待管理员审核
        borrowInfo.setIsReturn(-1);
        borrowInfoService.save(borrowInfo);
        return "success";
    }


}
