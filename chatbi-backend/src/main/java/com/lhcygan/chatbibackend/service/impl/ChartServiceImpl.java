package com.lhcygan.chatbibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhcygan.chatbibackend.model.entity.Chart;
import com.lhcygan.chatbibackend.service.ChartService;
import com.lhcygan.chatbibackend.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author autoe
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-02-07 15:32:36
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




