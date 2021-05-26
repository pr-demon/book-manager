package com.qlu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Punish {
    public static final Integer PUNISH_TIME = 7;

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*
    *   用户ID
    **/
    private Integer uid;

    /*
    *   release time
    * */
    private Date releaseTime;
}

