package com.lhcygan.chatbibackend.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhcygan.chatbibackend.common.ErrorCode;
import com.lhcygan.chatbibackend.common.ResultUtils;
import com.lhcygan.chatbibackend.exception.BusinessException;
import com.lhcygan.chatbibackend.model.entity.Chart;
import com.lhcygan.chatbibackend.service.ChartService;
import com.lhcygan.chatbibackend.mapper.ChartMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.BasicUserPrincipal;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.lhcygan.chatbibackend.constant.RedisConstant.*;

/**
* @author autoe
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-02-07 15:32:36
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ChartMapper chartMapper;

    public List<Chart> listChartByCacheService() throws JsonProcessingException {
        //先从缓存中查询key是否存在,从缓存中获取key值
        Set<String> keys = stringRedisTemplate.keys(CHART_LIST_CACHE_KEY);
        //判断缓存是否为空，若不为空，则从缓存中获取key中的chart表json格式数据
        //判断keys和CHART_LIST_CACHE_KEY是否相等
        boolean equals = keys.contains(CHART_LIST_CACHE_KEY);
        if (equals) {
            //从缓存中获取key中的chart表json格式数据
            String chartJson = stringRedisTemplate.opsForValue().get(CHART_LIST_CACHE_KEY);

            //用于将JSON数据转换为Java对象
            List<Chart> list = JSONUtil.toList(chartJson, Chart.class);

            return list;
        }

        //数据库中查询chartList数据
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        List<Chart> chartList = chartMapper.selectList(queryWrapper);
        if (chartList == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //将数据库中的数据进行缓存
        //1、先将Java对象转换成json格式
        ObjectMapper objectMapper = new ObjectMapper();
        String cacheChartJson = objectMapper.writeValueAsString(chartList);
        if (cacheChartJson == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        stringRedisTemplate.opsForValue().set(CHART_LIST_CACHE_KEY, cacheChartJson, CHART_LIST_CACHE_TIME, TimeUnit.MINUTES);
        return chartList;
    }

    public Chart getChartByCache(long id) throws JsonProcessingException {

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String cacheId = CHART_CACHE_ID + id;
        String cacheChart = stringRedisTemplate.opsForValue().get(cacheId);
        if (StringUtils.isNotBlank(cacheChart)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Chart chart = objectMapper.readValue(cacheChart, Chart.class);
            return chart;
        }

        Chart chart = this.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String chartJson = JSONUtil.toJsonStr(chart);
        stringRedisTemplate.opsForValue().set(cacheId, chartJson, CHART_CACHE_TIME, TimeUnit.MINUTES);
        return chart;
    }

}




