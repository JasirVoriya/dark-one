<!-- markdownlint-disable MD033 MD041 -->

<p align="center">
  <img src="https://gitee.com/wu_xueming/drawing-bed/raw/master/image/202305111212511.png" width="200" height="200" alt="DarkOne">
</p>


<div align="center">

# DarkOne

_✨ 轻量级Java服务器框架 ✨_

</div>
<p align="center">
  <a href="https://www.apache.org/licenses/LICENSE-2.0.html">
    <img src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg" alt="license">
  </a>
  <img src="https://img.shields.io/badge/Java-11+-blue" alt="Java">
  <img src="https://github.com/nonebot/nonebot2/actions/workflows/website-deploy.yml/badge.svg?branch=master&event=push" alt="site"/>
  <br />
  <img src="https://img.shields.io/badge/DarkOne-v1.0-black?style=social&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAAIVBMVEUAAAAAAAADAwMHBwceHh4UFBQNDQ0ZGRkoKCgvLy8iIiLWSdWYAAAAAXRSTlMAQObYZgAAAQVJREFUSMftlM0RgjAQhV+0ATYK6i1Xb+iMd0qgBEqgBEuwBOxU2QDKsjvojQPvkJ/ZL5sXkgWrFirK4MibYUdE3OR2nEpuKz1/q8CdNxNQgthZCXYVLjyoDQftaKuniHHWRnPh2GCUetR2/9HsMAXyUT4/3UHwtQT2AggSCGKeSAsFnxBIOuAggdh3AKTL7pDuCyABcMb0aQP7aM4AnAbc/wHwA5D2wDHTTe56gIIOUA/4YYV2e1sg713PXdZJAuncdZMAGkAukU9OAn40O849+0ornPwT93rphWF0mgAbauUrEOthlX8Zu7P5A6kZyKCJy75hhw1Mgr9RAUvX7A3csGqZegEdniCx30c3agAAAABJRU5ErkJggg==" alt="onebot">
  <a href="https://docs.github.com/en/developers/apps">
    <img src="https://img.shields.io/badge/GitHub-DarkOne-181717?style=social&logo=github" alt="github"/>
  </a>
  <br />
  <a href="https://jq.qq.com/?_wv=1027&k=5OFifDh">
    <img src="https://img.shields.io/badge/QQ-1838566218-orange?style=flat-square" alt="QQ Chat Group">
  </a>
</p>


-------

## 什么是DarkOne

_✨DarkOne，一个应用，一套解决！✨_  
 一套开源轻量级的服务器解决方案，包含orm框架、TCP服务器、应用层服务器以及安全框架等，目前可以运行一个博客网站，欢迎Coder们一起来研究，做一个自己的框架，一起交流技术！

![image-20230511121903149](https://gitee.com/wu_xueming/drawing-bed/raw/master/image/202305111219223.png)

## 特色

- 轻量级：专注服务器开发，架构更轻巧，让你的项目更小
- 易于开发：学习成本低，基于Spring、Netty的使用习惯开发，代码编写上手简单
- 海纳百川：一个框架，可自定义通信协议，可支持各种应用

## 镜像站
如果github卡顿，可以访问下面的Gitee国内镜像站  
<a href="https://github.com/dobilibili/dark-one" target="_blank">Github 传送门</a>  
<a href="https://gitee.com/wu_xueming/dark-one" target="_blank">Gitee 传送门</a>

## 模块介绍
### byte-bye
_✨ ByteBye，发送你的数据，和你的数据君一路走好！ ✨_  
一个想写成类似于Netty一样的轻量级网络服务器，基于JavaNIO，使用React主从模式开发，可以自定义应用层协议解析器，目前已经编写了一个Http的协议解析器。  
 目前能够基于http搭建web应用，但是还存在一些bug和性能问题。

 ### byte-hi
 _✨ByteHi，哈喽哈喽，数据君你来啦，来了就别走了！✨_  
 一个轻量级的半自动化ORM框架，简单易用，基于连接池封装开发。

 ### web-mvc
 _✨高效，简洁易上手的webmvc框架！ ✨_  
 基于SpringMVC的使用习惯开发的MVC框架，如果你熟悉Spring搭建web应用，那么你基本上可以零成本上手该框架。

下面是一个例子：

```java
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
```

 ### web-server-demo
 使用本项目框架所搭建的一个web网站，是我大一写过的一个博客网站，现在移植到这个项目里了，目前只有静态网页。

![image-20230511121737136](https://gitee.com/wu_xueming/drawing-bed/raw/master/image/202305111217298.png)
