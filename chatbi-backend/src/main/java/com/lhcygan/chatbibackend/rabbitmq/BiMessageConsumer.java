package com.lhcygan.chatbibackend.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lhcygan.chatbibackend.common.ErrorCode;
import com.lhcygan.chatbibackend.constant.BiMqConstant;
import com.lhcygan.chatbibackend.exception.BusinessException;
import com.lhcygan.chatbibackend.manager.AiManager;
import com.lhcygan.chatbibackend.model.entity.Chart;
import com.lhcygan.chatbibackend.model.enums.ExecStatusEnum;
import com.lhcygan.chatbibackend.service.ChartService;
import com.lhcygan.chatbibackend.utils.JsonUtils;
import com.lhcygan.chatbibackend.utils.RetryUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.prefs.BackingStoreException;

@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    /**
     * 指定程序监听的消息队列和确认机制
     *
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long  deliveryTag) {
        log.info("receiveMessage message={}", message);
        if (StringUtils.isBlank(message)) {
            // 消息为空，则拒绝掉消息
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接收到的消息为空");
        }

        // 获取图表id
        long chartId = Long.parseLong(message);
        // 从数据库取
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            // 将消息拒绝
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图表为空");
        }

        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(ExecStatusEnum.RUNNING.getValue());
        boolean updateChartById = chartService.updateById(updateChart);
        if (!updateChartById) {
           channel.basicNack(deliveryTag, false, false);
           handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
        }

        // 调用AI
//        String result = aiManager.doChat(buildUserInput(chart));
//        JSONObject resultJson = JSON.parseObject(JSON.parseObject(result).get("data").toString());
//        if (result.length() < 10) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
//        }
        String result = RetryUtils.retry(() -> aiManager.doChat(buildUserInput(chart)));
        JSONObject resultJson = JSON.parseObject(JSON.parseObject(result).get("data").toString());
        if (result.length() < 10) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        }
        String genChart = resultJson.get("echartsCode").toString();
        genChart = JsonUtils.addQuotesToKeys(genChart);

        String genResult = resultJson.get("conclusion").toString()
                .replace("\\n", "").replace("\\\"", "\"").replace("\"}", "");
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        
        updateChartResult.setStatus(ExecStatusEnum.SUCCEED.getValue());
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }

        channel.basicAck(deliveryTag, false);
    }

    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(ExecStatusEnum.FAILED.getValue());
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }
}
