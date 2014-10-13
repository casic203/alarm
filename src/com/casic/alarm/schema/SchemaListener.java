package com.casic.alarm.schema;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SchemaListener implements ServletContextListener {

	private Timer timer=null;
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		event.getServletContext().log("定时器销毁！");
	}

	public void contextInitialized(ServletContextEvent event) {
		try {
			timer = new Timer(true);
			event.getServletContext().log("定时器已经启动！");
			timer.schedule(new Task(), 0, 20 * 1000);
			event.getServletContext().log("已经添加任务！"); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
