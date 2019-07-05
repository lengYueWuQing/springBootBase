package cn.sh.base.dao.user.domain;

import java.io.Serializable;
import java.util.Date;


public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7362603405302202291L;
	private Integer id;//用户主键
	private String loginName;//登录名
	private String password;//登录密码
	private String status;//状态
	private String realName;//真实姓名
	
	private Date createTime;//创建时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", loginName=" + loginName + ", password=" + password + ", status=" + status
				+ ", realName=" + realName + ", createTime=" + createTime + "]";
	}
	
}
