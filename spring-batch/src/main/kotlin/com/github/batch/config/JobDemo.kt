package com.github.batch.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class JobDemo {

    val log: Logger = LoggerFactory.getLogger(JobDemo::class.java)

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun demoJob(): Job {
        return jobBuilderFactory["demoJob"]
//            .start(step1())
//            .next(step2())
//            .next(step3())
            .start(step1())
                // 指定条件
            .on("COMPLETED").to(step2())
            .from(step2()).on("COMPLETED").to(step3())
            .from(step3()).end()
            .build()
    }

    private fun step1(): Step {
        return stepBuilderFactory["step1"]
            .tasklet { contribution, chunkContext ->
                log.info("demoJob: step1")
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun step2(): Step {
        return stepBuilderFactory["step1"]
            .tasklet { contribution, chunkContext ->
                log.info("demoJob: step2")
                RepeatStatus.FINISHED
            }
            .build()
    }

    private fun step3(): Step {
        return stepBuilderFactory["step1"]
            .tasklet { contribution, chunkContext ->
                log.info("demoJob: step3.1")
                RepeatStatus.CONTINUABLE
            }
            .tasklet { contribution, chunkContext ->
                log.info("demoJob: step3.2")
                RepeatStatus.FINISHED
            }
            .build()
    }

}
