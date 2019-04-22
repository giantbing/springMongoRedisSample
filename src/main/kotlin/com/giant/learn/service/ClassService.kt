package com.giant.learn.service

import com.giant.learn.log
import com.giant.learn.model.ClassBean
import com.giant.learn.repositroy.ClassRepositroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class ClassService {
    @Autowired
    lateinit var classRepository: ClassRepositroy
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, ClassBean>
    @Autowired
    lateinit var redisTemplateList: RedisTemplate<String, List<ClassBean>>

    companion object {
        val MemuCache = "memu_cache"
        val ClassBeanCache = "ClassBeanCache"
    }

    //    @Cacheable
    fun findAllClass(): Optional<List<ClassBean>> {
        val hashOp = redisTemplateList.opsForHash<String, List<ClassBean>>()
        if (redisTemplateList.hasKey(MemuCache) && hashOp.hasKey(MemuCache, MemuCache)) {
            val data = hashOp.get(MemuCache, MemuCache)
            log.info("get Memu:{} From Redis.", data)
            return Optional.of(data!!)
        }

        val mongoData = classRepository.findAll()
        log.info("put memu to Redis")
        hashOp.put(MemuCache, MemuCache, mongoData)
        redisTemplateList.expire(MemuCache, 1, TimeUnit.MINUTES)
        return Optional.of(mongoData)
    }

    fun findOneClassByName(name: String): Optional<ClassBean> {
        val hashOp = redisTemplate.opsForHash<String, ClassBean>()
        if (redisTemplate.hasKey(ClassBeanCache) && hashOp.hasKey(ClassBeanCache, name)) {
            val data = hashOp.get(ClassBeanCache, name)
            log.info("get {} From Redis ", name)
            return Optional.of(data!!)
        }

        val mongoData = classRepository.findByName(name)
        log.info("put {} in Redis", name)
        hashOp.put(ClassBeanCache, name, mongoData.get())
        redisTemplate.expire(ClassBeanCache, 1, TimeUnit.MINUTES)
        return mongoData
    }

    fun findAllClassBySchool(school: String): Optional<List<ClassBean>> = classRepository.findAllBySchool(school)
    //    @CacheEvict
    fun deleteClassCache() {

    }

    fun initClass() {
        classRepository.deleteAll()
        val list = listOf(ClassBean(name = "1ban", updateime = Date(), school = "yi"), ClassBean(name = "2ban", updateime = Date(), school = "yi"))
        classRepository.saveAll(list)
    }

}