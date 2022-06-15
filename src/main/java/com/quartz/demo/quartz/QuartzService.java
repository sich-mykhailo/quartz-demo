package com.quartz.demo.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;

    public void terminateJob(String key, String group) {
        try {
            scheduler.deleteJob(JobKey.jobKey(key, group));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", key, e);
        }
    }

   public void pauseJob(String key, String group) {
        try {
            scheduler.pauseJob(JobKey.jobKey(key, group));
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", key, e);
        }
    }

    public void resumeJob(String key, String group) {
        try {
            scheduler.resumeJob(JobKey.jobKey(key, group));
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", key, e);
        }
    }

   public void startNow(String key, String group) {
        try {
            scheduler.triggerJob(new JobKey(key, group));
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", key, e);
        }
    }

    public  <T extends AbstractJob, M extends Trigger> void rescheduleJob(String key, M trigger) {
        try {
            scheduler.rescheduleJob(TriggerKey.triggerKey(key), trigger);
        } catch (SchedulerException e) {
            log.error("Failed to reschedule new job - {}", key, e);
        }
    }

    public <T extends AbstractJob, M extends Trigger> void scheduleJob(T job, M trigger) {
        try {
            JobDetail jobDetail = createJob(job.getJobExecutor(), job.getKey(), job.getGroup(), job.getDescription(), job.createJobDataMap());
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("Failed to schedule new job - {}", job.getKey(), e);
        }
    }

    private JobDetail createJob(Class<? extends Job> jobClass, String identity, String group, String description, JobDataMap jobDataMap) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(identity, group)
                .withDescription(description)
                .usingJobData(jobDataMap)
                .build();
    }
}
