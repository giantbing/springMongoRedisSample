package com.giant.learn.model

import org.springframework.data.annotation.Id
import java.io.Serializable
import java.util.*


data class ClassBean(

        val name: String,
        val school:String,
        val updateime: Date? = null,
        val createTime: Date = Date(),
        @Id
        val id: String? = null
        ):Serializable