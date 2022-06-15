package com.quartz.demo.quartz;

import lombok.Builder;

@Builder
public class SimpleJob extends AbstractJob {
    private String key;
    private Class<? extends AbstractJobExecutor> executor;
    private String group;
    private String description;

    @Override
    protected Class<? extends AbstractJobExecutor> getJobExecutor() {
        return executor;
    }

    @Override
    protected String getKey() {
        return key;
    }

    @Override
    protected String getGroup() {
        return group;
    }

    @Override
    protected String getDescription() {
        return description;
    }
}
