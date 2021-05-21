package com.qlu.bean.vo;

import com.qlu.bean.Book;
import lombok.Data;

@Data
public class BookAndUserVo extends Book {

    /*
    *   预约数量 + 已借出数量
    * */
    private Integer borrowCount;

    /*
    *   当前登陆用户是否已经 预约本书 or 借阅本书
    * */
    private Integer userBorrow;
}
