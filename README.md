#best pay sdk -- 可能是目前最好的支付sdk
#### 支付方式支持
1. 支付宝（app, pc, 扫码等）
2. 微信（app, h5, 扫码等）

#### 功能支持
1. 支付
2. 退款（自动秒退）

####特别注意
1. 本sdk要求的最低的jdk版本是8(纳尼, 你还在用7? 难道你不知道9块出来了吗)
2. 支付时的编码只支持utf-8
3. 支付宝的签名方式只支持RSA和RSA2, 不支持MD5(签名的功能sdk都帮实现了, 你完全不用担心这点)
4. 如果想贡献代码，请阅读【代码贡献指南】


#### 交流方式
1. qq群（590730230）。目前项目处于刚刚起步开发阶段，欢迎有兴趣的朋友加群共同开发。


#### Maven最新版本
```
<dependency>
    <groupId>com.github.lly835</groupId>
    <artifactId>best-pay-sdk</artifactId>
    <version>1.0.2</version>
</dependency>
```
