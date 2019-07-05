package cn.sh.base;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 返回内容
 * @author hanyongtao
 *
 */
public class Response {

	@JsonProperty("head")
	public String head;
	@JsonProperty("data")
	public String data;
	@JsonProperty("page")
	public String page;
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
	
}
