package com.giant.learn.repositroy

import com.giant.learn.model.ClassBean
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ClassRepositroy : MongoRepository<ClassBean, String>{
    fun findAllBySchool(school:String):Optional<List<ClassBean>>
    fun findByName(name:String):Optional<ClassBean>
}