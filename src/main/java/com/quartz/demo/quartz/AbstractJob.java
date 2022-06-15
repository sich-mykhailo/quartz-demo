package com.quartz.demo.quartz;

import org.quartz.JobDataMap;



public abstract class AbstractJob {
    protected abstract Class<? extends AbstractJobExecutor> getJobExecutor();
    protected abstract String getKey();
    protected abstract String getGroup();
    protected abstract String getDescription();
    protected JobDataMap createJobDataMap() {
        return new JobDataMap();
    }
}
