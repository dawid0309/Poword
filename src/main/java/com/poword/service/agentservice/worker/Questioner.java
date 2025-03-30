package com.poword.service.agentservice.worker;

import com.poword.model.request.NewsDTO;
import com.poword.service.agentservice.Handler;

public class Questioner implements Handler {
    private Handler nextHandler;
    private Handler previousHandler;

    @Override
    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void setPreviousHandler(Handler handler) {
        this.previousHandler = handler;
    }

    @Override
    public void handleRequest(NewsDTO request) {
        return; 
    }
}
