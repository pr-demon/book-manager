package com.qlu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qlu.bean.Punish;

public interface IPunishService extends IService<Punish> {

    public Punish getPunish(int uid);

    public void addPunish(int uid);
}
