package com.example.taskflow.log.aop;


import com.example.taskflow.log.entity.ActivityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityLogging {
    ActivityType value();
    String targetParam() default "targetId";
}