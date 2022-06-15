package com.quartz.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String jobClass;
    private String cronExpression;
    private String descr;
    private String interfaceName;
    private Long repeatTime;
    private Boolean cronJob;
}
