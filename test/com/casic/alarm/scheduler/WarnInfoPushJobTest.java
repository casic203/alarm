package com.casic.alarm.scheduler;

import static org.junit.Assert.*;

import javax.jms.Destination;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class WarnInfoPushJobTest {

	
	public static void main(String[] args) 	{
		/*
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-jms.xml");       
		Destination destination = (Destination) applicationContext.getBean("destination");   
		JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");   
		
		while (true) {    
			try {
		  //  System.out.println("服务器端开始"); 
			TextMessage txtmsg = (TextMessage) jmsTemplate.receive(destination); 
			if (null != txtmsg) 
			{
				System.out.println("[DB Proxy] " + txtmsg);  
			    System.out.println("[DB Proxy] 收到消息内容为: " + txtmsg.getText()); 
			} 
			else 
				break;           
			} 
		catch (Exception e) 
		{  
			e.printStackTrace();    
		}       
	}
	*/
	}
}
