package com.lhcygan.chatbibackend.model.document;

import cn.hutool.core.builder.EqualsBuilder;
import cn.hutool.core.builder.HashCodeBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document("chart")
@Data
public class ChartDoc implements Serializable {

    public static final Integer DEFAULT_VERSION = 1;

    public static final String COLLECTION_NAME = "chart";

    /**
     * MongoDB自动生成的唯一ID
     */
    @Id
    private String id;

    /**
     * 图表id
     */
    @Indexed
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chartId;

    /**
     * 版本号 : 用户可以更改提交的数据, 并且重新生成新的版本的图表
     */
    private Integer version;

    /**
     * 用户ID
     */
    @Indexed
    private Long userId;

    /**
     * 表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * wait,running,succeed,failed
     */
    private String status;

    /**
     * 执行信息
     */
    private String execMessage;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 生成的图表数据
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Chart{" +
                "id='" + id + '\'' +
                ", chartId=" + chartId +
                ", version=" + version +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", goal='" + goal + '\'' +
                ", status='" + status + '\'' +
                ", execMessage='" + execMessage + '\'' +
                ", chartType='" + chartType + '\'' +
                ", genChart='" + genChart + '\'' +
                ", genResult='" + genResult + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ChartDoc chart = (ChartDoc) o;

        return new EqualsBuilder().append(id, chart.id).append(chartId, chart.chartId).append(version, chart.version).append(userId, chart.userId).append(name, chart.name).append(goal, chart.goal).append(status, chart.status).append(execMessage, chart.execMessage).append(chartType, chart.chartType).append(genChart, chart.genChart).append(genResult, chart.genResult).append(createTime, chart.createTime).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(chartId).append(version).append(userId).append(name).append(goal).append(status).append(execMessage).append(chartType).append(genChart).append(genResult).append(createTime).toHashCode();
    }
}
