package cn.sh.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by liujiansheng on 2016/11/10.
 */
public class AESUtils {
	/** 密钥加密算法 */
	private static final String KEY_ALGORITHM = "AES";
	/**
	 * 加密器加密算法 加解密算法/工作模式/填充方式
	 * Java6.0以上支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
	 */
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	/** UTF8编码 */
	public static final String CHARSET_UTF8 = "UTF-8";
    
	/**
     * 加密
     * 
     * @param content 需要加密的内容
     * @param password 加密密码
     * @return 密文
     */
    public static byte[] encrypt(String content, byte[] password) throws Exception {
  		// 生成密钥
		SecretKeySpec key = new SecretKeySpec(password, KEY_ALGORITHM);
        // 创建密码器，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        byte[] byteContent = content.getBytes(CHARSET_UTF8);
        // 初始化密码器，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        return result;
    }

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return 原文
	 */
	public static byte[] decrypt(byte[] content, byte[] password) throws Exception {
		// 生成密钥
		SecretKeySpec key = new SecretKeySpec(password, KEY_ALGORITHM);
		// 创建密码器
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化密码器，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(content);
		return result;
	}
}