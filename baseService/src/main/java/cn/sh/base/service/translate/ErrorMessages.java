package cn.sh.base.service.translate;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 错误信息
 * @author hanyongtao
 *
 */
public class ErrorMessages {

	@JSONField(name="ermes")
	List<String> errorMessage;

	public List<String> getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(List<String> errorMessage) {
		this.errorMessage = errorMessage;
	}
}