package com.casic.alarm.scheduler;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.casic.alarm.permission.HttpUrlSourceFetcher;

@Service
public class WarnInfoPusherProxy 
{
	private static Logger logger = LoggerFactory.getLogger(WarnInfoPusherProxy.class);

	@Resource
	private JmsTemplate jsmTemplate;
	
	@Resource
	private Destination destionation;
	
	private String msg;
	
	public JmsTemplate getJsmTemplate() {
		return jsmTemplate;
	}

	public void setJsmTemplate(JmsTemplate jsmTemplate) {
		this.jsmTemplate = jsmTemplate;
	}

	public Destination getDestionation() {
		return destionation;
	}

	public void setDestionation(Destination destionation) {
		this.destionation = destionation;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void sendMsg(final String source)
	{
		this.msg=source;
		
		jsmTemplate.send(destionation,new MessageCreator(){
	
			public Message createMessage(Session session)
					throws JMSException {
				// TODO Auto-generated method stub
				return session.createTextMessage(source);
			}
		});
		logger.info("发送消息成功："+source);
		
	}
}
