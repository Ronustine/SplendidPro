package com.ronustine.splendid;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ronustine.splendid.pojo.User;
import com.ronustine.splendid.service.IUserService;  

 
@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})  
 
public class TestMyBatis {  
// private ApplicationContext ac = null;  
   @Resource  
   private IUserService userService = null;  
 
// @Before  
// public void before() {  
//     ac = new ClassPathXmlApplicationContext("applicationContext.xml");  
//     userService = (IUserService) ac.getBean("userService");  
// }  
 
   @Test  
   public void test1() {  
       User user = userService.getUserById(1);  
       // System.out.println(user.getUserName());  
       // logger.info("值："+user.getUserName());  
       System.out.println(user.getUserName());
   }  
}