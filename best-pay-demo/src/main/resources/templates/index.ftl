<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
        <link href="/css/index.css" rel="stylesheet">
    </head>

    <body>
        <div class="container">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <div class="jumbotron">
                        <h1>支付演示</h1>
                        <p>微信支付官方开发文档：<a target="_blank" href="https://pay.weixin.qq.com/wiki/doc/api/index.html">https://pay.weixin.qq.com/wiki/doc/api/index.html</a></p>
                        <p>支付宝官方开发文档：<a target="_blank" href="https://docs.open.alipay.com/catalog">https://docs.open.alipay.com/catalog</a></p>
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
                        <div class="form-group">
                            <label for="amount">支付方式</label>
                            <select class="form-control" id="payType">
                                <option value="ALIPAY_PC">支付宝PC</option>
                                <option value="WXPAY_NATIVE">微信Native支付</option>
                                <option value="WXPAY_MWEB">微信H5支付</option>
                                <option value="WXPAY_MP">微信公众号支付</option>
                                <option value="WXPAY_MINI">微信小程序支付</option>
                                <option value="WXPAY_APP">微信APP支付</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="openid">openid</label>
                            <input type="text" class="form-control" id="openid" name="openid"/>
                        </div>
                        <button type="submit" onclick="" class="btn btn-default">提交</button>
                    </form>
                    <h5>响应结果</h5>
                    <pre id="response"></pre>
                    <h5>结果处理</h5>
                    <div id="result"></div>
                </div>

            </div>
        </div>

        <footer class="bs-docs-footer">
            <div class="container">
                <p>获取本站源码，请访问<a href="https://github.com/Pay-Group/best-pay-sdk" target="_blank">best-pay-sdk</a></p>
            </div>
        </footer>
    </body>

    <script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/js/bootstrap.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
    <script>
        $(function () {
            genOrderId()

            //选择支付方式
            $("#payType").change(function (e) {
                genOrderId()
                $("#response").html('')
                $('#result').html('')
                $("#openid").val('')

                var payType = $("#payType").val()
                //为公众号和小程序支付设置openid
                if (payType == 'WXPAY_MINI') {
                    $("#openid").val('o_KuH5GRKRY4kn7dmfCpvZBtRRgM')
                }else if (payType == 'WXPAY_MP') {
                    $("#openid").val('oTgZpwZ9zPqleNgw2vshBqZQRG60')
                }
            })

            $("form").submit(function(e){
                var data = {
                    orderId: $("#orderId").val(),
                    amount: $("#amount").val(),
                    payType: $("#payType").val(),
                    openid: $("#openid").val()
                };
                $.ajax({
                    type:'get',
                    url:'/pay',
                    data:data,
                    success:function(response){
                        //支付宝会跳转, 不要显示
                        if ($("#payType").val() != 'ALIPAY_PC') {
                            $('#response').html(JSON.stringify(response, null, "\t"))
                        }

                        //根据支付方式处理
                        switch($("#payType").val()) {
                            case 'WXPAY_MWEB':
                                $('#result').html('结果是 ' + response.mwebUrl + '<br />'
                                    + '由于微信H5支付会校验referer, 也就是商户平台申请h5支付时会填写的产品域名<br />'
                                    + '所以这里做了个重定向, 请复制链接<strong>用手机浏览器打开</strong><br />'
                                    + response.mwebUrl.replace('https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb', 'http://' + document.domain + '/wxpay_mweb_redirect'));
                                break
                            case 'WXPAY_NATIVE':
                                $('#result').qrcode(response.codeUrl)
                                break
                            case 'WXPAY_MP':
                            case 'WXPAY_MINI':
                                $('#result').html('小程序/公众号支付，需要openid参数，暂时无法体验发起支付')
                                break
                            case 'WXPAY_APP':
                                $('#result').html('app支付需由android/ios端发起')
                                break
                            case 'ALIPAY_PC':
                                console.log(response.body)
                                $('#result').html(response.body)
                                break
                        }
                    },
                    error:function(response){
                        $('#response').text(JSON.stringify(response, null, "\t"))
                    }
                });
                return false;
            });

            //随机生成订单号
            function genOrderId() {
                $('#orderId').val(new Date().getTime() + String(Math.floor(Math.random() * (999999 - 100000 + 1) + 100000)));
            }
        });
    </script>
</html>