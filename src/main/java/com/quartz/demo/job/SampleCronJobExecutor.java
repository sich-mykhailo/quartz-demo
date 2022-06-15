package com.quartz.demo.job;

import com.quartz.demo.quartz.AbstractJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import java.util.stream.IntStream;

@Slf4j
@DisallowConcurrentExecution
public class SampleCronJobExecutor extends AbstractJobExecutor {
    @Override
    protected void executeJob(String key, JobDataMap jobDataMap) {
        log.info("start................");
        IntStream.range(0, 10).forEach(i -> {
            log.info("Counting - {}", i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        });
        log.info("end................");
    }
}
