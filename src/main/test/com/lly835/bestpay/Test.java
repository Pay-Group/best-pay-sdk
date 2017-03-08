package com.lly835.bestpay;

import com.lly835.bestpay.config.AliDirectPayConfig;
import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;

public class Test {

    public static void main(String[] args) {
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCB+mccEqJAnUVDBXoTZbN7kySyOG5O2xh2Oj3PC3eVN3Tmq9bX8Q/0cPweTNOHQFXkOeLY8CpkNJqnXWTe2UmopL35+1M3ApX7v9Hee4ZYD1Dokv/SgMpqzH6iyqF7MK2FYdbtV49dmfH9X6bPHUjBl4At8GCaos3wswFGQmd4tN8+Kn0qCtFqTZpm/1OfDuqmi3vdZ8XO1/zeJdIfpWcDDHNFRx6R7KrxfNW4cHKLkTToNIyrzYGWvpFoM6GMH7aMOvYvnqcPSzDIrXEJOj0/+9F6IeVBzoB21Y8V9y6SnQLn9mosEh87qYBgeXpVbrorbGAypDmsi3bDtO2T6RFXAgMBAAECggEAI0TF67kmq6/BFoGK8W58OEDiCTYEwIL07Ue6c87hwOyOXPUt0+SGdsadsr8X+rA7XjVS15i7d/tKJ7XQ5CNDuG4TWpyWpOuftPMEyIDuIEDvrFwO7Jij1Dzu889V5+56dyHoRvimXzihkCX/Rl3k2xr33f9Gx0774J9B7kWgfAD4CTA3HXcZAwyeOfYQd+Y8dRNluu/9MD/NzXM2DTUTNTKlv8yyHVLWXtr+r34owsV69I4ZDtoxIzEV+1XONmE+rXOZbjK0CnjUvnm92WQ64y3Yutgm4RhJQcqToesrpHJyHV1tddBCNX4Brq9gMfiWBAyigRizGiSBmBbTd7A6CQKBgQD8AAfANmJfhlMOQqiRr3pWOONilldmOkxmpXHBO5xgVApQwwBQ5DvVSSuswUAK/yNDK6re2KKe4KoZJGDudzxnrmdOJ9F7EZ3Xvdq5RE+n9YvmjpJtg5qmUigAOl/eon44e3Fk5sJLSBHwoND8xWRx0K+RmdOLWNJLn4b0GNWPAwKBgQCECo1R7IAO0RUW2exHvr1i/SCB/NQ53CKyzs7SDBUc0gVcNxEQtHUcZxlTT5iC1FyWQDcU5jlTH09RNAFRzoPKkuDfljAnWlvkuFQPJk12tVCxzVwbjIU2Qr0dH8IENRMi7ox+mEdZN35o2xEe5B1Xw177V8WqU8laaJFei8tKHQKBgQCngLWmNAALktOeRPybadKdgU7Tdy89Cj9Cc3I7iQ4WkYYRzKxnGG4VBX/8Yq/ZH2InAd2gJsthY+BsuducZrFGEY0lTB5X+Yu7nbzHLPrtn6+QXvBBzxp6t11TWXnkGIphrNvgv+oh9BPIqHyBqAtuA9LPqzoMA8w07CLeGavKVQKBgG+PJsVfxvY9ys6qwon/aq2W5f9NaNWV2y8tseof0Tqva131HJL7lLKxnOEZr+Zhm/RKPv+GYEqNeotGIBNJI2pk3F+r2fV1z4wX/NVr7CjumkYPFtIj0Gz+yB8yNM8vNILf/436BNYpH8FhzT5HR09ePwKmrtfwwH+FN8Uk4VY1AoGAIayW5s/meMd+xXhrcxbFUY8p841mlwwbRYYEwAj6LxpyhQF0Es8pC+maoCPDncHsXlZ76z0GL2KDCj+Coi/VWAoyv+4cQ95PGezdr87HGT1cJWJtjRdn2RM8CNpxznacnrs9cX/TpcLao5k7K0dnzJy6cEKiR6L9ZDmi2ZrkuQI=";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgfpnHBKiQJ1FQwV6E2Wze5MksjhuTtsYdjo9zwt3lTd05qvW1/EP9HD8HkzTh0BV5Dni2PAqZDSap11k3tlJqKS9+ftTNwKV+7/R3nuGWA9Q6JL/0oDKasx+osqhezCthWHW7VePXZnx/V+mzx1IwZeALfBgmqLN8LMBRkJneLTfPip9KgrRak2aZv9Tnw7qpot73WfFztf83iXSH6VnAwxzRUcekeyq8XzVuHByi5E06DSMq82Blr6RaDOhjB+2jDr2L56nD0swyK1xCTo9P/vReiHlQc6AdtWPFfcukp0C5/ZqLBIfO6mAYHl6VW66K2xgMqQ5rIt2w7Ttk+kRVwIDAQAB";

        AlipayConfig alipayConfig = new AlipayConfig("appId", privateKey, publicKey, "http://notifyUrl");
        AliDirectPayConfig aliDirectPayConfig = new AliDirectPayConfig("2088123456789012", "md5Secret", privateKey,
                publicKey, "http://notifyUrl", "http://returnUrl");
        BestPayService pay = new BestPayServiceImpl(alipayConfig, aliDirectPayConfig);
        PayRequest request = new PayRequest();
        request.setOrderId("orderId");
        request.setOrderAmount(1.1D);
        request.setOrderName("name");
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_APP);
        try {
            PayResponse response = pay.pay(request);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
