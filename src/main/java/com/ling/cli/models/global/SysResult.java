package com.ling.cli.models.global;

// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

/**
 * @author ling
 * @version 1.0
 * @description: 全局统一返回
 */
@Data
public class SysResult {
    // @ApiModelProperty(notes = "响应码，非200 即为异常")
    private final int code;
    // @ApiModelProperty(notes = "响应消息", example = "提交成功")
    private final String msg;
    @Getter
    // @ApiModelProperty(notes = "响应数据")
    private Object data;
    // @ApiModelProperty(notes = "是否成功")
    private final Boolean success;

    protected SysResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = ResponseCode.SUCCESS == code;
    }

    public static SysResult success(String msg, Object data) {return new SysResult(ResponseCode.SUCCESS, msg, data);}
    public static SysResult success(Object data) {
        return success("操作成功", data);
    }
    public static SysResult success(String msg) {
        return success(msg, null);
    }
    public static SysResult success() {
        return success("操作成功", null);
    }
    public static SysResult warning(String msg) {return error(ResponseCode.WARNING, msg, null);}
    public static SysResult warning(String msg,Object data) {return error(ResponseCode.WARNING, msg, data);}
    public static SysResult error(int code, String msg, Object data) {
        return new SysResult(code, msg, data);
    }
    public static SysResult error(int code, String msg) {return error(code, msg, null);}
    public static SysResult error(Object data) {
        return error(ResponseCode.ERROR, "", data);
    }

    public void setData(Object data) {
        this.data = data;
    }

}
