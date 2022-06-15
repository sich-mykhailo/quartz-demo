package com.quartz.demo.job;

import com.quartz.demo.component.JobScheduleCreator;
import com.quartz.demo.entity.SchedulerJobInfo;
import com.quartz.demo.quartz.QuartzService;
import com.quartz.demo.quartz.SimpleJob;
import com.quartz.demo.repository.SchedulerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SchedulerJobService {
    private final Scheduler scheduler;
    private final SchedulerRepository schedulerRepository;
    private final JobScheduleCreator scheduleCreator;
    private final QuartzService quartzService;

    public SchedulerMetaData getMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }

    public List<SchedulerJobInfo> getAllJobList() {
        return schedulerRepository.findAll();
    }

    public void deleteJob(SchedulerJobInfo jobInfo) {
        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        schedulerRepository.delete(getJobInfo);
        quartzService.terminateJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        log.info("DELETED");
    }

    public void pauseJob(SchedulerJobInfo jobInfo) {
        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        getJobInfo.setJobStatus("PAUSED");
        schedulerRepository.save(getJobInfo);
        quartzService.pauseJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        log.info("PAUSED");
    }

    public void resumeJob(SchedulerJobInfo jobInfo) {
        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        getJobInfo.setJobStatus("RESUMED");
        schedulerRepository.save(getJobInfo);
        quartzService.resumeJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        log.info("RESUMED");
    }

    public void startJobNow(SchedulerJobInfo jobInfo) {
        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        getJobInfo.setJobStatus("SCHEDULED & STARTED");
        schedulerRepository.save(getJobInfo);
        quartzService.startNow(jobInfo.getJobName(), jobInfo.getJobGroup());
    }

    public void saveUpdate(SchedulerJobInfo scheduleJob) {
        if (scheduleJob.getCronExpression().length() > 0) {
            scheduleJob.setJobClass(SampleCronJobExecutor.class.getName());
            scheduleJob.setCronJob(true);
        } else {
            scheduleJob.setJobClass(JobExecutor.class.getName());
            scheduleJob.setCronJob(false);
            scheduleJob.setRepeatTime((long) 1);
        }
        if (scheduleJob.getId() == null) {
            log.info("Job Info: {}", scheduleJob);
            scheduleNewJob(scheduleJob);
        } else {
            updateScheduleJobTrigger(scheduleJob);
        }
        scheduleJob.setInterfaceName("interface_" + scheduleJob.getId());
    }

    private void scheduleNewJob(SchedulerJobInfo jobInfo) {
        SimpleJob simpleJob = SimpleJob.builder()
                .key(jobInfo.getJobName())
                .group(jobInfo.getJobGroup())
                .executor(SampleCronJobExecutor.class)
                .description(jobInfo.getDescr())
                .build();
        Trigger trigger;
        if (jobInfo.getCronJob()) {
            trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                    jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
                    jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        quartzService.scheduleJob(simpleJob, trigger);
        jobInfo.setJobStatus("SCHEDULED");
        schedulerRepository.save(jobInfo);
    }

    private void updateScheduleJobTrigger(SchedulerJobInfo jobInfo) {
        Trigger newTrigger;
        if (jobInfo.getCronJob()) {
            newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                    jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        quartzService.rescheduleJob(jobInfo.getJobName(), newTrigger);
        jobInfo.setJobStatus("EDITED & SCHEDULED");
        schedulerRepository.save(jobInfo);
    }
}
