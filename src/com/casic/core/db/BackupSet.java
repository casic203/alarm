package com.casic.core.db;

import java.io.IOException;

public class BackupSet {
	public static void delSchTask(String tn) {
		try {
			String cmd="schtasks /delete /tn \""+tn+"\" /f";
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void addSchTask(String tn,String tr,String mo) {
		String cmd="schtasks /create /tn \""+tn+"\" /tr "+tr+" /sc minute /mo "+mo+" /ru \"System\"";
		Runtime.getRuntime().equals(cmd);
	}
}
