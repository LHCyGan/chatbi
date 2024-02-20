package com.lhcygan.chatbibackend.manager;

import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AiManager {

    private static final String biUrl = "http://localhost:9000/xfModel/test";

    public String doChat(String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", "lhcygan");
        params.put("text", text);
        String answer = HttpUtil.get(biUrl, params);

        return answer;
    }
}
