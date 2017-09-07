package com.jund.security.test;

import org.springframework.boot.test.context.SpringBootTest;

import com.jund.security.Application;
import com.jund.testcase.suite.WebSuite;

@SpringBootTest(classes = Application.class)
public class SpringBootSuite extends WebSuite{

}
