package com.gianvittorio.concurrency.lesson3;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerPublisher extends SubmissionPublisher<Integer> {
    private ScheduledExecutorService executorService;

    private ScheduledFuture<?> future;

    private final AtomicInteger count = new AtomicInteger(0);

    public IntegerPublisher() {
        super(Executors.newFixedThreadPool(2), Flow.defaultBufferSize());

        executorService = Executors.newScheduledThreadPool(1);

        future = executorService.scheduleAtFixedRate(
                () -> submit(count.incrementAndGet()),
                500,
                500,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void close() {
        future.cancel(false);

        executorService.shutdown();
    }
}
