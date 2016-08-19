# 2016-8-19 iBistu接口文档


# 前言
---

 * 本文档提供的接口使用DreamFactory生成，默认返回json数据。

  - 返回的json结构：
 ```
 {
      "resource" :  [
                    { 对象1 } ,  
                    { 对象2 } ,  
                    ... ,
                    { 对象n }
              ]
 }
 ```
 * 本文档提供的接口除注册、登录、请求重置密码只需要添加请求头`X-DreamFactory-Api-Key`以外，其它接口均需要添加`X-DreamFactory-Api-Key`和`X-DreamFactory-Session-Token`。注册、登录、重置密码接口会自动忽略`X-DreamFactory-Session-Token`。

  - 请求头及其格式为：
 > "X-DreamFactory-Api-Key", "dreamfactory提供的apikey"

    > "X-DreamFactory-Session-Token","当前登录获取到的token"

  - 也可以不使用请求头，直接以URL参数的形式添加api_key和session_token代替请求头进行接口访问。例如：
 > http://api.ifalb.org/api/v2/ibistu/_table/module_map?api_key=3528bd808dde403b83b456e986ce1632d513f7a06c19f5a582058be87be0d8c2&session_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI5LCJ1c2VyX2lkIjoyOSwiZW1haWwiOiJ0ZXN0dXNlckB0ZXN0LmNvbSIsImZvcmV2ZXIiOnRydWUsImlzcyI6Imh0dHA6XC9cLzEwNC4xNTUuMjExLjE0M1wvYXBpXC92MlwvdXNlclwvc2Vzc2lvbiIsImlhdCI6MTQ3MTU0MDI4MCwiZXhwIjoxNDcxNTQzODgwLCJuYmYiOjE0NzE1NDAyODAsImp0aSI6IjFlOWI3ZTBlMDZjYzcwMDg0OGRhM2NkNDA1OTBjOGYzIn0.4I_BVND1GGp4v8aSO2_liMBCwDpBSSTgbO1oD_zbl8M

 * 本文档提供的接口均可以通过添加可选参数`include_count=true`以获得包含数据对象总数的json数据。例如：
 > http://api.ifalb.org/api/v2/ibistu/_table/module_map?include_count=true&api_key=3528bd808dde403b83b456e986ce1632d513f7a06c19f5a582058be87be0d8c2&session_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjI5LCJ1c2VyX2lkIjoyOSwiZW1haWwiOiJ0ZXN0dXNlckB0ZXN0LmNvbSIsImZvcmV2ZXIiOnRydWUsImlzcyI6Imh0dHA6XC9cLzEwNC4xNTUuMjExLjE0M1wvYXBpXC92MlwvdXNlclwvc2Vzc2lvbiIsImlhdCI6MTQ3MTU0MDI4MCwiZXhwIjoxNDcxNTQzODgwLCJuYmYiOjE0NzE1NDAyODAsImp0aSI6IjFlOWI3ZTBlMDZjYzcwMDg0OGRhM2NkNDA1OTBjOGYzIn0.4I_BVND1GGp4v8aSO2_liMBCwDpBSSTgbO1oD_zbl8M

  - 返回值结构：
 ```
 {
      "resource" :  [
                    { 对象1 } ,  
                    { 对象2 } ,  
                    ... ,
                    { 对象n }
              ] ,
       "meta" :  { "count" : 对象数量n }
 }
 ```
 * 本文档提供的接口也可以通过添加可选参数`accept=application/xml`转变成xml结构的数据
 * 本文档提供的接口的返回值均为示例数据，数据数量有删减，仅用于说明结构，具体数据请自行获取测试

---
# 正文
---


## 用户模块

 * 注册

    - 接口：`http://api.ifalb.org/api/v2/user/register`
    - 请求方法：post
    - 请求体：

    ```
    {
      "email": "testuser@test.com",
      "password":"testuser",
      "phone":"13800001000",
      "name": "test user",
      "first_name": "test",
      "last_name": "user"
    }
    ```
      + name、first_name、last_name是可选参数
      + email字段必须符合邮箱格式
    - 示例返回值：
    ```
    {
    "success":true
    }
    ```
 * 注册验证

    - 注册验证使用网易云信对手机进行短信验证，详见[官方文档](http://dev.netease.im/docs?doc=server_sms)
 * 登录

    - 接口：`http://api.iflab.org/api/v2/user/session?remember_m=true`
    - 请求方法：post
    - 请求体：
    ```
    {
    "email": "testuser@test.com",
    "password":"testuser"
    }
    ```

      + 此处返回的session_token默认有效期为24小时，过期后必须刷新token，否则无法使用。
    - 示例返回值：
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
 * 退出登录：

    - 接口：`http://api.ifalb.org/api/v2/user/session`
    - 请求方法：delete
    - 参数：无
    - 示例返回值：
    ```
    {
        "success": true
    }
    ```
 * 修改密码：

    - 接口：`http://api.ifalb.org/api/v2/user/password`
    - 请求方法：post
    - 请求体：
    ```
    {
      "old_password": "testuser",
      "new_password": "newtestuser",
      "email": "testuser@test.com"
    }
    ```
    - 示例返回值：
    ```
    {
      "success": true
    }
    ```
 * 请求重置密码：

    - 接口：`http://api.ifalb.org/api/v2/user/password?reset=true`
    - 请求方法：post
    - 请求体：

    ```
    {
      "email": "testuser@test.com"
    }
    ```
      + 此处email值必须是当前登录用户的email
      + 该请求发出后就会往该用户的邮箱里发送重置密码邮件
    - 示例返回值：
    ```
    {
      "success": true
    }
    ```
 * 刷新token：

    - 接口：`http://api.ifalb.org/api/v2/user/session`
    - 请求方法：put
    - 参数：无
    - 示例：刷新当前token（此处参数值为登录后获取到的token）：`http://104.155.211.143/api/v2/user/session`
    - 示例返回值：
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

 * 获取iBistu相关的介绍数据

   - 接口：`http://api.ifalb.org/api/v2/ibistu/_table/module_about`
   - 请求方法：get
   - 参数：无
   - 示例返回值：
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

 * 获取班车数据

   - 接口：`http://api.ifalb.org/api/v2/ibistu/_table/module_bus`
   - 请求方法：get
   - 参数：无
   - 示例返回值：
 ```
 {
  "resource": [
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


## 黄页模块

 * 获取黄页部门列表数据

   - 接口：`http://api.ifalb.org/api/v2/ibistu/_table/module_department_list`
   - 请求方法：get
   - 参数：无
   - 示例返回值：
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
 * 获取黄页某一部门下的电话号码数据

   - 接口：`http://api.ifalb.org/api/v2/ibistu/_table/module_yellowpage`
   - 请求方法：get
   - 参数：

      * `offset`：固定参数，值为`1`
      * `filter`：固定前缀`department=`，值为黄页接口1返回的数据中的`department`字段值
   - 示例：获取研究生工作办公室的电话号码：（此处参数值为：`department=10`）：`http://api.ifalb.org/api/v2/ibistu/_table/module_yellowpage?offset=1&filter=department=10`
   - 示例返回值：
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


## 地图模块

 * 获取校区位置数据

   - 接口: `http://api.ifalb.org/api/v2/ibistu/_table/module_map`
   - 请求方法：get
   - 参数：无
   - 示例返回值：
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


## 模块列表

  用于控制已有模块是否显示，暂无


## 错误接口

 * 错误接口指接口访问失败时返回的json数据

   - 返回值结构：
  ```
  {
    "error": {
      "context": 产生异常的上下文环境,
      "message": 异常原因,
      "code": 异常状态码
    }
  }
  ```
   - 关于json的处理可参考DreamFacotory官方Demo中的处理流程：
      + [iOS-Objective-C](https://github.com/dreamfactorysoftware/ios-sdk)
      + [iOS-Swift](https://github.com/dreamfactorysoftware/ios-swift-sdk)
      + [ReactJS](https://github.com/dreamfactorysoftware/reactjs-sdk)
      + [Android](https://github.com/dreamfactorysoftware/android-sdk)
