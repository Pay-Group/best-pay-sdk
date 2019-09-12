<script src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery.qrcode.min.js"></script>

<script>
    jQuery(function(){
        jQuery('#qrcode').qrcode("${payUrl}");
    })
</script>

<div id="qrcode"></div>
