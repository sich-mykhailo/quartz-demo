package com.quartz.demo.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;


@Slf4j
public abstract class AbstractJobExecutor implements Job {

    protected abstract void executeJob(String key, JobDataMap jobDataMap);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        executeJob(getKey(name), jobDataMap);
    }

    private String getKey(String name) {
        try {
            return name;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
