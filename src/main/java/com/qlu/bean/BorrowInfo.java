package com.qlu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 借阅信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowInfo {
    public static int BORROW_CHECK_TIME = 1;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 书id
     */
    private Integer bid;

    /**
     * 借阅时间
     */
    private Date borrowTime;

    /**
     * 归还时间
     * isReturn == 0  应该归还的时间
     * isReturn == 1  归还时间
     */
    private Date returnTime;

    /**
     * 书籍归还信息
     * -1：  默认值，用户已经预约，正在等待管理员审核
     * 0：  用户还未还书
     * 1：  用户已经还书
     */
    private Integer isReturn;


    /*
    *   该字段只用于违规处理，记录图书应该归还的时间
    *   只有当isReturn=1, 用户已经还书时，该字段起作用
    * */
    private Date shouldReturnTime;

}
