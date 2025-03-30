package com.poword.service.agentservice;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.poword.model.request.NewsDTO;

public interface Handler {
    void setNextHandler(Handler handler);  // 设置下一个处理器
    void setPreviousHandler(Handler handler);  // 设置前一个处理器
    void handleRequest(NewsDTO request) throws NoApiKeyException, InputRequiredException;  // 处理请求
}
