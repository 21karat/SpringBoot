package cn.aone.util;

import cn.aone.vo.ResultVo;

/**
 * 返回视图对象工具类
 * 可以省去总是new 返回的对象
 */
public class ResultVOUtil {
	//成功
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResultVo success(Object object) {
    	ResultVo resultVO = new ResultVo();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }
    //成功返回空
    @SuppressWarnings("rawtypes")
	public static ResultVo success() {
        return success(null);
    }
    //失败
    @SuppressWarnings("rawtypes")
	public static ResultVo error(Integer code, String msg) {
    	ResultVo resultVO = new ResultVo();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
