### 运行环境
* JDK 1.8
* MySQL 5.7
* RabbitMQ
* Redis
* InfluxDB 1.5.3-1
### 主要开发技术
* Spring Boot,Thymeleaf,layUI,JWT
### 数据流
![说明](/docs/flow.jpg)
### 代码简介与扩展
* 通过实现MessagePreHandler接口增加消息预处理器
* 通过实现MessageSender接口增加消息发送器
* 可以修改ReceiverHandler这个预处理器通过自定义的接口获取消息接收人信息
* 针对标题和内容进行敏感词过滤，敏感词在sensitive-words.txt中配置
* 针对消息业务类型可以进行分钟/小时/天级别的流量控制配置
* 消息接口采用JWT token进行保护, POST http://message-rest/token
```json
{
	"sourceCode": "order",
	"password": "1234qwer"
}
```
* Message Admin的管理账户在application-dev.properties中配置
* 发送消息, POST http://message-rest/collectMessage

| **字段代码** | **字段说明** | **数据类型** | **最大长度** | **非空** |
| --- | --- | --- | --- | --- |
| **sourceCode** | 来源业务系统id | String | 8 | Y |
| **messageBizType** | 消息业务类型 | String | 32 | Y |
| **messageReceiverType** | 接受者类型（group:群组，single：个人） | String | 8 | Y |
| **messageReceiver** | 接受者（群组id或者个人id） | String | 32 | Y |
| **messageSender** | 发送人id | String | 32 | N |
| **messageBizId** | 消息业务主键 | String | 64 | N |
| **messageReceiverAddress** | 接受者地址（可空，通过预处理获得） | String | 128 | N |
| **title** | 消息标题 | String | 128 | N |
| **content** | 消息内容（直接内容或者模板填充内容） | String | 65535 | Y |
| **privilege** | 优先级 | String | 8 | N |
| **attachments** | 附件列表 | List<Attachment> |  | N |
| **extraInfo** | 扩展信息 | Map<String,Object> |  | N |

发送带附件的消息示例：
```json
{
    "attachments":[
        {
            "attName":"test.png",
            "attUrl":"https://www.baidu.com/img/superlogo_c4d7df0a003d3db9b65e9ef0fe6da1ec.png"
        },
        {
            "attName":"123.png",
            "attUrl":"http://img.ngacn.cc/attachments/mon_201806/11/-55aq9Q5-l33eZdT1kSb4-b4.png"
        }
    ],
    "content":"{\"code\":\"9527\"}",
    "messageBizType":"test_biz",
    "messageReceiver":"test_group",
    "messageReceiverType":"group",
    "sourceCode":"test",
    "title":"测试标题"
}

```
指定邮箱进行发送示例：
```json
{
    "content":"{\"code\":\"9527\"}",
    "extraInfo":{
        "email_serverCode":"gmail"
    },
    "messageBizType":"test_biz",
    "messageReceiver":"test_user",
    "messageReceiverType":"single",
    "sourceCode":"test",
    "title":"测试标题"
}

```