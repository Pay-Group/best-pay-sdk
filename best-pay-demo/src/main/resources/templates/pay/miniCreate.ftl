<script>
    wx.requestPayment(
        {
            'timeStamp': '${payResponse.timeStamp}',
            'nonceStr': '${payResponse.nonceStr}',
            'package': '${payResponse.packAge}',
            'signType': 'MD5',
            'paySign': '${payResponse.paySign}',
            'success':function(res){alert('支付成功')},
            'fail':function(res){alert('支付失败')},
            'complete':function(res){alert('完成')}
        })
</script>