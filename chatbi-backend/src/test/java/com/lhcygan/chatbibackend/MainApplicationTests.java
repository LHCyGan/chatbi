package com.lhcygan.chatbibackend;

import cn.hutool.http.HttpUtil;
import com.lhcygan.chatbibackend.mapper.ChartMapper;
import com.lhcygan.xunfei.controller.XFMessageController;
import com.lhcygan.xunfei.service.PushService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 主类测试

 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    ChartMapper chartMapper;

    @Test
    void testPushMessage() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", "lhcygan");
        params.put("text", "分析需求: 分析网站用户的增长情况 原始数据 日期，用户数 1号，10 2号，20， 3号，30");
        String answer = HttpUtil.get("http://localhost:9000/xfModel/test", params);
        System.out.println(answer);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void queryChartData() {
        String charId = "13";
        String querySql = String.format("select * from chart_%s", charId);
        List<Map<String, Object>> resultData = chartMapper.queryChartData(querySql);
        System.out.println(resultData);
    }

}
