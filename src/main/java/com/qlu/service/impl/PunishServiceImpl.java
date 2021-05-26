package com.qlu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qlu.bean.Punish;
import com.qlu.mapper.PunishMapper;
import com.qlu.service.IPunishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PunishServiceImpl extends ServiceImpl<PunishMapper, Punish> implements IPunishService {

    @Autowired
    private IPunishService punishService;

    @Override
    public Punish getPunish(int uid) {
        return punishService.getOne(new QueryWrapper<Punish>().eq("uid", uid).last("for update"));
    }

    @Override
    public void addPunish(int uid) {
        Punish punish = getPunish(uid);
        Date data = new Date();
        Integer after = Punish.PUNISH_TIME;
        if (punish == null){
            punish = new Punish();
            punish.setUid(uid);
            punish.setReleaseTime(getAfterTime(data, after));
            punishService.save(punish);
        }else if (punish.getReleaseTime().before(data)){
            punish.setReleaseTime(getAfterTime(data, after));
            punishService.updateById(punish);
        }else{
            punish.setReleaseTime(getAfterTime(punish.getReleaseTime(), after));
            punishService.updateById(punish);
        }

    }

    private Date getAfterTime(Date date, Integer after){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, after);
        return calendar.getTime();
    }
}
