package cn.sh.base.dao.changHai;

import java.util.Date;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

public class ChangHaiDaoSql {

	public String insertSendMailInfo(String uuid, String mail, String sendReason, Boolean result, Date createTime) {
		return new SQL() {
			{
				INSERT_INTO("send_mail_info");
				VALUES("uuid", "#{uuid}");
				VALUES("mail", "#{mail}");
				VALUES("send_reason", "#{sendReason}");
				VALUES("create_time", "#{createTime}");
				if (result != null) {
					VALUES("result", "#{result}");
				}
			}
		}.toString();
	}
	
	public String selectSendMailInfoByUuid(String uuid, String mail, String sendReason, Boolean result) {
		return new SQL() {
			{
				SELECT("uuid, mail, send_reason as sendReason, result, create_time as createTime");
				FROM("send_mail_info");
				WHERE("uuid=#{uuid}");
				
				if (!StringUtils.isEmpty(mail)) {
					AND();
					VALUES("mail", "#{mail}");
				}
				if (result != null) {
					AND();
					VALUES("result", "#{result}");
				}
				if (result != null) {
					AND();
					VALUES("send_reason", "#{sendReason}");
				}
			}
		}.toString();
	}
}
