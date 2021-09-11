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
import org.springframework.core.task.SimpleAsyncTaskExecutor

@Configuration
class SplitDemo {

    val log: Logger = LoggerFactory.getLogger(JobDemo::class.java)

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun splitJob(): Job {
        return jobBuilderFactory["splitDemo"]
            .start(flowSplitDemo1())
            .split(SimpleAsyncTaskExecutor()).add(flowSplitDemo2())
            .end()
            .build()
    }

    private fun flowSplitDemo1(): Flow {
        return FlowBuilder<Flow>("flowSplitDemo")
            .start(flowSplitStep1())
            .next(flowSplitStep2())
            .build()
    }

    private fun flowSplitDemo2(): Flow {
        return FlowBuilder<Flow>("flowSplitDemo")
            .start(flowSplitStep3())
            .next(flowSplitStep4())
            .build()
    }

    private fun flowSplitStep1(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowSplitStep1")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun flowSplitStep2(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowSplitStep2")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun flowSplitStep3(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowSplitStep3")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun flowSplitStep4(): Step {
        return stepBuilderFactory["flowStep1"]
            .tasklet { contribution, chunkContext ->
                log.info("flowSplitStep4")
                RepeatStatus.FINISHED
            }.build()
    }
}
