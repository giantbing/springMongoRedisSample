package com.giant.learn

import com.giant.learn.model.ClassBean
import com.giant.learn.service.ClassService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableMongoRepositories
@SpringBootApplication
@EnableTransactionManagement
//@EnableCaching(proxyTargetClass = true)
class LearnApplication : ApplicationRunner {

    @Autowired
    lateinit var classService: ClassService

    override fun run(args: ApplicationArguments?) {


        classService.findAllClass()

        for (i in 1..10) {
            classService.findAllClass()
        }

    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, ClassBean> {
        val template = RedisTemplate<String, ClassBean>()
        template.setConnectionFactory(redisConnectionFactory)
        return template
    }

    @Bean
    fun redisTemplateList(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, List<ClassBean>> {
        val template = RedisTemplate<String, List<ClassBean>>()
        template.setConnectionFactory(redisConnectionFactory)
        return template
    }
}

val log = LoggerFactory.getLogger(LearnApplication::class.java)
fun main(args: Array<String>) {
    runApplication<LearnApplication>(*args)
}
