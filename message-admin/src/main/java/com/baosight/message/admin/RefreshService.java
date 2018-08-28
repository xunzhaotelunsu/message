package com.baosight.message.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RefreshService {

    @Value("${server.refresh.email}")
    String[] emails;

    private RestTemplate getRestTemplate(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    /**
     * 变更邮件发送服务器后刷新message-service中的holder
     */
    @Async
    public void refreshEmailServers(){
        for(String email: emails){
            RestTemplate restTemplate = getRestTemplate();
            restTemplate.postForObject(email,null, String.class);
        }
    }
}
