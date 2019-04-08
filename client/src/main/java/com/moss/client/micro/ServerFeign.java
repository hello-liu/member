package com.moss.client.micro;

import com.moss.client.micro.imp.ServerFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "moss-server", path = "/controller", fallback = ServerFeignFallback.class)

public interface ServerFeign {


    @RequestMapping(value = "/api")
    String api(@RequestBody String message);

}