package cn.darkone.controller;

import cn.darkone.entity.vo.ResultMessage;
import cn.darkone.utils.ResultUtil;
import cn.darkone.entity.dao.Member;
import cn.darkone.entity.enums.Sex;
import cn.darkone.framework.common.security.annotations.AccessLimit;
import cn.darkone.framework.common.web.annotations.GetMapping;
import cn.darkone.framework.common.web.annotations.PostMapping;
import cn.darkone.framework.common.web.annotations.RequestMapping;
import cn.darkone.framework.common.web.annotations.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/test")
public class TestController {

    @AccessLimit(seconds = 60, maxCount = 10, needLogin = false)
    @GetMapping({"/login", "/register"})
    @PostMapping({"/login", "/register"})
    public ResultMessage<Member> login(String account, String password, Integer code) {
        return ResultUtil.data(new Member(account, password, code, null, null, null));
    }

    @AccessLimit(seconds = 60, maxCount = 10, needLogin = false)
    @GetMapping({"/member"})
    @PostMapping({"/member"})
    public ResultMessage<Member> member(Member member) {
        return ResultUtil.data(member);
    }

    @GetMapping({"/array1"})
    public ResultMessage<Object> array1(int[] nums, Integer[] counts) {
        System.out.println(Arrays.toString(nums));
        System.out.println(Arrays.toString(counts));
        return ResultUtil.success();
    }

    @GetMapping({"/array2"})
    public ResultMessage<Object> array2(int[] nums, Long[] counts) {
        System.out.println(Arrays.toString(nums));
        System.out.println(Arrays.toString(counts));
        return ResultUtil.success();
    }

    @GetMapping({"/array3"})
    public ResultMessage<Object> array3(boolean[] nums, Boolean[] counts) {
        System.out.println(Arrays.toString(nums));
        System.out.println(Arrays.toString(counts));
        return ResultUtil.success();
    }

    @GetMapping({"/enums"})
    public ResultMessage<Sex> enums(Sex sex) {
        return ResultUtil.data(sex);
    }
}
