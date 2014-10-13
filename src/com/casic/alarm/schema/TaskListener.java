package com.casic.alarm.schema;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.casic.core.prop.PropertiesTool;

public class TaskListener implements ServletContextListener {
	private Timer timer = null;

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("定时器销毁！");
	}

	public void contextInitialized(ServletContextEvent event) {
//		try {
//			timer = new Timer(true);
//			event.getServletContext().log("定时器已经启动！");
//			System.out
//					.println("定时器已经启动.......................................");
//			Object interval = new PropertiesTool().getProperty("scan_interval");
//			if (null == interval) {
//				timer.schedule(new Task(), 0, 20 * 1000);
//			} else {
//				timer.schedule(new Task(), 0,
//						Long.parseLong(interval.toString()));
//			}
//			event.getServletContext().log("已经添加任务！");
//			System.out.println("已经添加任务.......................................");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
