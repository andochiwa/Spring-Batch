package com.github.batch.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlowDemo {

    val log: Logger = LoggerFactory.getLogger(FlowDemo::class.java)

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun flowJob(): Job {
        return jobBuilderFactory["flowJob"]
            .start(flowDemo())
            .next(flowStep3())
            .end()
            .build()
    }

    // 创建flow
    private fun flowDemo(): Flow {
        return FlowBuilder<Flow>("flowDemo")
            .start(flowStep1())
            .next(flowStep2())
            .build()
    }

    private fun flowStep1(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowStep1")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun flowStep2(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowStep2")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun flowStep3(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowStep3.1")
                RepeatStatus.CONTINUABLE
            }
            .tasklet { contribution, chunkContext ->
                log.info("flowStep3.2")
                RepeatStatus.FINISHED
            }.build()
    }
}
