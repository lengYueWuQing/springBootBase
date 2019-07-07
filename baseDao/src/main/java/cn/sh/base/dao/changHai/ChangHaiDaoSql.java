package cn.sh.base.dao.changHai;

import java.util.Date;

import org.apache.ibatis.jdbc.SQL;

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
}
