package com.lhcygan.chatbibackend.utils;

import com.github.rholder.retry.*;
import com.lhcygan.chatbibackend.common.ErrorCode;
import com.lhcygan.chatbibackend.exception.BusinessException;
import com.google.common.base.Predicates;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RetryUtils {

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_INTERVAL = 1000; // 1 second

    public static<T> T retry(Callable<T> callable) throws BusinessException {

        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));

        Retryer<T> retryer = RetryerBuilder.<T>newBuilder()
                .retryIfResult(Predicates.isNull())
                .retryIfExceptionOfType(Exception.class)
                .withStopStrategy(StopStrategies.stopAfterAttempt(MAX_RETRIES))
                .withWaitStrategy(WaitStrategies.fixedWait(RETRY_INTERVAL, TimeUnit.MILLISECONDS))
                .build();

        try {
            return retryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            Throwable rootCause = Throwables.getRootCause(e);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Error occurred: " + rootCause.getMessage());
        } finally {
            executorService.shutdown();
        }
    }
}
