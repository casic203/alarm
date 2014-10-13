package com.casic.alarm.schema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.h2.util.New;

public class BackUpTask extends TimerTask {
	
	@Override
	public void run() {
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
			String tm=dateFormat.format(new Date());
			//String cmd="exp 'sys/orcl@orcl as sysdba' file="+readPath()+"\\"+tm+"\\数据库_scott_"+tm+".dmp owner=scott;";
			String cmd="exp 'sys/orcl@orcl as sysdba' file="+readPath()+"\\数据库_scott_"+tm+".dmp owner=scott;";
			System.out.println(cmd);
			doBackUp(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void doBackUp(String cmd){
		try {
			Runtime runtime=Runtime.getRuntime();
			runtime.exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
	private String readPath() throws IOException{
		File file = new File("d:\\cfg.txt");
		String path=null;
		if (file.isFile() && file.exists()) {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader bReader = new BufferedReader(reader);
			bReader.readLine();
			path=bReader.readLine();
			reader.close();
		}
		return path;
	}

}
