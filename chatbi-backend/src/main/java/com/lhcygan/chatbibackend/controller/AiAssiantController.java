package com.lhcygan.chatbibackend.controller;

import com.lhcygan.chatbibackend.common.BaseResponse;
import com.lhcygan.chatbibackend.common.ErrorCode;
import com.lhcygan.chatbibackend.common.ResultUtils;
import com.lhcygan.chatbibackend.exception.BusinessException;
import com.lhcygan.chatbibackend.exception.ThrowUtils;
import com.lhcygan.chatbibackend.manager.AiManager;
import com.lhcygan.chatbibackend.manager.RedisLimiterManager;
import com.lhcygan.chatbibackend.model.dto.aiassistant.GenChatByAiRequest;
import com.lhcygan.chatbibackend.model.entity.AiAssistant;
import com.lhcygan.chatbibackend.model.entity.User;
import com.lhcygan.chatbibackend.model.enums.ExecStatusEnum;
import com.lhcygan.chatbibackend.service.AiAssistantService;
import com.lhcygan.chatbibackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/aiAssistant")
@Slf4j
@RestController
public class AiAssiantController {

    @Resource
    private UserService userService;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private AiManager aiManager;

    @Resource
    private AiAssistantService aiAssistantService;

    @PostMapping("/chat")
    public BaseResponse<?> aiAssistant(@RequestBody GenChatByAiRequest genChatByAiRequest, HttpServletRequest request) {
        String questionGoal = genChatByAiRequest.getQuestionGoal();
        String questionName = genChatByAiRequest.getQuestionName();
        String questionType = genChatByAiRequest.getQuestionType();


        User loginUser = userService.getLoginUser(request);
        // 校验
        if (StringUtils.isBlank(questionName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "问题名称为空");
        }

        if (ObjectUtils.isEmpty(questionType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "问题类型为空");
        }

        if (StringUtils.isBlank(questionGoal)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "问题分析目标为空");
        }

        // 用戶限流
        redisLimiterManager.doRateLimit("Ai_Rate_" + loginUser.getId());

        String result = aiManager.doChat(questionGoal);

        AiAssistant aiAssistant = new AiAssistant();
        aiAssistant.setQuestionName(questionName);
        aiAssistant.setQuestionGoal(questionGoal);
        aiAssistant.setQuestionResult(result);
        aiAssistant.setQuestionType(questionType);
        aiAssistant.setQuestionStatus(ExecStatusEnum.SUCCEED.getValue());
        aiAssistant.setUserId(loginUser.getId());
        boolean save = aiAssistantService.save(aiAssistant);
        ThrowUtils.throwIf(!save, ErrorCode.PARAMS_ERROR, "ai对话保存失败");

        return ResultUtils.success(aiAssistant);
    }
}
