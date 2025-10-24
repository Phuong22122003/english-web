package com.english.learning_service.httpclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "agent",url = "${app.services.user}")
public interface UserClient {

}
