package com.qlu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qlu.bean.BorrowInfo;
import com.qlu.bean.Discuss;
import com.qlu.service.IBorrowInfoService;
import com.qlu.service.IDiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/discuss")
public class FrontDiscussController {

    @Autowired
    private IDiscussService discussService;
    @Autowired
    private IBorrowInfoService borrowInfoService;

    @PostMapping("/add")
    @ResponseBody
    public String add(Discuss discuss) {
        discuss.setCreateTime(new Date());
        System.out.println(discuss);
        discussService.save(discuss);
        return "success";
    }

    @GetMapping("/get")
    @ResponseBody
    public List<Discuss> get(String bid) {
        List<Discuss> discussesList = discussService.list(new QueryWrapper<Discuss>().eq("bid", bid).orderByDesc("create_time"));
        return discussesList;
    }

    /*
     *   用户是否有权限评论某本图书
     * */
    @RequestMapping("whoCanDiscuss")
    @ResponseBody
    public String whoCanDiscuss(String uid, String bid) {
        List<BorrowInfo> list = borrowInfoService.list(
                new QueryWrapper<BorrowInfo>().
                        eq("uid", uid).
                        eq("bid", bid).
                        in("is_return", 0, 1));

        int size = list.size();
        if (size > 0) {
            return "success";
        } else {
            return "fail";
        }
    }
}
