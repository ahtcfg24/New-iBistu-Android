# 2016-8-19 iBistu接口文档


# 前言

#### 本文档提供的接口（以以下简称接口）使用DreamFactory生成，返回json数据。

#### 接口大部分默认需要添加两个header参数：`X-DreamFactory-Api-Key`和`X-DreamFactory-Session-Token`（以下简称apikey和token），apikey的值固定为`3528bd808dde403b83b456e986ce1632d513f7a06c19f5a582058be87be0d8c2`,token的值需要登录后获取。部分不需要这两个header参数的接口添加了会自动忽略。这两个header参数完全等价于URL参数`api_key`和`session_token`，因此可以不添加header，而直接在每个接口后面以URL参数的形式追加。

- header参数格式：`"X-DreamFactory-Api-Key", "3528bd808dde403b83b456e986ce1632d513f7a06c19f5a582058be87be0d8c2"`，
`"X-DreamFactory-Session-Token","当前用户登录后获取到的token"`
- 或者以URL参数的形式添加api_key和session_token代替请求头进行接口访问。例如：`http://api.iflab.org/api/v2/ibistu/_table/module_map?api_key=3528bd808dde403b83b456e986ce1632d513f7a06c19f5a582058be87be0d8c2&session_token=当前用户登录后获取到的token`

#### 接口的返回值均为示例数据，数据数量和数据内容与实际有出入，仅用于说明结构，具体数据请自行获取测试

---
# 正文

## 安卓升级更新（本模块的接口只需要添加apikey，不需要token）

#### 检查更新

- 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_update/1`
- 请求方法：get
- 参数：无
- 示例请求成功返回值
```
{
    "id": 1,
    "name": "iBistu.apk",
    "path": "files/ibistu/update/Android/",
    "version": "1.0",
    "versionCode": 1,
    "versionInfo": "第二次更新",
    "updateTime": "2016-08-21 15:06:11",
    "updateSize": 6.38
}
```

#### 下载更新

- 接口：`http://api.iflab.org/api/v2/{检查更新返回值中的path字段}{name字段}`
- 请求方法：get
- 参数：无
- 示例URL：`http://api.iflab.org/api/v2/files/ibistu/update/Android/iBistu.apk`
- 示例请求成功返回值：iBistu.apk


## 用户模块

#### 注册（只需添加apikey）

- 接口：`http://api.iflab.org/api/v2/user/register`
- 请求方法：post
- 请求体：（name、first_name、last_name是可选参数，email字段必须符合邮箱格式）
```
{
    "email": "testuser@test.com",
    "password": "testuser",
    "phone": "13800001000",
    "name": "test user",
    "first_name": "test",
    "last_name": "user"
}
```
- 示例请求成功返回值：（可忽略）
 ```
 {
     "success": true
 }
 ```

#### 注册验证（无需添加apikey和token）

- 使用网易云信平台进行短信验证，详见[官方文档](http://dev.netease.im/docs?doc=server_sms)
- 或者使用mob平台进行短信验证，详见[官方文档](http://www.mob.com/#/download#sms)

#### 登录（只需添加apikey）

- 接口：`http://api.iflab.org/api/v2/user/session?remember_m=true`
- 请求方法：post
- 请求体：
```
{
    "email": "testuser@test.com",
    "password": "testuser"
}
```
- 示例请求成功返回值：(此处返回的session_token默认有效期为24小时，过期后必须刷新token，否则无法使用。)
```
{
   "session_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI5LCJ1c2VyX2lkIjoyOSwiZW1haWwiOiJ0ZXN0dXNlckB0ZXN0LmNvbSIsImZvcmV2ZXIiOnRydWUsImlzcyI6Imh0dHA6XC9cLzEwNC4xNTUuMjExLjE0M1wvYXBpXC92MlwvdXNlclwvc2Vzc2lvbiIsImlhdCI6MTQ3MTU0MDI4MCwiZXhwIjoxNDcxNTQzODgwLCJuYmYiOjE0NzE1NDAyODAsImp0aSI6IjFlOWI3ZTBlMDZjYzcwMDg0OGRhM2NkNDA1OTBjOGYzIn0.4I_BVND1GGp4v8aSO2_liMBCwDpBSSTgbO1oD_zbl8M",
   "session_id": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI5LCJ1c2VyX2lkIjoyOSwiZW1haWwiOiJ0ZXN0dXNlckB0ZXN0LmNvbSIsImZvcmV2ZXIiOnRydWUsImlzcyI6Imh0dHA6XC9cLzEwNC4xNTUuMjExLjE0M1wvYXBpXC92MlwvdXNlclwvc2Vzc2lvbiIsImlhdCI6MTQ3MTU0MDI4MCwiZXhwIjoxNDcxNTQzODgwLCJuYmYiOjE0NzE1NDAyODAsImp0aSI6IjFlOWI3ZTBlMDZjYzcwMDg0OGRhM2NkNDA1OTBjOGYzIn0.4I_BVND1GGp4v8aSO2_liMBCwDpBSSTgbO1oD_zbl8M",
   "id": 29,
   "name": "test user",
   "first_name": "test",
   "last_name": "user",
   "email": "testuser@test.com",
   "is_sys_admin": false,
   "last_login_date": "2016-08-18 17:11:20",
   "host": "dreamfactory",
   "role": "RegisterRole",
   "role_id": 5
}
```

#### 退出登录：

- 接口：`http://api.iflab.org/api/v2/user/session`
- 请求方法：delete
- 参数：无
- 示例请求成功返回值：（可忽略）
```
{
    "success": true
}
```

#### 修改密码：

- 接口：`http://api.iflab.org/api/v2/user/password`
- 请求方法：post
- 请求体：
```
{
    "old_password": "testuser",
    "new_password": "newtestuser",
    "email": "testuser@test.com"
}
```
- 示例请求成功返回值：（可忽略）
```
{
    "success": true
}
```

#### 请求重置密码：（只需添加apikey）

- 接口：`http://api.iflab.org/api/v2/user/password?reset=true`
- 请求方法：post
- 请求体：（请求体中的email值必须是当前登录用户的email）
```
{
    "email": "testuser@test.com"
}
```
- 示例请求成功返回值：（可忽略，该请求成功发出后就会往该用户的邮箱里发送重置密码邮件）
```
{
    "success": true
}
```

#### 刷新token：

- 接口：`http://api.iflab.org/api/v2/user/session`
- 请求方法：put
- 参数：无
- 示例：刷新当前token（此处参数值为登录后获取到的token）：`http://104.155.211.143/api/v2/user/session`
- 示例请求成功返回值：
```
{
    "session_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjExLCJ1c2VyX2lkIjoxMSwiZW1haWwiOiJqZG9lQGV4YW1wbGUuY29tIiwiZm9yZXZlciI6ZmFsc2UsImlzcyI6Imh0dHA6XC9cLzEwNC4xNTUuMjExLjE0M1wvYXBpXC92MlwvdXNlclwvc2Vzc2lvbiIsImlhdCI6MTQ3MTQzNzg0NSwiZXhwIjoxNDcxNDQxNjcyLCJuYmYiOjE0NzE0MzgwNzIsImp0aSI6ImJkZTQ3NzA0NmQ2M2FmMWYzZDJiOTVkMjJjZjEzZWZhIn0.AnqB40vdntLkxyD6WHtes7DZEQ8wsrCtpWq9aXC8MzE",
    "session_id": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjExLCJ1c2VyX2lkIjoxMSwiZW1haWwiOiJqZG9lQGV4YW1wbGUuY29tIiwiZm9yZXZlciI6ZmFsc2UsImlzcyI6Imh0dHA6XC9cLzEwNC4xNTUuMjExLjE0M1wvYXBpXC92MlwvdXNlclwvc2Vzc2lvbiIsImlhdCI6MTQ3MTQzNzg0NSwiZXhwIjoxNDcxNDQxNjcyLCJuYmYiOjE0NzE0MzgwNzIsImp0aSI6ImJkZTQ3NzA0NmQ2M2FmMWYzZDJiOTVkMjJjZjEzZWZhIn0.AnqB40vdntLkxyD6WHtes7DZEQ8wsrCtpWq9aXC8MzE",
    "id": 29,
    "name": "test user",
    "first_name": "test",
    "last_name": "user",
    "email": "testuser@test.com",
    "is_sys_admin": false,
    "last_login_date": "2016-08-17 12:44:05",
    "host": "dreamfactory",
    "role": "RegisterRole",
    "role_id": 5
}
```


## 关于模块

#### 获取iBistu相关的介绍数据

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_about`
 - 请求方法：get
 - 参数：无
 - 示例请求成功返回值：
```
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
            "id": 47,
            "aboutName": "ifLab",
            "aboutDetails": "<p><img src=\"http://iflab.org/wp-content/uploads/2013/10/IMG_8766.jpg\" width=\"100%\">\n\n<p>ifLab全称北京信息科技大学网络实践创新联盟，简称“创联”，是由学校信息网络中心组织和管理的技术社团。\n\n<p>i代表internet（互联网），innovation（创新）；f代表future（未来），fulfill（实践）。\n\n<p>我们的使命是学习和研究前沿互联网技术，建设和开发有助于学校教育信息化的项目；通过这些项目提高成员的IT水平，培养一批有专业造诣和项目实践经验的人才。\n\n<p>我们的目标是成为校内最大最强的技术社团；在信息网络中心指导下协助建设学校信息化创新项目；帮助校内社团、组织及师生提高前沿IT技能；每年培养10个以上相当于1年专业工作经验的IT人才。\n\n<p>我们欢迎校内的社团组织、学术机构与我们联盟合作。我们可以提供必要的技术协助，或者帮其他社团培养需要的技术人才。\n\n<p>目前我们主要学习研究的领域为移动互联网以及云计算。\n\n<p>你可以通过访问http://www.iflab.org或者关注新浪微博ifLab来了解我们。"
        }
    ]
}
```


## 班车模块

#### 获取班车数据

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_bus`
 - 请求方法：get
 - 参数：无
 - 示例请求成功返回值：
```
{
    "resource": [
        {
            "id": 5,
            "busName": "西线班车",
            "busType": "通勤班车",
            "departureTime": "06:55:00",
            "returnTime": "17:10:00",
            "busLine": "[{"station":"公主坟","arrivalTime":"06: 55"},{"station":"当代商城","arrivalTime":"07: 05"},{"station":"蓝旗营清华西南门","arrivalTime":"07: 15"},{"station":"小营校区","arrivalTime":"07: 45"},{"station":"小营校区","arrivalTime":"17: 10"},{"station":"蓝旗营清华西南门","arrivalTime":"17: 35"},{"station":"当代商城","arrivalTime":"17: 50"},{"station":"公主坟","arrivalTime":"18: 20"}]",
            "busIntro": "西线班车"
        },
        {
            "id": 11,
            "busName": "望京-健翔桥",
            "busType": "通勤班车",
            "departureTime": "07:15:00",
            "returnTime": "17:10:00",
            "busLine": "[{"station":"望兴园","arrivalTime":"07: 15"},{"station":"望京花园","arrivalTime":"07: 17"},{"station":"健翔桥校区","arrivalTime":"07: 45"},{"station":"健翔桥校区","arrivalTime":"17: 10"},{"station":"望京花园","arrivalTime":"17: 45"},{"station":"望兴园","arrivalTime":"17: 47"}]",
            "busIntro": "望京-健翔桥"
        },
        {
            "id": 12,
            "busName": "小营-清河校区南门-健翔桥",
            "busType": "教学班车",
            "departureTime": "09:50:00",
            "returnTime": null,
            "busLine": "[{"station":"小营校区","arrivalTime":"09: 50"},{"station":"清河小区南门","arrivalTime":"09: 51"},{"station":"健翔桥校区","arrivalTime":"10: 35"}]",
            "busIntro": "小营-清河小区南门-健翔桥"
        },
        {
            "id": 17,
            "busName": "健翔桥-小营",
            "busType": "教学班车",
            "departureTime": "09:50:00",
            "returnTime": null,
            "busLine": "[{"station":"健翔桥校区","arrivalTime":"09: 50"},{"station":"小营校区","arrivalTime":"10: 10"}]",
            "busIntro": "健翔桥-小营"
        }
    ]
}
```


## 黄页模块

#### 获取黄页部门列表数据

 - 接口：`ibistu/_table/module_yellowpage?filter=isDisplay%3D1&offset=1&group=department`
 - 请求方法：get
 - 参数：无
 - 示例请求成功返回值：
```
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
            "id": 274,
            "name": "健翔桥校区",
            "telephone": "1",
            "department": 21,
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

#### 获取黄页某一部门下的电话号码数据

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_yellowpage`
 - 请求方法：get
 - 参数：

     + `offset`：固定参数，值为`1`
     + `filter`：固定前缀`department=`，值为黄页接口1返回的数据中的`department`字段值
 - 示例：获取研究生工作办公室的电话号码：（此处参数值为：`department=10`）：`http://api.iflab.org/api/v2/ibistu/_table/module_yellowpage?offset=1&filter=department=10`
 - 示例请求成功返回值：
```
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
      "id": 97,
      "name": "副主任室",
      "telephone": "82426097",
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


## 全景模块

#### 获取全景列表数据

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_vr`
 - 请求方法：get
 - 参数：无
 - 示例请求成功返回值：
```
{
    "resource": [
        {
            "id": 2,
            "resource": "http://7xp4hv.com1.z0.glb.clouddn.com/image/vr/20098c.jpg",
            "preview": "http://7xp4hv.com1.z0.glb.clouddn.com/image_preview.jpg",
            "title": "示例360度全景图片",
            "type": "IMAGE"
        },
        {
            "id": 3,
            "resource": "http://7xp4hv.com1.z0.glb.clouddn.com/image/vr/stereo.jpg",
            "preview": "http://7xp4hv.com1.z0.glb.clouddn.com/image/vr/stereo.jpg",
            "title": "示例STEREO镜头图片",
            "type": "IMAGE"
        }
    ]
}
```


## 新闻模块

#### 获取新闻列表数据

 - 接口：`http://api.iflab.org/api/v2/newsapi/newslist`
 - 请求方法：get
 - 参数：

    + `category`：必需参数，新闻分类，可选的值为"zhxw", "tpxw", "rcpy", "jxky", "whhd", "xyrw", "jlhz", "shfw", "mtgz"
    + `page`：必需参数，当前页数，从0开始，0表示第一页
 - 示例：获取第5页的综合新闻：（此处参数值为：`category=zhxw`，`page=4`）：`http://api.iflab.org/api/v2/newsapi/newslist?category=zhxw&page=5`
 - 示例请求成功返回值：
```
[
    {
        "newsTitle": "我校召开2016年本科招生录取工作总结会",
        "newsTime": "2016-09-14",
        "newsLink": "http://news.bistu.edu.cn/zhxw/201609/t20160914_39069.html",
        "newsImage": "http://news.bistu.edu.cn/zhxw/201609/W020160914608238897988_135.jpg",
        "newsIntro": "9月14日上午，学校在小营校区召开2016年度本科生招生录取工作总结会。副校长许宝杰主持会议并做总结讲话，相关职能部门负责人及招生录取工作相关人员参加了会议。\n　　\n"
    },
    {
        "newsTitle": "学校领导走访校友企业“佰能蓝天”",
        "newsTime": "2016-09-13",
        "newsLink": "http://news.bistu.edu.cn/zhxw/201609/t20160914_39066.html",
        "newsImage": "",
        "newsIntro": "9月13日下午，校友会常务副会长、副校长韩秋实，校友会副会长、校长助理林国策，校友校史工作办公室、自动化学院相关领导及工作人员一行前往校友企业“北京佰能蓝天科技股份公司”调研，并祝贺“佰能蓝天”公司成...\n"
    },
    ...
    {
        "newsTitle": "【院长访谈】 李宁教授畅谈大类招生分流培养",
        "newsTime": "2016-09-12",
        "newsLink": "http://news.bistu.edu.cn/zhxw/201609/t20160912_39011.html",
        "newsImage": "http://news.bistu.edu.cn/zhxw/201609/W020160912316469392018_135.jpg",
        "newsIntro": "“大类招生、分流培养”作为一种新的人才培养模式正逐渐被国内外高校所采用。计算机学院是我校实行大类招生的试点学院。大类招生始于2014年，至今已有3年时间。\n"
    }
]
```

#### 获取新闻详情

 - 接口：`http://api.iflab.org/api/v2/newsapi/newsdetail`
 - 请求方法：get
 - 参数：

    + `link`：必需参数，新闻列表接口中返回的newsLink内容
 - 示例：获取link为`http://news.bistu.edu.cn/zhxw/201609/t20160912_39011.html`的新闻详情：（此处参数值为：`link=http://news.bistu.edu.cn/zhxw/201609/t20160912_39011.html`）：`http://api.iflab.org/api/v2/newsapi/newsdetail?link=http://news.bistu.edu.cn/zhxw/201609/t20160912_39011.html`
 - 示例请求成功返回值：
```
{
    "title": "【院长访谈】 李宁教授畅谈大类招生分流培养",
    "time": "2016-09-12",
    "article": "　　“大类招生、分流培养”作为一种新的人才培养模式正逐渐被国内外高校所采用。计算机学院是我校实行大类招生的试点学院。大类招生始于2014年，至今已有3年时间。如今第一批大类招生的学生已经顺利平稳分流到各个专业，中间是否有经验和值得探讨的问题？带着这些疑问我们采访了计算机学院院长李宁教授。\n \n\n \n　　记者：李院长您好，今年计算机学院仍然推行大类招生措施吗？能否简单介绍下相关情况？\n　　李宁：您好，很愿意与大家分享计算机学院推行大类招生方面的经验。计算机学院今年仍然实行大类招生，以后仍然如此。目前按大类招生的2016级学生366人已经报到360人，报到率为98.4%，为全校第三名。\n \n　　记者：大类招生相对于一般招生而言有什么优势？\n　　李宁：我国的大类招生大体始于2003年，有30多所211工程院校实施了“大类招生、分流培养”模式，并得到了大多数学生和家长的认可。大类招生的优势一般可归结为以下几点：1）大类招生顺应社会需要，体现“宽口径、厚基础、强能力、高素质”，可以避免专才教育导致的知识面过窄的问题；2）可以避免高考时学生选择专业的盲目性，经过一段时间的基础课学习，让学生更好地了解自己，结合社会发展需要，从容地选择专业，使专业与自身的契合度大大提高；3)在高校办学层面上，有利于提高生源质量，提高办学效益和教育质量；4)在学科专业建设层面上，有利于加强专业建设，形成院系良性竞争，构建新型的人才培养模式和课程体系；5)在学生自身发展层面上，有利于提高学生综合能力，因材施教，培养创新型人才。\n \n　　记者：专业分流问题是大类招生一直探讨的一个焦点，我们学校的专业分流平稳，请问我们是按照学习成绩来进行专业分流的吗？\n　　李宁：关于按学习成绩进行专业分流的问题。学生的在校成绩是学生学习能力和学习效果的最重要的评价依据。教育资源优先满足优秀的学生本身无可厚非，高考本身体现的就是这个原则，问题的关键是专业分流所依据的成绩评价是否客观。一些学校把高考成绩也作为分流的依据之一，显然有失偏颇，这是高考“一考定终身”流弊的延续。我们认为，专业分流的成绩评价应该反映出学生个体的差异，即对于不同专业的兴趣和潜力，体现他们对于未来不同专业的契合度。\n \n　　记者：影响学生未来专业选择的重要因素是什么？专业分流的最佳时机是什么时候？\n　　李宁：我们认为分流前的学生所学的课程对其未来的专业选择有重要影响。因此专业分流最佳时机是学生基本学完了大类基础课，并学习了少量专业基础课，对各个专业有了初步认识和把握之后，同时又在系统地开展专业教学之前。所以，不应该为了专业分流将基础课和专业课截然分开，时间也不宜过早或过晚。我们认为第3学期之后是比较适宜的。这里有个前提条件，即大类培养应在相近的学科专业之间开展，这样才有可能精细地设置各个阶段的课程。此外，我校实行的是大类招生（计算机学院为主体）与转专业（学校为主体）并行的制度，因此，学生的跨学科的专业选择也是可能的。\n　　与很多高校不同的是，专业分流中我们对学生成绩的评价按不同的权重考虑了以下几方面：1）平均学分绩点；2）所修的不同大类课程对于不同专业的影响度，例如：“计算机电路基础”对于计算机科学与技术专业和网络工程专业的影响度高于软件工程专业，而“面向对象技术”则反之。这样给出的评价对于不同的专业更有区分性；3）专业发展概论课程的成绩。显然这样的成绩评价比单纯考察总成绩更为客观合理。\n \n　　记者：专业是否应该完全由学生自己来选择？\n　　李宁：与很多观点不同，我们认为专业的选择应该是双向的。这是因为：1）学校的专业设置是依据教育部的学科专业目录，按照国家对人才的需求、学校的办学定位以及办学条件而确定的。虽然也应该与时俱进，但不应太受学生一时好恶的影响；2）学生在选择专业时有时存在一定盲目性，一些研究发现，学生选择专业时参考的信息来源有7.5%来自于家长；13%来自于老师；25.5%来自于学长；14.5%来自于本年级同学；17.5%来自于自己查阅的资料；21%来自于所选的课程；1%来自于其他渠道。可见，学长和同学对专业选择有着绝对的影响，甚至出现了同一个自然班或同一个宿舍的学生同时选择某个专业的现象。在这种情况下，就不可能让学生完全自由选择，而是需要主动引导学生作出更为适合的选择；3）学生并不是凭兴趣就能学好所有的专业，一些专业需要有较强的数理基础，一些专业需要较强的实践能力，一些专业则需要较强的人际沟通能力，并不是所有学生都能做到。因此，专业教师应该起到把关的作用，对学生进行有目标的引导，挑选有发展前途的学生进到相应的专业学习。\n \n　　记者：能否简单介绍我们的专业分流过程和分流经验？\n　　李宁：我们的专业分流安排在第3学期期末进行，在这个时间点，主要的大类课程已基本上完，专业教学尚未全面展开。在第3学期开设专业发展概论课之后，学生对专业分流已经心中有数，即可开始准备。学生有充裕的时间填报3个志愿，待期末成绩公布便可通过分流系统迅速看到公示的结果。在暑假前完成全部的分流工作，便于学生填报下学期的专业选修课程，并在假期做好专业课学习的准备。目前，计算机学院涉及大类招生的3个专业的年度招生计划是：计算机科学与技术：180人，软件工程：90人，网络工程：90人。根据学生报名情况，各专业招收人数允许上下浮动10%，这样既兼顾了学生的意愿，又不至于打乱既有的教学计划。\n　　针对影响大类招生和专业分流的瓶颈，即专业引导和评价体系这两个关键问题，我们设计了特色鲜明的“计算机专业发展概论”课程，用学生容易接受的方式，端正学生对专业的认识，激发他们的专业兴趣，同时对每个学生与3个专业的契合度进行了区分性的评价，通过面试等方式了解学生的专业认知，并给予正确的引导。同时建立了比较完善的成绩评价体系，除了考虑学生的总成绩之外，还要考虑大类阶段各门课程成绩对各个专业的影响度。在此基础上，开发的分流系统能够“短平快”地得到分流结果。\n \n　　记者：大类招生是否可以导致学生热衷热门专业，而有些专业则更加冷门？学院是如何思考、处理这些问题的？\n　　李宁：应该看到，专业分流是一个复杂的工程，除了及时的专业引导和建设合理的分流体系之外，它与我们的办学理念，学校的专业布局和教学改革的力度密切相关。显然，大类招生最多能够淘汰不受欢迎的专业，但却不能产生受欢迎的专业。通过专业引导，我们可以说服学生选择一些冷门专业，但是动机必须端正，即我们保护的冷门专业一定是仍有社会需求的，在未来仍有发展前途的专业，而不是从局部利益出发保护已经过气的专业。总之，作为办学主体我们应该清楚什么是应该坚持的，什么是需要改革的，而不是完全听命于受教育者和当前的就业市场，因而大类招生也同时考量着教育者和管理者的胆识和办学自信。（供稿：党委宣传部）",
    "imgList": [
        "http://news.bistu.edu.cn/zhxw/201609/W020160912316468133294.jpg",
        "http://news.bistu.edu.cn/zhxw/201609/W020160912316468133295.jpg",
        "http://news.bistu.edu.cn/zhxw/201609/W020160912316468133296.jpg"
    ]
}
```


## 失物招领模块

#### 获取失物招领列表数据

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_lost_found?limit=10&order=createTime%20desc`
 - 请求方法：get
 - 参数：

    + `offset`：必需参数，招领信息（页数-1）的10倍，当offset为0时，返回的是第一页的数据，offset为10时，返回的是第二页，20时返回的是第三页，依次类推。
    + `filter`：必需参数，只有两个值，值为“isFound=false”时，返回普通列表数据；当值为“(isFound=false)And(author=用户名)”时，返回的是该用户发布的所有招领信息
 - 示例：获取第2页的用户名为mphone的已发布信息列表，：（此处参数值为：`offset=10`，`filter=(isFound=false)And(author=mphone)`，如果想获取普通列表，则`filter=isFound=false`）：`http://api.iflab.org/api/v2/ibistu/_table/module_lost_found?limit=10&order=createTime%20desc&offset=10&filter=isFound=false`
 - 示例请求成功返回值：
```
{
    "resource": [
        {
            "id": 86,
            "details": "捡到一堆小玩意儿",
            "createTime": "2016-10-13 13:26:30",
            "author": "mphone",
            "phone": "13622251463",
            "isFound": false,
            "imgUrlList": "[{\"url\":\"files/ibistu/lost_found/image/14763363892752229.jpg\"},{\"url\":\"files/ibistu/lost_found/image/14763363331344112.jpg\"}]"
        },
        {
            "id": 85,
            "details": "捡到一盒餐具，不知道是谁的，如图所示",
            "createTime": "2016-10-13 13:25:33",
            "author": "mphone",
            "phone": "13688881425",
            "isFound": false,
            "imgUrlList": "[{\"url\":\"files/ibistu/lost_found/image/14763363331344112.jpg\"}]"
        }
    ]
}
```

#### 上传图片（多图上传）

 - 接口：`http://api.iflab.org/api/v2/files/ibistu/lost_found/image/`
 - 请求方法：post
 - 请求体：（上传图片需要将图片转换为base64格式的编码字符串，然后作为content）
```
{
    "resource": [
        {
            "name": "filename1.jpg",
            "type": "file",
            "is_base64": true,
            "content": "base64字符串"
        },
        {
            "name": "filename2.jpg",
            "type": "file",
            "is_base64": true,
            "content": "base64字符串"
        },
        ...,
        {
            "name": "filename3.jpg",
            "type": "file",
            "is_base64": true,
            "content": "base64字符串"
        }
    ]
}
```
 - 示例请求成功返回值：（上传成功后文件的真实存储路径为：“http://api.iflab.org/api/v2/files/上传成功后返回的path值”）
```
{
 "resource": [{
   "name": "filename1.jpg",
   "type": "file",
   "path": "图片存放的相对路径"
 }
 ,{
   "name": "filename2.jpg",
   "type": "file",
   "path": "图片存放的相对路径"
 }
 ...
 ,
 {
   "name": "filename3.jpg",
   "type": "file",
   "path": "图片存放的相对路径"
 }]
}

```

#### 发布新招领信息

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_lost_found`
 - 请求方法：post
 - 请求体：(其中imgUrlList中的url为:“files/上传图片成功后返回的path”)
 ```
 [
    {
        "details": "捡到一盒餐具，不知道是谁的，如图所示",
        "author": "mphone",
        "phone": "13688881425",
        "imgUrlList": "[{\"url\":\"files/ibistu/lost_found/image/14763363892752229.jpg\"},{\"url\":\"files/ibistu/lost_found/image/14763363331344112.jpg\"}]"
    }
]
```
 - 示例请求成功返回值：（可忽略）
```
{
  "resource": [
    {
      "id": 93
    }
  ]
}
```

#### 将某条招领信息设为完结

 - 接口：`http://api.iflab.org/api/v2/ibistu/_table/module_lost_found/{id}`(id为需要改变状态的一条信息的具体id)
 - 请求方法：patch
 - 请求体：
 ```
 {
   "isFound": 1
 }
```
 - 示例请求成功返回值：(可忽略)
```
{
  "id": "93"
}
```

## 地图模块

#### 获取校区位置数据

 - 接口: `http://api.iflab.org/api/v2/ibistu/_table/module_map`
 - 请求方法：get
 - 参数：无
 - 示例请求成功返回值：
 ```
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


## 错误接口

#### 错误接口指接口访问失败时返回的json数据

 - 返回值结构：
```
{
    "error": {
        "context": "产生异常的上下文环境",
        "message": "异常原因",
        "code": 异常状态码
    }
}
```


# DreamFacotory官方Demo：
  + [iOS-Objective-C](https://github.com/dreamfactorysoftware/ios-sdk)
  + [iOS-Swift](https://github.com/dreamfactorysoftware/ios-swift-sdk)
  + [ReactJS](https://github.com/dreamfactorysoftware/reactjs-sdk)
  + [Android](https://github.com/dreamfactorysoftware/android-sdk)
