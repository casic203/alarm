package com.casic.alarm.utils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.casic.alarm.scheduler.SystemJob.MessageService;
import com.casic.core.Phone.WebUrl;

public class SmsMessage implements MessageService {

	public void sendMsgToAcceptAlarmPerson(String contact, String msgTitle,	String msgContent) {
		try {
			com.casic.core.Phone.MessageService msgService=new com.casic.core.Phone.MessageService();
			msgService.setUrl(WebUrl.UTF);
			msgService.setKey("314159");
			msgService.setUid("thc");
			msgService.send(contact, msgContent);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
