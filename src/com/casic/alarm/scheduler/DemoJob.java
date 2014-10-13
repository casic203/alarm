package com.casic.alarm.scheduler;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.casic.core.mail.SimpleMailService;

public class DemoJob implements Job {

	/**
	 * 邮件服务
	 */
	@Resource
	private SimpleMailService simpleMailService;
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("执行execute");
	}
	
	public void test() {
		System.out.println("test......");
		simpleMailService.send("410611627@qq.com", "报警信息", "测试报警信息");
	}
}
