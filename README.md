# Easy-SpringBoot-CLI

#### 介绍
一个简易的SpringBoot框架脚手架，具备SpringBoot服务器应该具备的小模块，不包含其他主线模块以及花里胡哨模块。

#### 集成内容

| 功能模块            |
| --------------- |
| web server      |
| 全局异常捕获          |
| 跨域处理            |
| servlet接收流重复读封装 |
| 数据库持久化          |
| 请求日志记录          |
| 线程池工具           |
| 接口数据加密          |
| 统一响应体封装         |

#### 使用说明

1.  接口加解密
    加密规则：随机生成AES密钥对请求体进行数据加密，再对AES密钥进行RSA非对称加密，加密用公钥，解密用私钥。(注：因CryptoJs的AES默认加解密算法为mode:CBC,padding:Pkcs7,所以该模块也统一该算法)
    只需在配置文件配置是否启用接口加解密功能，开启请求加密则客户端只需在请求头中加入**Encrypt-key** 属性 把AES密钥加密传到服务端进行解密。响应加密相同、取出响应头**Encrypt-key**数据逐步解密就好。
    配置项：
```yaml
api-enc:  
  # 是否开启请求加密  
  requestEnable: true  
  # 是否开启请求加密  
  respondEnable: true  
  # 请求头中携带的加密字段 AES 密钥加密后密文  
  keyHeader: Encrypt-Key  
  # RSA加解密密钥-替换为自己项目的公私密钥  
  publicKey: ...
  privateKey: ...
```