package com.casic.alarm.utils;

import javax.annotation.Resource;

import com.casic.alarm.scheduler.SystemJob.MessageService;
import com.casic.core.mail.SimpleMailService;

public class MailMessage implements MessageService {
	
	/**
	 * 邮件服务
	 */
	@Resource
	private SimpleMailService simpleMailService;
	
	public SimpleMailService getSimpleMailService() {
		return simpleMailService;
	}

	public void setSimpleMailService(SimpleMailService simpleMailService) {
		this.simpleMailService = simpleMailService;
	}

	public void sendMsgToAcceptAlarmPerson(String contact, String msgTitle,
			String msgContent) {
		simpleMailService.send(contact, msgTitle, msgContent);
	}
}
