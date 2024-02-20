package com.lhcygan.chatbibackend.mapper;

import com.lhcygan.chatbibackend.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
* @author autoe
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-02-07 15:32:36
* @Entity generator.domain.Chart
*/
@Mapper
public interface ChartMapper extends BaseMapper<Chart> {

    @MapKey("")
    List<Map<String, Object>> queryChartData(String querySql);
}




