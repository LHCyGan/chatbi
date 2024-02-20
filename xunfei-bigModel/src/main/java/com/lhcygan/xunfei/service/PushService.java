package com.lhcygan.xunfei.service;

import com.lhcygan.xunfei.bean.ResultBean;

public interface PushService {

    void pushToOne(String uid, String text);

    void pushToAll(String text);

    ResultBean pushMessageToXFServer(String uid, String text);
}
