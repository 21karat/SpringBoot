package cn.aone.exception;

import cn.aone.enums.ResultEnum;

public class SellException extends RuntimeException{

   
	private static final long serialVersionUID = 1639299013361413024L;
	private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
