package com.lhcygan.xunfei.listener;

import com.alibaba.fastjson.JSON;
import com.lhcygan.xunfei.bean.JsonParse;
import com.lhcygan.xunfei.bean.Text;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import java.io.IOException;
import java.util.*;

/**
 * @Author: LHCyGan
 * @CreateTime: 2023-10-18  10:17
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public class XFWebSocketListener extends WebSocketListener {

    //断开websocket标志位
    private boolean wsCloseFlag = false;

    //语句组装buffer，将大模型返回结果全部接收，在组装成一句话返回
    private StringBuilder answer = new StringBuilder();

    public String getAnswer() {
        return answer.toString();
    }

    public boolean isWsCloseFlag() {
        return wsCloseFlag;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("大模型服务器连接成功！");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        JsonParse myJsonParse = JSON.parseObject(text, JsonParse.class);
        log.info("myJsonParse:{}", JSON.toJSONString(myJsonParse));
        if (myJsonParse.getHeader().getCode() != 0) {
            log.error("发生错误，错误信息为:{}", JSON.toJSONString(myJsonParse.getHeader()));
            this.answer.append("大模型响应异常，请联系管理员");
            // 关闭连接标识
            wsCloseFlag = true;
            return;
        }
        List<Text> textList = myJsonParse.getPayload().getChoices().getText();
        for (Text temp : textList) {
            log.info("返回结果信息为：【{}】", JSON.toJSONString(temp));
            this.answer.append(temp.getContent());
        }
        log.info("result:{}", this.answer.toString());
        if (myJsonParse.getHeader().getStatus() == 2) {
            wsCloseFlag = true;
            //todo 将问答信息入库进行记录，可自行实现
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                log.error("onFailure body:{}", response.body().string());
                if (101 != code) {
                    log.error("讯飞星火大模型连接异常");
                }
            }
        } catch (IOException e) {
            log.error("IO异常：{}", e);
        }
    }
}
