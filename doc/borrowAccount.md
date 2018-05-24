# 支付账号借用

## 点击观看[视频教程](https://github.com/Pay-Group/best-pay-sdk/blob/master/doc/use.md)

个人身份是不能申请微信或者支付宝支付的，必须企业资质才行。账号借用有风险，所以收取一定的费用，价格如下。

用户类型 | 借用时间 | 价格
-------|-------|------
普通用户 | 30天 | 49元
慕课网用户 | 30天 | 29元

> - 慕课网用户特指购买[《Spring Boot开发的微信点餐系统》](http://coding.imooc.com/class/117.html)这门课的用户
> - 账号只限用于学习开发使用, 一经发现用于非法使用, 立即收回, 费用不予退还
> - 付款后,24小时内提供账号,超过24小时请邮件再联系我

## 如何借用？
填写表单 https://www.wenjuan.in/s/NjiiQje/

## 请求示意图

```sequence
支付授权目录 -> 你的外网 -> 你的电脑
```

支付授权目录
http://sell.springboot.cn/sell/pay?openid=xxxxxxxxx

你的外网
http://xxx.natapp.cc/pay?openid=xxxxxxxxx

你的电脑
http://127.0.0.1:8080/pay?openid=xxxxxxxxxx

#### 付款码

![29](http://app-all.b0.upaiyun.com/29.jpg!300)


![49](http://app-all.b0.upaiyun.com/49.jpg!300)





