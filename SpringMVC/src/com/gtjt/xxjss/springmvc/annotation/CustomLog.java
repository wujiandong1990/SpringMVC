package com.gtjt.xxjss.springmvc.annotation;  
  
import java.lang.annotation.*;  
  
/** 
 *自定义注解 拦截Controller 
 */  
  
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface CustomLog {  
  
	String url() default "";
    String description()  default "";
    
}  
  
