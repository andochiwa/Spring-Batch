package com.github.batch.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
class JobConfiguration {

    val log: Logger = LoggerFactory.getLogger(JobConfiguration::class.java)

    // 注入创建任务对象
    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    // 任务的执行由step决定，创建注入step对象
    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    // 任务对象
    @Bean
    fun helloWorldJob(): Job {
        return jobBuilderFactory["helloWorldJob"]
            .start(step1())
            .build()
    }

    private fun step1(): Step {
        return stepBuilderFactory["step1"]
            .tasklet { contribution, chunkContext ->
                log.info("contribution: $contribution")
                log.info("chunkContext: $chunkContext")
                log.info("hello world")
                // 是否结束step
                RepeatStatus.FINISHED
            }
            .build()
    }
}
