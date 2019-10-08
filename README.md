<p align="center">
elasticsearch-netty 基于hmtool的扩展工具包
</p>
<p align="center">
-- 主页：<a href="http://mhuang.tech/hmtool-ext-netty">http://mhuang.tech/hmtool-ext-netty</a>  --
</p>
<p align="center">
    -- QQ群①:<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=6703688b236038908f6c89b732758d00104b336a3a97bb511048d6fdc674ca01"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="hmtool官方交流群①" title="hmtool官方交流群①"></a>
</p>
---------------------------------------------------------------------------------------------------------------------------------------------------------

## 简介
hmtool-ext-netty 是一个基于netty和hmtool-core封装的便捷工具包、简单几行代码即可对netty进行操作
协议采用大端方式传输
协议  |标识头|消息头|消息体|校验码|标识尾|

标识头 0x7c 表示。一个字节
消息头 
    版本号|2个字节
    消息id|2个字节
    数据长度|4个字节
    接入码|8个字节
消息体 自定义对称性加密算法
校验码 消息体每个字节的异或|1个字节
标识尾 0x7c表示 1个字节

目前包含了有加密、解密、通过netty实现粘包、解包、心跳等操作

## 安装

### MAVEN
在pom.xml中加入
```
    <dependency>
        <groupId>tech.mhuang</groupId>
        <artifactId>hmtool-ext-netty</artifactId>
        <version>${last.version}</version>
    </dependency>
```
### 非MAVEN
下载任意链接
- [Maven中央库1](https://repo1.maven.org/maven2/tech/mhuang/hmtool-ext-netty/)
- [Maven中央库2](http://repo2.maven.org/maven2/tech/mhuang/hmtool-ext-netty/)
> 注意
> hmtool只支持jdk1.8以上的版本