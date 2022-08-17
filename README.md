# 可能是目前最好的支付sdk
支付宝、微信支付方式多样, 开发繁琐, 使用该sdk, 只需10行代码, 帮你搞定！

示例 https://github.com/Pay-Group/best-pay-demo

## 版本更新
#### 1.3.7(2022.8.18)
    1. 新增支付app
    2. 修复支付宝wap回调参数的bug

#### 1.3.3(2020.5.23)
    2. 修正支付沙盒

#### 1.3.1(2019.11.28)
    1. 修复：微信APP支付签名问题
    2. 新增支付宝WAP支付
    3. PayRequest增加参数returnUrl, 优先级高于PayConfig.returnUrl
    4. 修复：查询订单，微信订单未支付的情况下timeEnd会返回空

更多更新记录 https://github.com/Pay-Group/best-pay-sdk/releases

## 文档
1. [使用文档](https://github.com/Pay-Group/best-pay-sdk/blob/develop/doc/use.md)

## 特别注意
1. 要求JDK8+
2. IDE 需安装 lombok 插件
3. 如果想贡献代码，请阅读[代码贡献指南](https://github.com/Pay-Group/best-pay-sdk/blob/master/doc/CONTRIBUTION.md)

## 在线体验
1. http://best-pay.springboot.cn
2. 微信公众号支付请扫码体验
    
    ![师兄干货](http://img.mukewang.com/5db958ec0001b67d02580258.jpg)

## 交流方式
1. qq群（590730230）加群暗号：best。目前项目处于刚刚起步开发阶段，欢迎有兴趣的朋友加群共同开发。


## Maven最新版本
```xml
<!-- https://mvnrepository.com/artifact/cn.springboot/best-pay-sdk -->
<dependency>
    <groupId>cn.springboot</groupId>
    <artifactId>best-pay-sdk</artifactId>
    <version>请使用最新版</version>
</dependency>
```




