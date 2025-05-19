package com.xqj.nutaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.MessageAggregator;

/**
 * 自定义日志 Advisor
 * 打印 info 级别日志、只输出单次用户提示词和 AI 回复的文本
 */
@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private AdvisedRequest before(AdvisedRequest request) {
        log.info("AI Request: {}", request.userText());
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 1. 处理请求（前置处理）
        advisedRequest = before(advisedRequest);

        // 2. 调用链中的下一个Advisor
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

        // 3. 处理响应（后置处理）
        observeAfter(advisedResponse);

        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        // 1. 处理请求
        advisedRequest = before(advisedRequest);

        // 2. 调用链中的下一个Advisor并处理流式响应
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

        // 3. 根据MessageAggregator工具类将Flux响应聚合成单个AdvisedResponses
        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }

}