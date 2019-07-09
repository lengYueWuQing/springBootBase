package cn.sh.base.dao.changHai;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.sh.base.dao.changHai.domain.QiangPiaoInfo;
import cn.sh.base.dao.changHai.domain.SendMailInfo;

@Mapper
public interface ChangHaiDao {

	@Insert("insert into qiang_piao_info (uuid, doctor_name, create_time) values (#{uuid}, #{doctorName}, #{createTime})")
	void insertQiangPiaoInfo(@Param("uuid") String uuid, @Param("doctorName") String doctorName, @Param("createTime") Date createTime);
	
	@Insert("update qiang_piao_info set play_music = #{playMusic}, update_time = #{updateTime} where uuid = #{uuid} and doctor_name = #{doctorName}")
	void updateQiangPiaoInfo(String uuid,String doctorName, Boolean playMusic, Date updateTime);
	
	@Select("select uuid, doctor_name as doctorName, play_music as playMusic from qiang_piao_info where uuid = #{uuid} and doctor_name = #{doctorName}")
	List<QiangPiaoInfo> selectQiangPiaoInfo(String uuid, String doctorName);
	
	
	@InsertProvider(method = "insertSendMailInfo", type = ChangHaiDaoSql.class)
	void insertSendMailInfo(String uuid, String mail, String sendReason, Boolean result, Date createTime);
	
	@Update("update send_mail_info set result = #{result} where uuid = ${uuid}")
	void updateSendMailInfoByUuid(String uuid, Boolean result);
	
	@InsertProvider(method = "selectSendMailInfo", type = ChangHaiDaoSql.class)
	List<SendMailInfo> selectSendMailInfo(String uuid,String mail, String sendReason, Boolean result);
	
	@InsertProvider(method = "countSendMailInfo", type = ChangHaiDaoSql.class)
	Integer countSendMailInfo(String uuid,String mail, String sendReason, Boolean result);
}
