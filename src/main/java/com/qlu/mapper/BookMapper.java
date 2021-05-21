package com.qlu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qlu.bean.Book;
import com.qlu.bean.vo.BookAndUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper extends BaseMapper<Book> {

    /*
     *   获取所有图书信息
     * */
    @Select("SELECT book.*,  count(borrow_info.id) as borrow_count, sum(if(borrow_info.uid = #{uid}, borrow_info.is_return, NULL)) as user_borrow " +
            " FROM book left join borrow_info on(book.id = borrow_info.bid and (borrow_info.is_return = 0 or (borrow_info.is_return = -1 and datediff(now(), borrow_time) <= #{borrowCheckTime}))) " +
            "where (book.book_name like '%${query}%' or book.type like '%${query}%' or book.author like '%${query}%') group by book.id order by book.id desc")
    public List<BookAndUserVo> getAllBookInfo(Page<BookAndUserVo> page,
                                              @Param("uid") Integer uid,
                                              @Param("borrowCheckTime") Integer borrowCheckTime,
                                              @Param("query") String query);

}