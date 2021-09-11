package com.github.batch.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.flow.FlowExecutionStatus
import org.springframework.batch.core.job.flow.JobExecutionDecider
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 测试决策器
@Configuration
class DeciderDemo {

    val log: Logger = LoggerFactory.getLogger(DeciderDemo::class.java)

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun deciderJob(): Job {
        val myDecider = MyDecider()
        // count 从1开始，所以执行顺序为
        return jobBuilderFactory["deciderJob"]
            .start(deciderStep()) // 1
            .next(myDecider) // 2, 5
            .from(myDecider).on("even").to(evenStep()) // 6
            .from(myDecider).on("odd").to(oddStep()) // 3
            .from(oddStep()).on("*").to(myDecider) // 4
            .end() // 7
            .build()
    }

    // 决策器
    private class MyDecider : JobExecutionDecider {
        var count = 0

        override fun decide(jobExecution: JobExecution, stepExecution: StepExecution?): FlowExecutionStatus {
            count++
            return if (count % 2 == 0)
                FlowExecutionStatus("even")
            else
                FlowExecutionStatus("odd")
        }
    }

    private fun deciderStep(): Step {
        return stepBuilderFactory["deciderStep"]
            .tasklet { contribution, chunkContext ->
                log.info("decider job start")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun oddStep(): Step {
        return stepBuilderFactory["oddStep"]
            .tasklet { contribution, chunkContext ->
                log.info("odd")
                RepeatStatus.FINISHED
            }.build()
    }

    private fun evenStep(): Step {
        return stepBuilderFactory["evenStep"]
            .tasklet { contribution, chunkContext ->
                log.info("even")
                RepeatStatus.FINISHED
            }.build()
    }
}
