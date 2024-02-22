package com.lhcygan.chatbibackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lhcygan.chatbibackend.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author autoe
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-02-07 15:32:36
*/
public interface ChartService extends IService<Chart> {

    public List<Chart> listChartByCacheService() throws JsonProcessingException;
}
