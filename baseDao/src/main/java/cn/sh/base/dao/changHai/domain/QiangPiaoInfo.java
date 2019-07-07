package cn.sh.base.dao.changHai.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 抢票信息表
 * @author hanyongtao
 *
 */
public class QiangPiaoInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4748479049593708997L;
	private Integer id;
	private String uuid;
	private String doctorName;
	private Boolean playMusic;
	private Date createTime;
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Boolean getPlayMusic() {
		return playMusic;
	}
	public void setPlayMusic(Boolean playMusic) {
		this.playMusic = playMusic;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return "SendMailInfo [id=" + id + ", uuid=" + uuid + ", doctorName=" + doctorName + ", playMusic=" + playMusic
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	
}
