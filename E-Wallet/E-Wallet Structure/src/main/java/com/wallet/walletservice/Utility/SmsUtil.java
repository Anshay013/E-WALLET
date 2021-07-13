package com.wallet.walletservice.Utility;

import java.net.URISyntaxException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SmsUtil {
    private static final Logger logger = LoggerFactory.getLogger(SmsUtil.class);
    void sendSms()throws URISyntaxException, UnirestException {
        HttpResponse response = Unirest.get("https://www.fast2sms.com/dev/bulk?authorization=YOUR_KEY&sender_id=F2SMS&message=This%20is%20a%20test%20message&language=english&route=p&numbers=********")
                .header("cache-control", "no-cache")
                .asString();
     
        logger.info(response.getBody().toString());
        logger.info(response.getHeaders().toString());
    }
}
   // to send SMS to some user we call fast2sms API
