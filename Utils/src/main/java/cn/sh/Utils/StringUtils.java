package cn.sh.Utils;

public class StringUtils {

	/**
     * 判断一个字符是否是汉字
     * PS：中文汉字的编码范围：[\u4e00-\u9fa5]
     *
     * @param c 需要判断的字符
     * @return 是汉字(true), 不是汉字(false)
     */
    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }
    
    /**
     *  判断一个字符是否是数字（不含正负符号）
     * @param c
     * @return
     */
    public static boolean isNumeric(char c){
    	if (Character.isDigit(c)){
    	    return true;
    	   }
    	  return false;
    	 }
	
    /**
     * 该函数判断一个字符串是否包含标点符号（中文英文标点符号）。
     * 原理是原字符串做一次清洗，清洗掉所有标点符号。
     * 此时，如果原字符串包含标点符号，那么清洗后的长度和原字符串长度不同。返回true。
     * 如果原字符串未包含标点符号，则清洗后长度不变。返回false。
     * @param s
     * @return
     */
    public static boolean check(String s) {
        boolean b = false;
 
        String tmp = s;
        tmp = tmp.replaceAll("\\p{P}", "");
        if (s.length() != tmp.length()) {
            b = true;
        }
 
        return b;
    }
    
    /**
	 * 中文标点符号转英文字标点符号
	 * 
	 * @param str
	 *            原字符串
	 * @return str 新字符串
	 */
	public static String cToe(String str) {
		String[] regs = { "！", "，", "。", "；", "~", "《", "》", "（", "）", "？",
				"”", "｛", "｝", "“", "：", "【", "】", "”", "‘", "’", "!", ",",
				".", ";", "`", "<", ">", "(", ")", "?", "'", "{", "}", "\"",
				":", "{", "}", "\"", "\'", "\'" };
		for (int i = 0; i < regs.length / 2; i++) {
			str = str.replaceAll(regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
	
	/**
	 * 判断字符串是否为空或空字符串
	 * 
	 * @param str
	 *            原字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
 
	/**
	 * 全角转半角:
	 * 
	 * @param fullStr
	 * @return
	 */
	public static final String full2Half(String fullStr) {
		if (isEmpty(fullStr)) {
			return fullStr;
		}
		char[] c = fullStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			System.out.println((int) c[i]);
			if (c[i] >= 65281 && c[i] <= 65374) {
				c[i] = (char) (c[i] - 65248);
			} else if (c[i] == 12288) { // 空格
				c[i] = (char) 32;
			}
		}
		return new String(c);
	}
 
	/**
	 * 半角转全角
	 * 
	 * @param halfStr
	 * @return
	 */
	public static final String half2Full(String halfStr) {
		if (isEmpty(halfStr)) {
			return halfStr;
		}
		char[] c = halfStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
			} else if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
}
