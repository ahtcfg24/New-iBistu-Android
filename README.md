# NewBistu
New iBistu of DreamFactory API.

# 注意：本仓库是一个Android Studio的Project下面的一个module，所以你导入的你的Project中的时候应该选择`New`->`import Module`->本仓库

## 后端接口使用DreamFactory生成，数据接口全部存放在`\src\main\java\org\iflab\newbistu\apis`下面

---
title: 2016-8-2 iBistu接口文档
tags: iBistu
---
## 前言
1. 本接口使用DreamFactory生成，返回的json结构固定为：
```json
{
"resource": [
  {对象1}，
  {对象2}，
  ...
  {对象n}
  ]
}
```

2. 访问本接口需要添加请求头，请求头内容包括：apikey，token，目前由于用户模块未完善，可仅提供apikey进行开发。目前可用的apikey请求头及其格式为：
`"X-DreamFactory-Api-Key", "3528bd808dde403b83b456e986ce1632d513f7a06c19f5a582058be87be0d8c2"`

## 用户
等待接入cas以及短信验证平台，暂无。

## 关于

 1. 获取iBistu相关的介绍数据
   - 接口：`http://45.32.11.169/api/v2/ibistu/_table/module_about`
   - 请求方法：get
   - 参数：无
   - 返回值：
  ```json
    {
  "resource": [
    {
      "id": 1,
      "aboutName": "学校简介",
      "aboutDetails": "<p><img src=\"http://www.bistu.edu.cn/xxgk/xyfc/201305/W020130510690617275637.jpg\" width=\"100%\">\n<p>　　北京信息科技大学是由原北京机械工业学院和原北京信息工程学院合并组建，以工管为主体、工管理经文法多学科协调发展，以培养高素质应用型人才为主、北京市重点支持建设的全日制普通高等学校。 \n</p>\n<p>　　学校秉承“勤以为学，信以立身”的校训，发扬“抢抓机遇、迎难而上、争先创优、挑战自我”的新大学精神，坚持办学指导思想，明确发展目标和办学定位，传承办学优势与特色，紧紧依靠广大师生员工，抓建设、促改革、谋发展，内涵建设与外延发展等各项事业取得显著成效。 \n</p>\n<p>　　面向未来，学校正以坚定步伐朝着“在电子信息、现代制造与光机电一体化、知识管理与技术经济等领域的优势与特色更加突出，综合办学实力稳居北京市属高校前列，并早日达到国内同类高校的一流水平”的奋斗目标迈进。 \n</p>"
    },
    {
      "id": 2,
      "aboutName": "历史沿革 ",
      "aboutDetails": "<p><img src=\"http://www.bistu.edu.cn/xxgk/lsyg/201305/W020150929666961409433.jpg\" width=\"100%\">\n<p>　　原北京机械工业学院的前身是1986年陕西机械学院北京研究生部和北京机械工业管理专科学校合并成立的北京机械工业管理学院，其办学历史可追溯到20世纪30年代。 \n<p>　　原北京信息工程学院的前身是1978年第四机械工业部1915所举办北京大学第二分校。 \n<p>　　2003年8月21日，北京市委、市政府决定组建北京信息科技大学。\n<p>　　2004年5月18日，教育部批准筹建北京信息科技大学。 \n<p>　　2008年3月26日，教育部批准正式设立北京信息科技大学。"
    },
    {
      "id": 3,
      "aboutName": "学科专业",
      "aboutDetails": "<p>\n学校坚持优化体系、整合资源、凝练方向、寻求突破，以经济社会发展需要和学科发展前沿为导向，着眼于首都和行业需求，立足学校定位与发展目标，不断加强学科建设，初步形成了可持续发展的学科体系。学校坚持发挥优势、打造特色、科学规划、加强建设，主动适应社会需求，优化学科结构布局，努力建设重点学科、科研基地和特色专业。\n<p>北京市重点学科 3个 \n<p>北京市重点建设学科 9个 \n<p>部级重点学科 2个 \n<p>教育部重点实验室 1个 \n<p>北京市重点实验室 3个 \n<p>北京市哲学社会科学研究基地 1个 \n<p>北京高校工程技术研究中心 1个 \n<p>机械工业重点实验室 2个 \n<p>硕士学位授权一级学科 14个 \n<p>硕士学位授权二级学科 22个 \n<p>专业学位门类（工程硕士、MBA） 2个 \n<p>工程硕士专业学位授权领域 8个 \n<p>本科专业 31个 \n<p>国家级特色专业建设点 4个 \n<p>北京市级特色专业建设点 9个 \n<p>在京第一批招生专业 10个 "
    },
    {
      "id": 4,
      "aboutName": "Credits",
      "aboutDetails": "<p>iBistu是北京信息科技大学校园移动应用，由我校信息网络中心管理的技术社团\"网络实践创新联盟ifLab.org\"独立设计研发。\n\n<p>项目开始于2012年3月，2012年9月推出测试版WebApp，2012年底推出基于PhoneGap的应用。目前所有三个版本已经开发完成上线。\n\n<p class=\"lead\">2013年8月25日，iBistu获得“全国移动互联网应用大赛”三等奖。\n\n<p>iBistu是iCampus的一个独立版本。iCampus的目标是实现一个开源的校园移动应用框架，免费提供给国内其他高校使用。\n\n<p>如果你想参与我们，欢迎Email到hr@iflab.org或者找到新浪微博 ifLab。\n\n<h3>iBistu2.0</h3>\n<p>Project Leader / 曾铮\n<p>Advisior / 龚汉明\n<p>iOS / 马奎 刘鸿喆 高铭 李智伟 马超\n<p>Android / 黄陈 王鹏 李占宇\n<p>WebApp / <img src=\"http://www.karlur.com/zz1.png\">\n<p>Database / <img src=\"http://www.karlur.com/zz1.png\"> 郑小博\n<p>Backend / 黄伟 \n<p>API / 顾翔\n<p>Design / 马奎 肖晨曦\n<p>System / 朱劲寿\n<p>Special Thanks / 孙彦超 郝保水 侯琴 王惠 白茜薇\n\n<h3>iBistu1.0</h3>\n<p>Project Leader / <img src=\"http://www.karlur.com/zz1.png\">\n<p>Advisior / 龚汉明\n<p>Database / 郑小博 <img src=\"http://www.karlur.com/zz1.png\">\n<p>Android / 李占宇 刘相宇\n<p>PhoneGap / 熊伦\n<p>WebApp / <img src=\"http://www.karlur.com/zz1.png\">\n<p>Backend System / 顾翔\n<p>API / 顾翔\n<p>Design / 肖晨曦 "
    },
    {
      "id": 47,
      "aboutName": "ifLab",
      "aboutDetails": "<p><img src=\"http://iflab.org/wp-content/uploads/2013/10/IMG_8766.jpg\" width=\"100%\">\n\n<p>ifLab全称北京信息科技大学网络实践创新联盟，简称“创联”，是由学校信息网络中心组织和管理的技术社团。\n\n<p>i代表internet（互联网），innovation（创新）；f代表future（未来），fulfill（实践）。\n\n<p>我们的使命是学习和研究前沿互联网技术，建设和开发有助于学校教育信息化的项目；通过这些项目提高成员的IT水平，培养一批有专业造诣和项目实践经验的人才。\n\n<p>我们的目标是成为校内最大最强的技术社团；在信息网络中心指导下协助建设学校信息化创新项目；帮助校内社团、组织及师生提高前沿IT技能；每年培养10个以上相当于1年专业工作经验的IT人才。\n\n<p>我们欢迎校内的社团组织、学术机构与我们联盟合作。我们可以提供必要的技术协助，或者帮其他社团培养需要的技术人才。\n\n<p>目前我们主要学习研究的领域为移动互联网以及云计算。\n\n<p>你可以通过访问http://www.iflab.org或者关注新浪微博ifLab来了解我们。"
    }
  ]
}
  ```

## 班车
 
 1. 获取班车数据
   - 接口：`http://45.32.11.169/api/v2/ibistu/_table/module_bus`
   - 请求方法：get
   - 参数：无
   - 返回值：
 ```json
 {
  "resource": [
    {
      "id": 4,
      "busName": "东线班车",
      "busType": "通勤班车",
      "departureTime": "06:50:00",
      "returnTime": "17:10:00",
      "busLine": "[{"station":"金台路校区","arrivalTime":"06:50"},{"station":"北二环钟楼北桥","arrivalTime":"07:15"},{"station":"德外关厢","arrivalTime":"07:20"},{"station":"小营校区","arrivalTime":"07:40"},{"station":"小营校区","arrivalTime":"17:10"},{"station":"德外关厢","arrivalTime":"17:25"},{"station":"北二环钟楼北桥","arrivalTime":"17:35"},{"station":"金台路校区","arrivalTime":"19:00"}]",
      "busIntro": "东线班车"
    },
    {
      "id": 5,
      "busName": "西线班车",
      "busType": "通勤班车",
      "departureTime": "06:55:00",
      "returnTime": "17:10:00",
      "busLine": "[{"station":"公主坟","arrivalTime":"06:55"},{"station":"当代商城","arrivalTime":"07:05"},{"station":"蓝旗营清华西南门","arrivalTime":"07:15"},{"station":"小营校区","arrivalTime":"07:45"},{"station":"小营校区","arrivalTime":"17:10"},{"station":"蓝旗营清华西南门","arrivalTime":"17:35"},{"station":"当代商城","arrivalTime":"17:50"},{"station":"公主坟","arrivalTime":"18:20"}]",
      "busIntro": "西线班车"
    },
    {
      "id": 6,
      "busName": "望京-小营",
      "busType": "通勤班车",
      "departureTime": "07:05:00",
      "returnTime": "17:10:00",
      "busLine": "[{"station":"北四环联大","arrivalTime":"07:05"},{"station":"望兴园","arrivalTime":"07:15"},{"station":"望京花园","arrivalTime":"07:20"},{"station":"小营校区","arrivalTime":"07:50"},{"station":"小营校区","arrivalTime":"17:10"},{"station":"望京花园","arrivalTime":"17:30"},{"station":"望兴园","arrivalTime":"17:40"},{"station":"北四环联大","arrivalTime":"17:55"}]",
      "busIntro": "望京-小营"
    },
    {
      "id": 7,
      "busName": "回龙观-育新-小营",
      "busType": "通勤班车",
      "departureTime": "07:00:00",
      "returnTime": "17:10:00",
      "busLine": "[{"station":"流星花园","arrivalTime":"07:00"},{"station":"北店嘉园","arrivalTime":"07:03"},{"station":"矩阵小区","arrivalTime":"07:06"},{"station":"龙跃苑","arrivalTime":"07:10"},{"station":"育新小区","arrivalTime":"07:26"},{"station":"小营校区","arrivalTime":"07:36"},{"station":"小营校区","arrivalTime":"17:10"},{"station":"育新小区","arrivalTime":"17:18"},{"station":"龙跃苑","arrivalTime":"17:35"},{"station":"矩阵小区","arrivalTime":"17:40"},{"station":"北店嘉园","arrivalTime":"17:45"},{"station":"流星花园","arrivalTime":"17:48"}]",
      "busIntro": "回龙观-育新-小营"
    },
    {
      "id": 8,
      "busName": "健翔桥-小营",
      "busType": "通勤班车",
      "departureTime": "15:20:00",
      "returnTime": "15:35:00",
      "busLine": "[{"station":"健翔桥校区","arrivalTime":"15:20"},{"station":"小营校区","arrivalTime":"15:35"},{"station":"小营校区","arrivalTime":"15:35"},{"station":"健翔桥校区","arrivalTime":"16:05"}]",
      "busIntro": "健翔桥-小营"
    },
    {
      "id": 9,
      "busName": "小营-健翔桥",
      "busType": "通勤班车",
      "departureTime": "06:30:00",
      "returnTime": "07:15:00",
      "busLine": "[{"station":"小营校区","arrivalTime":"6:30"},{"station":"健翔桥校区","arrivalTime":"6:55"},{"station":"健翔桥校区","arrivalTime":"7:15"},{"station":"小营校区","arrivalTime":"7:50"}]",
      "busIntro": "小营-健翔桥"
    },
    {
      "id": 10,
      "busName": "回龙观-育新-健翔桥",
      "busType": "通勤班车",
      "departureTime": "07:05:00",
      "returnTime": "17:10:00",
      "busLine": "[{"station":"育新小区","arrivalTime":"07:05"},{"station":"健翔桥校区","arrivalTime":"07:42"},{"station":"健翔桥校区","arrivalTime":"17:10"},{"station":"育新小区","arrivalTime":"17:32"},{"station":"龙跃苑","arrivalTime":"17:50"}]",
      "busIntro": "回龙观-育新-健翔桥"
    },
    {
      "id": 11,
      "busName": "望京-健翔桥",
      "busType": "通勤班车",
      "departureTime": "07:15:00",
      "returnTime": "17:10:00",
      "busLine": "[{"station":"望兴园","arrivalTime":"07:15"},{"station":"望京花园","arrivalTime":"07:17"},{"station":"健翔桥校区","arrivalTime":"07:45"},{"station":"健翔桥校区","arrivalTime":"17:10"},{"station":"望京花园","arrivalTime":"17:45"},{"station":"望兴园","arrivalTime":"17:47"}]",
      "busIntro": "望京-健翔桥"
    },
    {
      "id": 12,
      "busName": "小营-清河校区南门-健翔桥",
      "busType": "教学班车",
      "departureTime": "09:50:00",
      "returnTime": null,
      "busLine": "[{"station":"小营校区","arrivalTime":"09:50"},{"station":"清河小区南门","arrivalTime":"09:51"},{"station":"健翔桥校区","arrivalTime":"10:35"}]",
      "busIntro": "小营-清河小区南门-健翔桥"
    },
    {
      "id": 14,
      "busName": "小营-清河校区南门-健翔桥",
      "busType": "教学班车",
      "departureTime": "15:20:00",
      "returnTime": null,
      "busLine": "[{"station":"小营校区","arrivalTime":"15:20"},{"station":"清河校区南门","arrivalTime":"15:21"},{"station":"健翔桥校区","arrivalTime":"15:40"}]",
      "busIntro": "小营-清河校区南门-健翔桥"
    },
    {
      "id": 15,
      "busName": "健翔桥-清河校区南门-小营",
      "busType": "教学班车",
      "departureTime": "12:30:00",
      "returnTime": null,
      "busLine": "[{"station":"健翔桥校区","arrivalTime":"12:30"},{"station":"清河校区南门","arrivalTime":"12:46"},{"station":"小营","arrivalTime":"12:48"}]",
      "busIntro": "健翔桥-清河校区南门-小营"
    },
    {
      "id": 16,
      "busName": "小营-清河校区南门-健翔桥",
      "busType": "教学班车",
      "departureTime": "12:30:00",
      "returnTime": null,
      "busLine": "[{"station":"小营校区","arrivalTime":"12:30"},{"station":"清河校区南门","arrivalTime":"12:33"},{"station":"健翔桥校区","arrivalTime":"12:55"}]",
      "busIntro": "小营-清河校区南门-健翔桥"
    },
    {
      "id": 17,
      "busName": "健翔桥-小营",
      "busType": "教学班车",
      "departureTime": "09:50:00",
      "returnTime": null,
      "busLine": "[{"station":"健翔桥校区","arrivalTime":"09:50"},{"station":"小营校区","arrivalTime":"10:10"}]",
      "busIntro": "健翔桥-小营"
    }
  ]
}
 ```
## 黄页

 1. 获取黄页部门列表数据
   - 接口：`http://45.32.11.169/api/v2/ibistu/_table/module_department_list`
   - 请求方法：get
   - 参数：无
   - 返回值：
```json
  {
  "resource": [
    {
      "id": 94,
      "name": "研究生部（党委研究生工作部）",
      "telephone": "1",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 102,
      "name": "人事处",
      "telephone": "1",
      "department": 11,
      "isDisplay": true
    },
    {
      "id": 110,
      "name": "财务处",
      "telephone": "1",
      "department": 12,
      "isDisplay": true
    },
    {
      "id": 126,
      "name": "学生工作处（党委学生工作部、武装部）",
      "telephone": "1",
      "department": 13,
      "isDisplay": true
    },
    {
      "id": 137,
      "name": "招生就业办公室",
      "telephone": "1",
      "department": 14,
      "isDisplay": true
    },
    {
      "id": 145,
      "name": "国际交流合作处",
      "telephone": "1",
      "department": 15,
      "isDisplay": true
    },
    {
      "id": 155,
      "name": "资产管理处",
      "telephone": "1",
      "department": 16,
      "isDisplay": true
    },
    {
      "id": 160,
      "name": "审计处",
      "telephone": "1",
      "department": 17,
      "isDisplay": true
    },
    {
      "id": 163,
      "name": "后勤管理处(后勤服务集团)",
      "telephone": "1",
      "department": 18,
      "isDisplay": true
    },
    {
      "id": 251,
      "name": "基建处（新校区建设办公室）",
      "telephone": "1",
      "department": 19,
      "isDisplay": true
    },
    {
      "id": 258,
      "name": "保卫处（党委保卫部）",
      "telephone": "1",
      "department": 20,
      "isDisplay": true
    },
    {
      "id": 274,
      "name": "健翔桥校区",
      "telephone": "1",
      "department": 21,
      "isDisplay": true
    },
    {
      "id": 285,
      "name": "清河校区",
      "telephone": "1",
      "department": 22,
      "isDisplay": true
    },
    {
      "id": 295,
      "name": "离退休工作办公室（党委老干部工作部）",
      "telephone": "1",
      "department": 23,
      "isDisplay": true
    },
    {
      "id": 301,
      "name": "校工会",
      "telephone": "1",
      "department": 24,
      "isDisplay": true
    },
    {
      "id": 306,
      "name": "校团委",
      "telephone": "1",
      "department": 25,
      "isDisplay": true
    },
    {
      "id": 310,
      "name": "校友工作办公室",
      "telephone": "1",
      "department": 26,
      "isDisplay": true
    },
    {
      "id": 312,
      "name": "机关党委",
      "telephone": "1",
      "department": 27,
      "isDisplay": true
    },
    {
      "id": 314,
      "name": "校区联合党总支",
      "telephone": "1",
      "department": 28,
      "isDisplay": true
    },
    {
      "id": 316,
      "name": "教辅单位党总支",
      "telephone": "1",
      "department": 29,
      "isDisplay": true
    },
    {
      "id": 16,
      "name": "学校办公室（党委办公室）",
      "telephone": "1",
      "department": 3,
      "isDisplay": true
    },
    {
      "id": 318,
      "name": "机电工程学院",
      "telephone": "1",
      "department": 30,
      "isDisplay": true
    },
    {
      "id": 340,
      "name": "光电信息与通信工程学院",
      "telephone": "1",
      "department": 31,
      "isDisplay": true
    },
    {
      "id": 355,
      "name": "自动化学院",
      "telephone": "1",
      "department": 32,
      "isDisplay": true
    },
    {
      "id": 372,
      "name": "计算机学院",
      "telephone": "1",
      "department": 33,
      "isDisplay": true
    },
    {
      "id": 392,
      "name": "经济管理学院",
      "telephone": "1",
      "department": 34,
      "isDisplay": true
    },
    {
      "id": 413,
      "name": "信息管理学院",
      "telephone": "1",
      "department": 35,
      "isDisplay": true
    },
    {
      "id": 434,
      "name": "人文社科学院",
      "telephone": "1",
      "department": 36,
      "isDisplay": true
    },
    {
      "id": 446,
      "name": "外国语学院",
      "telephone": "1",
      "department": 37,
      "isDisplay": true
    },
    {
      "id": 456,
      "name": "理学院",
      "telephone": "1",
      "department": 38,
      "isDisplay": true
    },
    {
      "id": 479,
      "name": "体育部",
      "telephone": "1",
      "department": 39,
      "isDisplay": true
    },
    {
      "id": 31,
      "name": "党委组织部",
      "telephone": "1",
      "department": 4,
      "isDisplay": true
    },
    {
      "id": 487,
      "name": "计算中心",
      "telephone": "1",
      "department": 40,
      "isDisplay": true
    },
    {
      "id": 498,
      "name": "机电实习中心",
      "telephone": "1",
      "department": 41,
      "isDisplay": true
    },
    {
      "id": 503,
      "name": "健翔桥校区管理办公室",
      "telephone": "1",
      "department": 42,
      "isDisplay": true
    },
    {
      "id": 517,
      "name": "清河校区管理办公室",
      "telephone": "1",
      "department": 43,
      "isDisplay": true
    },
    {
      "id": 525,
      "name": "金台路校区管理办公室",
      "telephone": "1",
      "department": 44,
      "isDisplay": true
    },
    {
      "id": 534,
      "name": "酒仙桥校区管理办公室",
      "telephone": "1",
      "department": 45,
      "isDisplay": true
    },
    {
      "id": 543,
      "name": "继续教育学院",
      "telephone": "1",
      "department": 46,
      "isDisplay": true
    },
    {
      "id": 553,
      "name": "高教研究室（发展战略研究室）",
      "telephone": "1",
      "department": 47,
      "isDisplay": true
    },
    {
      "id": 557,
      "name": "图书馆",
      "telephone": "1",
      "department": 48,
      "isDisplay": true
    },
    {
      "id": 580,
      "name": "网络管理中心",
      "telephone": "1",
      "department": 49,
      "isDisplay": true
    },
    {
      "id": 39,
      "name": "党委宣传部（党委统战部）",
      "telephone": "1",
      "department": 5,
      "isDisplay": true
    },
    {
      "id": 46,
      "name": "纪委办公室（监察处）",
      "telephone": "1",
      "department": 6,
      "isDisplay": true
    },
    {
      "id": 49,
      "name": "保密工作办公室",
      "telephone": "1",
      "department": 7,
      "isDisplay": true
    },
    {
      "id": 51,
      "name": "教务处",
      "telephone": "1",
      "department": 8,
      "isDisplay": true
    },
    {
      "id": 75,
      "name": "科技处",
      "telephone": "1",
      "department": 9,
      "isDisplay": true
    }
  ]
}
```
 2. 获取黄页某一部门下的电话号码数据
   - 接口：`http://45.32.11.169/api/v2/ibistu/_table/module_yellowpage`
   - 请求方法：get
   - 参数：
      * `offset`：固定参数，值为`1`
      * `filter`：固定前缀`department=`，值为黄页接口1返回的数据中的`department`字段值
   - 示例：获取研究生工作办公室的电话号码：（此处参数值为：`department=10`）：`http://45.32.11.169/api/v2/ibistu/_table/module_yellowpage?offset=1&filter=department=10`
   - 返回值：
 ```json
{
  "resource": [
    {
      "id": 95,
      "name": "主任室",
      "telephone": "82426837",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 96,
      "name": "书记/副主任室",
      "telephone": "82426098",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 97,
      "name": "副主任室",
      "telephone": "82426097",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 98,
      "name": "研究生工作办公室",
      "telephone": "82426835",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 99,
      "name": "培养与学籍/招生与就业办公室",
      "telephone": "82426836",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 100,
      "name": "培养与学籍/招生与就业办公室",
      "telephone": "62843704",
      "department": 10,
      "isDisplay": true
    },
    {
      "id": 101,
      "name": "学位与学科建设/行政办公室",
      "telephone": "82426838",
      "department": 10,
      "isDisplay": true
    }
  ]
}
 ```
 
## 地图

 1. 获取校区位置数据
   - 接口：http://45.32.11.169/api/v2/ibistu/_table/module_map
   - 请求方法：get
   - 参数：无
   - 返回值：
  ```json
  {
  "resource": [
    {
      "id": 5,
      "areaName": "小营校区",
      "areaAddress": "北京市海淀区清河小营东路12号",
      "zipCode": "100192",
      "longitude": 40.036,
      "latitude": 116.349,
      "zoom": 10
    },
    {
      "id": 6,
      "areaName": "健翔桥校区",
      "areaAddress": "北京市朝阳区北四环中路35号",
      "zipCode": "100101",
      "longitude": 39.988,
      "latitude": 116.392,
      "zoom": 10
    },
    {
      "id": 7,
      "areaName": "清河小区",
      "areaAddress": "北京市海淀区清河四拨子",
      "zipCode": "100192",
      "longitude": 40.047,
      "latitude": 116.337,
      "zoom": 10
    },
    {
      "id": 8,
      "areaName": "金台路校区",
      "areaAddress": "北京市朝阳区金台西路2号",
      "zipCode": "100026",
      "longitude": 39.919,
      "latitude": 116.47,
      "zoom": 10
    },
    {
      "id": 9,
      "areaName": "酒仙桥校区",
      "areaAddress": "北京市朝阳区酒仙桥六街坊1号院",
      "zipCode": "100016  　　",
      "longitude": 39.963,
      "latitude": 116.49,
      "zoom": 10
    }
  ]
}
  ```
  
## 模块列表
  
  用于控制已有模块是否显示，需结合用户模块，暂无
  
  