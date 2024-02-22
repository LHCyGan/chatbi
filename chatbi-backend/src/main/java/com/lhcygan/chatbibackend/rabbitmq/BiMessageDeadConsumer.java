package com.lhcygan.chatbibackend.rabbitmq;

import com.lhcygan.chatbibackend.common.ErrorCode;
import com.lhcygan.chatbibackend.constant.BiMqConstant;
import com.lhcygan.chatbibackend.exception.BusinessException;
import com.lhcygan.chatbibackend.model.entity.Chart;
import com.lhcygan.chatbibackend.model.enums.ExecStatusEnum;
import com.lhcygan.chatbibackend.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 图表分析队列的死信队列
 */
@Component
@Slf4j
public class BiMessageDeadConsumer {

    @Resource
    private ChartService chartService;


    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_DEAD_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long  deliveryTag) {
        log.warn("接收到死信队列信息，receiveMessage={}=======================================",message);
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图表为空");
        }

        //修改表状态为执行中，执行成功修改为“已完成”；执行失败修改为“失败”
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(ExecStatusEnum.FAILED.getValue());
        boolean updataResult = chartService.updateById(updateChart);
        if (!updataResult) {
            handleChartUpdateError(chart.getId(),"更新图表执行状态失败");
            return;
        }
        //消息确认
        channel.basicAck(deliveryTag,false);
    }

    private void handleChartUpdateError(Long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setStatus(ExecStatusEnum.FAILED.getValue());
        updateChartResult.setId(chartId);
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult){
            log.error("更新图片失败状态失败"+chartId+","+execMessage);
        }
    }

}
