package com.qlu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlu.bean.Book;
import com.qlu.bean.vo.BookAndUserVo;
import com.qlu.mapper.BookMapper;
import com.qlu.service.IBookService;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @since 2020-03-17
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    /*
     *   获取所有图书信息
     * */
    public Page<BookAndUserVo> getAllBookInfo(Page<BookAndUserVo> page, Integer uid, Integer borrowCheckTime, String query){
        return page.setRecords(this.baseMapper.getAllBookInfo(page, uid, borrowCheckTime, query));
    }
}
