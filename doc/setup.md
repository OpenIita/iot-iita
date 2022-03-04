## EMQX安装和配置
### 安装
参考官方文档：[https://www.emqx.io/docs/zh/v4.3/getting-started/getting-started.html](https://www.emqx.io/docs/zh/v4.3/getting-started/getting-started.html)
### 配置
#### emqx认证配置
emqx安装目录/etc/plugins/emqx_auth_http.conf
```properties
auth.http.auth_req.url = http://127.0.0.1:8082/mqtt/auth
auth.http.auth_req.headers.content_type = application/json
auth.http.auth_req.params = clientid=%c,username=%u,password=%P
auth.http.acl_req.url = http://127.0.0.1:8082/mqtt/acl
auth.http.acl_req.method = post
auth.http.acl_req.headers.content-type = application/json
auth.http.acl_req.params = access=%A,username=%u,clientid=%c,ipaddr=%a,topic=%t,mountpoint=%m
```
#### emqx插件启用配置
emqx安装目录/data/loaded_plugins
需要配置的项：
```erlang
{emqx_dashboard, true}.
{emqx_rule_engine, true}.
{emqx_auth_http, true}.
```
#### emqx规则引擎配置
启动emqx，进行管理后台界面
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646361469338-c2a47705-7663-4747-bd98-ada424d06e18.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=532&id=u80366cb7&margin=%5Bobject%20Object%5D&name=image.png&originHeight=958&originWidth=2552&originalType=binary&ratio=1&rotation=0&showTitle=false&size=484997&status=done&style=none&taskId=u97af5399-e533-43c7-9beb-fe3bff98c81&title=&width=1417.7778153360637)新建规则
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646361550118-ae6d690b-54e9-4cc8-9d99-24ea703102ee.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=789&id=u369151bb&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1420&originWidth=1886&originalType=binary&ratio=1&rotation=0&showTitle=false&size=183530&status=done&style=none&taskId=u551fb457-3c67-4fc1-92ff-b307b2bd37d&title=&width=1047.7778055344106)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646361567954-3c247928-88dd-4ed2-bc86-7387d6a99910.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=601&id=u320f14ab&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1082&originWidth=2178&originalType=binary&ratio=1&rotation=0&showTitle=false&size=126355&status=done&style=none&taskId=u3719e96f-ea63-40cc-930b-56cd13c7a26&title=&width=1210.0000320540544)
具体配置如下：
**client_disconnected:**
```sql
SELECT
  reason,
  clientid,
  username,
  peername,
  socketname
FROM
 "$events/client_connected"
```
```json
Type: republish
target_topic: /sys/client/connected
target_qos: 1
payload_tmpl: {"reason":"${reason}","clientid":"${clientid}","username":"${username}","peername":"${peername}","socketname":"${socketname}"}
```
**client_connected**:
```sql
SELECT
  reason,
  clientid,
  username,
  peername,
  socketname
FROM
 "$events/client_disconnected"
```
```json
Type: republish
target_topic: /sys/client/disconnected
target_qos: 1
payload_tmpl: {"reason":"${reason}","clientid":"${clientid}","username":"${username}","peername":"${peername}","socketname":"${socketname}"}
```


## keycloak安装和配置
Keycloak 为现代应用和分布式服务提供了一套完整的认证授权管理解决方案，它是开源的，是一个独立的认证授权服务器。官网：[https://www.keycloak.org/](https://www.keycloak.org/)
下载：
[https://www.keycloak.org/downloads](https://www.keycloak.org/downloads)![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646362037970-2ee933e4-c7c7-4019-9a23-68e2c81a1758.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=368&id=u99e0d10d&margin=%5Bobject%20Object%5D&name=image.png&originHeight=662&originWidth=2394&originalType=binary&ratio=1&rotation=0&showTitle=false&size=453768&status=done&style=none&taskId=uc7a14fb8-1ad7-44a9-8b9e-c09d8bf3f06&title=&width=1330.0000352329687)
解压启动即可
如果不是部署在本机，需要将config/keycloak.conf中的hostname改为可以外部访问的IP或域名
### 配置
启动后进行管理后台：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646362407165-1d2869de-e3df-499a-a417-22ce7617a826.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=633&id=u1f25352d&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1140&originWidth=2180&originalType=binary&ratio=1&rotation=0&showTitle=false&size=571762&status=done&style=none&taskId=u32b28065-e954-452c-8724-a146462b958&title=&width=1211.1111431945997)
#### 添加一个realm
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646362463636-712380e6-9d84-4c90-9702-da939df1565b.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=314&id=uc255089b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=566&originWidth=686&originalType=binary&ratio=1&rotation=0&showTitle=false&size=60196&status=done&style=none&taskId=u945f47db-8917-4df3-8e21-7808bf4f69f&title=&width=381.111121207108)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646362546918-18626af6-51c8-4f92-9f3a-1031caf15393.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=502&id=u61b2b0da&margin=%5Bobject%20Object%5D&name=image.png&originHeight=904&originWidth=928&originalType=binary&ratio=1&rotation=0&showTitle=false&size=85044&status=done&style=none&taskId=u550d3e0b-8e00-451b-b356-abc66284565&title=&width=515.555569213114)
#### 添加client
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646362708842-0e24469b-9d8f-49b3-aa1a-5134af919ec7.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=826&id=u3260aa5d&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1486&originWidth=1894&originalType=binary&ratio=1&rotation=0&showTitle=false&size=230619&status=done&style=none&taskId=ufc9e10b0-6795-42ad-af03-44a23c10f84&title=&width=1052.2222500965927)![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646362804161-7b4cc57c-1ff7-4e35-8348-2870fdc8cd37.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=403&id=uc8982895&margin=%5Bobject%20Object%5D&name=image.png&originHeight=726&originWidth=1610&originalType=binary&ratio=1&rotation=0&showTitle=false&size=85484&status=done&style=none&taskId=ub9301eae-e69e-45bc-8e40-6ed049fd051&title=&width=894.444468139131)
特别注意：
_ Valid Redirect URIs 需要填写iot系统前端的访问地址，以/*结尾，点“+”可以填多个_
#### 添加角色
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646363526883-4a51c812-4cd2-4a6f-95ef-f1daa830f410.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=403&id=u46de2d93&margin=%5Bobject%20Object%5D&name=image.png&originHeight=726&originWidth=2042&originalType=binary&ratio=1&rotation=0&showTitle=false&size=449437&status=done&style=none&taskId=u6319f808-048b-4690-997d-b8ba5a724ce&title=&width=1134.4444744969599)
#### 新增用户组
client为C端用户组，platform为平台用户组
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646364377380-c90ffaf7-b5e5-43c7-80b5-b0fbcee9e79e.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=591&id=uc69f55b2&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1064&originWidth=1222&originalType=binary&ratio=1&rotation=0&showTitle=false&size=119473&status=done&style=none&taskId=u00888a81-8ee2-4d68-8599-30d1b37bb3e&title=&width=678.8889068733032)
#### 新增一个管理员用户
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646363274878-81957323-31c2-41f4-9d76-3b7e9e000e8d.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=692&id=u91d8c013&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1246&originWidth=1544&originalType=binary&ratio=1&rotation=0&showTitle=false&size=188855&status=done&style=none&taskId=u391ea8ae-6ac2-42d6-b600-d0ccee1a8b0&title=&width=857.7778005011294)
并设置密码：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646363315839-8b08cc22-b0f2-4dee-af62-09a7003bcfcb.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=482&id=u0d2433aa&margin=%5Bobject%20Object%5D&name=image.png&originHeight=868&originWidth=1554&originalType=binary&ratio=1&rotation=0&showTitle=false&size=98033&status=done&style=none&taskId=uf5d1c686-e7ff-440b-957c-dd94f1b7177&title=&width=863.3333562038569)
指定用户角色：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646363584659-f765b150-7941-4ab5-8458-6eac2b934435.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=340&id=ub784b904&margin=%5Bobject%20Object%5D&name=image.png&originHeight=612&originWidth=1394&originalType=binary&ratio=1&rotation=0&showTitle=false&size=263046&status=done&style=none&taskId=u8df0d607-82a9-4df8-b570-c902b82751e&title=&width=774.4444649602166)
指定用户组：
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646364460623-6e5f2b19-60dc-41cc-b7ba-4c1921944b91.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=321&id=uad769b6b&margin=%5Bobject%20Object%5D&name=image.png&originHeight=578&originWidth=1518&originalType=binary&ratio=1&rotation=0&showTitle=false&size=89127&status=done&style=none&taskId=u8ecdc268-dfec-4730-ab02-bdc9ef034ff&title=&width=843.3333556740379)


#### 新增一个keycloak api用户
用于调用keycloak接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646364020558-2252f661-bd31-4721-b31e-8610314d1da5.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=499&id=u85754047&margin=%5Bobject%20Object%5D&name=image.png&originHeight=898&originWidth=1462&originalType=binary&ratio=1&rotation=0&showTitle=false&size=99896&status=done&style=none&taskId=ufe8baf91-b062-4213-b2cb-e7cbc91d886&title=&width=812.2222437387637)


## mongodb安装和配置


本人用的是阿里云的mongodb serverless版，1.27元/天，简单快捷省去搭建和维护工作
购买后，生成公网链接、添加数据库访问白名单即可
## 程序配置
平台由4个独立启动的程序构成：
**mqtt-auth**：用于emqx的auth_http认证，见《[emqx认证配置](#CLNCe)》
**mqtt-server**：用于设备消息处理（事件上报、服务回复、设备上下线）和设备指令下发
**manager**：为iot-console-web提供后台接口，以及执行其它业务逻辑
**iot-console-web**：iot平台pc端前端程序
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646368262365-98b93960-b8af-4410-a5cf-338ac45329b4.png#clientId=u985ddeea-6bca-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=388&id=wvxhq&margin=%5Bobject%20Object%5D&name=image.png&originHeight=698&originWidth=656&originalType=binary&ratio=1&rotation=0&showTitle=false&size=86948&status=done&style=none&taskId=u09a3aca8-a468-459e-af95-35a0c7b8e98&title=&width=364.44445409892546)


### mqtt-auth
#### 程序结构
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646369960452-a8722503-b5cc-4512-b416-d9b8610472eb.png#clientId=u680cddd7-508b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=337&id=u4dde0c06&margin=%5Bobject%20Object%5D&name=image.png&originHeight=606&originWidth=686&originalType=binary&ratio=1&rotation=0&showTitle=false&size=79551&status=done&style=none&taskId=u2943d2d6-844b-449c-9dad-474f8d521dc&title=&width=381.111121207108)
#### 配置
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://填写mongodb地址/admin
      database: iotkit
```
​

### mqtt-server
#### 程序结构
![image.png](https://cdn.nlark.com/yuque/0/2022/png/12603824/1646371520326-569d4a24-db3e-4928-ad38-3ebcad040883.png#clientId=u680cddd7-508b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=337&id=u9fe9b355&margin=%5Bobject%20Object%5D&name=image.png&originHeight=606&originWidth=620&originalType=binary&ratio=1&rotation=0&showTitle=false&size=73146&status=done&style=none&taskId=uabd07468-279d-40b3-9b79-e4a00b98a68&title=&width=344.4444535691064)
#### 配置
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://填写mongodb地址/admin
      database: iotkit

  cache:
    cache-names: foo,bar
    caffeine:
      spec: maximumSize=5000,expireAfterAccess=120s

mqtt:
  url: tcp://填写mqtt连接地址

```
​

