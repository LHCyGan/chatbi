package com.lhcygan.xunfei.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhcygan.xunfei.bean.NettyGroup;
import com.lhcygan.xunfei.bean.ResultBean;
import com.lhcygan.xunfei.service.PushService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: LHCyGan
 * @CreateTime: 2024-02-01  15:14
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    private PushService pushService;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded被调用,{}", JSON.toJSONString(ctx));
        //todo 添加校验功能，校验合法后添加到group中

        // 添加到channelGroup 通道组
        NettyGroup.getChannelGroup().add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务器收到消息：{}", msg.text());
        // 获取用户ID,关联channel
        JSONObject jsonObject = JSON.parseObject(msg.text());
        String channelId = jsonObject.getString("uid");

        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        //String channelId = CharUtil.generateStr(uid);
        NettyGroup.getUserChannelMap().put(channelId, ctx.channel());
        boolean containsKey = NettyGroup.getUserChannelMap().containsKey(channelId);
        //通道已存在，请求信息返回
        if (containsKey) {
            //接收消息格式{"uid":"123456","text":"中华人民共和国成立时间"}
            String text = jsonObject.getString("text");
            //请求大模型服务器，获取结果
            ResultBean resultBean = pushService.pushMessageToXFServer(channelId, text);
            String data = (String) resultBean.getData();
            //推送
            pushService.pushToOne(channelId, JSON.toJSONString(data));
        } else {
            ctx.channel().attr(key).setIfAbsent(channelId);
            log.info("连接通道id:{}", channelId);
            // 回复消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(ResultBean.success(channelId))));
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved被调用,{}", JSON.toJSONString(ctx));
        // 删除通道
        NettyGroup.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("通道异常：{}", cause.getMessage());
        // 删除通道
        NettyGroup.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }

    private void removeUserId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(key).get();
        NettyGroup.getUserChannelMap().remove(userId);
    }
}


