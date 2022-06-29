package com.example.thuephong.aspect;

import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Aspect
@Configuration
//là một annotation của class. Class được đánh dấu annotaiton này được Spring Container sử dụng làm nguồn định nghĩa bean
@ControllerAdvice
public class Aspecta {

    @Before(value = "executeController()")
    public void beforeExecuteController(){
        System.out.println("Before Controller");
    }

    @After(value = "executeController()")
    public void afterExecuteController(){
        System.out.println("After Controller");
    }

    @AfterThrowing(value = "executeController()" , throwing = "ex")
    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(Exception ex){
        System.out.println("Lỗi" + ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Pointcut(value = "within(com.example.thuephong.controller.*)")
    public void executeController(){
    }

}
