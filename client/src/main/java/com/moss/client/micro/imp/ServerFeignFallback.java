package com.moss.client.micro.imp;

import com.moss.client.micro.ServerFeign;
import org.springframework.stereotype.Component;

/**
 * @author Liuzc
 * @date 2018-07-05
 */
@Component
public class ServerFeignFallback implements ServerFeign {

    @Override
    public String api(String req) {
        return "{\"status\":\"02010027\"}";
    }
}