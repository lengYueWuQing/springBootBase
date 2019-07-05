package cn.sh.base;

import java.io.IOException;
public class Chuangkou {

	/*public static void main(String[] args) {
		String pid = startService(args);
		System.out.println(pid);
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stopService(pid);
	}*/
	
	public static String startService(String[] args){
		
		return Application.start(args);
	}

	public static boolean stopService(String pid){
		if(pid==null || (pid = pid.trim()).equals("")){
			return false;
		}
		try {
			Runtime.getRuntime().exec("cmd /c taskkill /pid "+pid+" /f");
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
