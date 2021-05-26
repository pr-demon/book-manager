package com.qlu;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qlu.bean.Book;
import com.qlu.bean.BorrowInfo;
import com.qlu.bean.vo.BookAndUserVo;
import com.qlu.bean.vo.BorrowInfoAndBookAndUserVo;
import com.qlu.service.IBookService;
import com.qlu.service.IBorrowInfoService;
import com.qlu.service.IPunishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMyPage {

    @Autowired
    private IBorrowInfoService borrowInfoService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private IPunishService punishService;

    @Test
    public void test1() {
        Page<BorrowInfoAndBookAndUserVo> objectPage = new Page<>(1, 2);
        //0 , 1 预约,
        //1 , 0 归还,
        //0 , 2 正被借的
        Page<BorrowInfoAndBookAndUserVo> borrowInfoAndBookAndUser = borrowInfoService.getBorrowInfoAndBookAndUser(objectPage, 0, 1);
        System.out.println(borrowInfoAndBookAndUser);
    }

    @Test
    public void testQuery() {
        Page<BorrowInfoAndBookAndUserVo> objectPage = new Page<>(1, 2);
        //0 , 1 预约,
        //1 , 0 归还,
        //0 , 2 正被借的
        Page<BorrowInfoAndBookAndUserVo> borrowInfoAndBookAndUser = borrowInfoService.getBorrowInfoAndBookAndUser(objectPage, 0, 1);
        System.out.println(borrowInfoAndBookAndUser);
    }

    @Test
    public void testBookInfo(){
        Page<BookAndUserVo> books =  bookService.getAllBookInfo(
                new Page<>(1, 100)
                ,4
                , BorrowInfo.BORROW_CHECK_TIME
                ,"pyt"
        );
        for (BookAndUserVo record : books.getRecords()) {
            System.out.println(record.getId() + " " + record.getBookName() + " " + record.getType() + " " + record.toString());
        }
    }

    @Test
    public void testPunish(){
        punishService.list();
    }
}
