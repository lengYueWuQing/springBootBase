package cn.sh.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {

	/**
	 * 获取文件内容
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static String readToString(File file) throws IOException{
		if(!file.exists() || file.isDirectory()){
			return null;
		}
        String encoding = "UTF-8";  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(filecontent);
		} catch (FileNotFoundException e1) {
			
		}finally{
			if(in != null){
				in.close();
			}
		}
		return new String(filecontent, encoding);
    }
}
