package cn.sh.base.service.translate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sh.Utils.HttpUtils;
import cn.sh.Utils.mail.SendQQEmailUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	/*List<String> addressList = new ArrayList<String>();
    	//addressList.add("1126257895@qq.com");
    	addressList.add("hanyongtao@sis.sh.cn");
    	List<File> fileList = new ArrayList<File>();
    	fileList.add(new File("D://styles.xml"));
    	fileList.add(new File("D://bb.txt"));
        SendQQEmailUtils.sendMessageMail("1126257895", "zqfizofzmpepfebd", "主题", "信息", addressList, fileList);*/
    	
    	System.out.println(HttpUtils.doGet("https://ss1.bdstatic.com/5eN1bjq8AAUYm2zgoY3K/r/www/cache/static/protocol/https/global", null));
    }
}
