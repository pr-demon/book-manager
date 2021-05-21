package com.qlu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qlu.bean.Book;
import com.qlu.bean.vo.BookAndUserVo;

import java.util.List;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @since 2020-03-17
 */
public interface IBookService extends IService<Book> {

    /*
    *   获取所有图书信息
    * */
    public Page<BookAndUserVo> getAllBookInfo(Page<BookAndUserVo> page, Integer uid, Integer borrowCheckTime, String query);
}
