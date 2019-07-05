package cn.sh.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvUtils {

	/**
	 * CSV操作导出
	 * 
	 * @param file
	 *            csv文件(路径+文件名)，csv文件不存在会自动创建
	 * @param dataList
	 *            数据
	  * @param flag
	 *            true为在原文件基础上写入， false为会替换掉原文件内容
	 * @return
	 * @throws Exception 
	 */
	public static boolean exportCsv(File file, List<String> dataList, boolean flag) throws Exception {
		boolean isSucess = false;
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		if(flag && !file.exists()){
			throw new Exception("生成文件不存在");
		}
		boolean fileEmty = false;
		if(file.length()<=0){
			fileEmty = true;
		}
		try {
			out = new FileOutputStream(file, flag);
			osw = new OutputStreamWriter(out, "utf-8");// 解决Excel 乱码（加BOM）
			if(!flag || fileEmty){//替换掉原文件内容 与 文件内容为空时
				osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }, "UTF-8"));
			}
			bw = new BufferedWriter(osw);
			if (dataList != null && !dataList.isEmpty()) {
				for (String data : dataList) {
					bw.append(data).append("\r");
				}
			}
			isSucess = true;
		} catch (Exception e) {
			isSucess = false;
			throw new Exception(e);
		} finally {
			try {
				if(bw != null){
					bw.close();
				}
				if (osw != null) {
					osw.close();
				}
				if(out != null){
					out.close();
				}
			} catch (Exception e2) {
				
			}
		}

		return isSucess;
	}
}
