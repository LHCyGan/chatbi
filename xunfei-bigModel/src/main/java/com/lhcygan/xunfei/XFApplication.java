package com.lhcygan.xunfei;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: LHCyGan
 * @CreateTime: 2024-02-01  14:11
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@SpringBootApplication
public class XFApplication {
    public static void main(String[] args) {
        log.info("这是新的XFApplication");
        SpringApplication.run(XFApplication.class, args);
    }
}
