package com.taf.executor;

import rx.Scheduler;

public interface PostExecutionThread {
    Scheduler getScheduler();
}
