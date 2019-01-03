package cn.aone.vo;

import java.io.Serializable;

/**
 * http请求返回的最外层对象
 * @author 开发
 *
 */
public class ResultVo<T> implements Serializable{
	/**
	 * id
	 */
	private static final long serialVersionUID = 6570721239649080921L;
	/**
	 * 错误码
	 */
	private Integer code;
	/**
	 * 提示信息
	 */
	private String msg;
	/**
	 *具体内容 
	 */
	private T data;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
