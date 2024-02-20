package com.lhcygan.chatbibackend.model.dto.aiassistant;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
public class GenChatByAiRequest implements Serializable {

    /**
     * 分析目标
     */
    private String questionGoal;

    /**
     * 图表名称
     */
    private String questionName;

    /**
     * 图表类型
     */
    private String questionType;

    private static final long serialVersionUID = 1L;
}