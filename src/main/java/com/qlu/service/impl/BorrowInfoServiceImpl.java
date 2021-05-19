package com.qlu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlu.bean.BorrowInfo;
import com.qlu.bean.vo.BorrowInfoAndBookAndUserVo;
import com.qlu.bean.vo.BorrowInfoAndBookVo;
import com.qlu.mapper.BorrowInfoMapper;
import com.qlu.service.IBorrowInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @since 2020-03-17
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements IBorrowInfoService {

    @Override
    public Page<BorrowInfoAndBookVo> getBorrowInfoAndBook(Page<BorrowInfoAndBookVo> page, Integer uid, Integer isReturn) {
        return page.setRecords(this.baseMapper.getBorrowInfoAndBook(page, uid, isReturn));
    }

    @Override
    public Page<BorrowInfoAndBookAndUserVo> getBorrowInfoAndBookAndUser(Page<BorrowInfoAndBookAndUserVo> page, Integer isReturn, Integer isBorrow) {
        return page.setRecords(this.baseMapper.getBorrowInfoAndBookAndUser(page, isReturn, isBorrow));
    }

    /*             admin               */
    /*
     *   获取所有正在预约中的信息
     * */
    @Override
    public Page<BorrowInfoAndBookAndUserVo> getBorrowCheckBookInfo(Page<BorrowInfoAndBookAndUserVo> page, Integer borrowCheckTime) {
        return page.setRecords(this.baseMapper.getBorrowCheckBookInfo(page, borrowCheckTime));
    }


    /*
     *   获取所有已经借出的书籍信息
     * */
    @Override
    public Page<BorrowInfoAndBookAndUserVo> getBorrowOutBookInfo(Page<BorrowInfoAndBookAndUserVo> page) {
        return page.setRecords(this.baseMapper.getBorrowOutBookInfo(page));
    }

    /*
     *   获取所有已经归还的书籍信息
     * */
    @Override
    public Page<BorrowInfoAndBookAndUserVo> getReturnedBookInfo(Page<BorrowInfoAndBookAndUserVo> page) {
        return page.setRecords(this.baseMapper.getReturnedBookInfo(page));
    }

    /*              user             */
    /*
     *   获取一个用户的借阅历史
     * */
    public Page<BorrowInfoAndBookAndUserVo> getReturnedHistory(Page<BorrowInfoAndBookAndUserVo> page, Integer uid) {
        return page.setRecords(this.baseMapper.getReturnedHistory(page, uid));
    }

    /*
     *   获取一个用户 正在借阅， 正在预约的信息
     * */
    @Override
    public Page<BorrowInfoAndBookAndUserVo> getBorrowCheckAndOut(Page<BorrowInfoAndBookAndUserVo> page, Integer uid, Integer borrowCheckTime) {
        return page.setRecords(this.baseMapper.getBorrowCheckAndOut(page, uid, borrowCheckTime));
    }
}
