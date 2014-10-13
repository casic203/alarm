package com.casic.alarm.schema;

import java.util.Timer;
import java.util.TimerTask;

import com.casic.core.prop.PropertiesTool;

public class Task extends TimerTask {

	private Timer timer = null;
	private Long interval = null;

	public Task() {
		timer = new Timer(true);
		this.interval=24 * 60 * 60 * 1000l;
	}

	@Override
	public void run() {
		try {
			Object interval = new PropertiesTool().getProperty("backup_interval");
			if (this.interval != Long.parseLong(interval.toString())) {
				this.interval = Long.parseLong(interval.toString());
				timer.schedule(new BackUpTask(), 0, this.interval);
			}else {
				timer.notify();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
