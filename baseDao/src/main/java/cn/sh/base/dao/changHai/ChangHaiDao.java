package cn.sh.base.dao.changHai;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.sh.base.dao.changHai.domain.QiangPiaoInfo;
import cn.sh.base.dao.changHai.domain.SendMailInfo;

@Mapper
public interface ChangHaiDao {

	@Insert("insert into qiang_piao_info (uuid, doctor_name, create_time) values (#{uuid}, #{doctorName}, #{createTime}))")
	void insertQiangPiaoInfo(String uuid, String doctorName, Date createTime);
	
	@Insert("update qiang_piao_info set play_music = #{plauMusic}, create_time = #{createTime})")
	void updateQiangPiaoInfoByUuid(String uuid, Boolean playMusic, Date createTime);
	
	@Select("select uuid, doctor_name as doctorName, play_music as playMusic from qiang_piao_info where uuid = #{uuid})")
	QiangPiaoInfo selectQiangPiaoInfoByUuid(String uuid);
	
	
	@InsertProvider(method = "insertSendMailInfo", type = ChangHaiDaoSql.class)
	void insertSendMailInfo(String uuid, String sendReason, Boolean result, Date createTime);
	
	@Update("update send_mail_info set result = #{result} where uuid = ${uuid}")
	void updateSendMailInfoByUuid(String uuid, Boolean result);
	
	@Select("select uuid, result from send_mail_info where uuid = #{uuid})")
	SendMailInfo selectSendMailInfoByUuid(String uuid);
}
