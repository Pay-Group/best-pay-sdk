<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body>
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="jumbotron">
                        <h1>支付演示</h1>
                    </div>
                    <form role="form" id="payForm">
                        <div class="form-group">
                            <label for="orderId">订单号</label>
                            <input type="text" class="form-control" id="orderId" name="orderId"/>
                        </div>
                        <div class="form-group">
                            <label for="amount">金额</label>
                            <input type="text" value="0.1" class="form-control" id="amount" name="amount"/>
                        </div>
                        <div class="radio">
                            <label>
                                <input type="radio" name="payType" id="WXPAY_MWEB" value="WXPAY_MWEB" checked>
                                微信H5支付
                            </label>
                        </div>
<#--                        <div class="radio">-->
<#--                            <label>-->
<#--                                <input type="radio" name="payType" id="WXPAY_NATIVE" value="WXPAY_NATIVE">-->
<#--                                微信Native支付-->
<#--                            </label>-->
<#--                        </div>-->
                        <button type="submit" onclick="" class="btn btn-default">提交</button>
                    </form>
                    <h5>响应结果</h5>
                    <div id="result"></div>
                </div>
            </div>
        </div>
    </body>

    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            //随机生成订单号
            $('#orderId').val(new Date().getTime() + String(Math.floor(Math.random() * (999999 - 100000 + 1) + 100000)));

            var data = $("#payForm").serialize();
            $("form").submit(function(e){
                $.ajax({
                    type:'get',
                    url:'/pay',
                    data:data,
                    success:function(result){
                        $('#result').html('结果是 ' + result.mwebUrl + '<br />'
                            + '由于微信H5支付会校验referer, 也就是商户平台申请h5支付时会填写的产品域名<br />'
                            + '所以这里做了个重定向, 请复制链接<strong>用手机浏览器打开</strong><br />'
                            + result.mwebUrl.replace('https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb', 'http://' + document.domain + '/wxpay_mweb_redirect'))
                    },
                    error:function(result){
                        $('#result').text(JSON.stringify(result))
                    }
                });
                return false;
            });
        });
    </script>
</html>