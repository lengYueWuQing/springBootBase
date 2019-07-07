package cn.sh.base.dao.changHai.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送邮件信息表
 * @author hanyongtao
 *
 */
public class SendMailInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4748479049593708997L;
	private Integer id;
	private String uuid;
	private String mail;
	private String sendReason;
	private Boolean result;
	private Date createTime;
	
	public Integer getId() {
		return id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getSendReason() {
		return sendReason;
	}
	public void setSendReason(String sendReason) {
		this.sendReason = sendReason;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "SendMailInfo [id=" + id + ", uuid=" + uuid + ", sendReason=" + sendReason + ", result=" + result
				+ ", createTime=" + createTime + "]";
	}
	
	
}
