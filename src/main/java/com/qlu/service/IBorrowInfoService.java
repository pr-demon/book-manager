package com.qlu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qlu.bean.BorrowInfo;
import com.qlu.bean.vo.BorrowInfoAndBookAndUserVo;
import com.qlu.bean.vo.BorrowInfoAndBookVo;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @since 2020-03-17
 */
public interface IBorrowInfoService extends IService<BorrowInfo> {
    Page<BorrowInfoAndBookVo> getBorrowInfoAndBook(Page<BorrowInfoAndBookVo> page, Integer uid, Integer isReturn);

    Page<BorrowInfoAndBookAndUserVo> getBorrowInfoAndBookAndUser(Page<BorrowInfoAndBookAndUserVo> page, Integer isReturn, Integer isBorrow);


    /*                     图书管理员查看预约，借出，归还图书信息的接口    */
    /*
     *   获取所有正在预约中的信息
     * */
    Page<BorrowInfoAndBookAndUserVo> getBorrowCheckBookInfo(Page<BorrowInfoAndBookAndUserVo> page, Integer borrowCheckTime);

    /*
     *   获取所有已经借出的书籍信息
     * */
    Page<BorrowInfoAndBookAndUserVo> getBorrowOutBookInfo(Page<BorrowInfoAndBookAndUserVo> page);

    /*
     *   获取所有已经归还的书籍信息
     * */
    Page<BorrowInfoAndBookAndUserVo> getReturnedBookInfo(Page<BorrowInfoAndBookAndUserVo> page);

    /*                    用户查看借阅历史，借阅/预约信息接口                */
    /*
     *   获取一个用户的借阅历史
     * */
    Page<BorrowInfoAndBookAndUserVo> getReturnedHistory(Page<BorrowInfoAndBookAndUserVo> page, Integer uid);


    /*
     *   获取一个用户 正在借阅， 正在预约的信息
     * */
    Page<BorrowInfoAndBookAndUserVo> getBorrowCheckAndOut(Page<BorrowInfoAndBookAndUserVo> page, Integer uid, Integer borrowCheckTime);

}
