package com.hp.common.base;

/**
 * 返回结果封装
 *
 * @author wanggw
 *
 */
public class Rs {

    //结果
    private boolean success;
    //信息
    private String message;
    //返回的对象
    private Object data;

    /**
     * 成功（默认）
     * @return 结果
     *
     */
    public static Rs success(){
        Rs rs = new Rs();
        rs.setSuccess(true);
        rs.setMessage("处理成功");
        rs.setData(null);
        return rs;
    }

    /**
     * 成功（自定义message）
     *
     * @param message 自定义message
     * @return 结果
     */
    public static Rs success(String message){
        Rs rs = success();
        rs.setMessage(message);
        return rs;
    }

    /**
     * 成功（自定义data）
     *
     * @param data 自定义data
     * @return 结果
     */
    public static Rs success(Object data){
        Rs rs = success();
        rs.setData(data);
        return rs;
    }

    /**
     * 成功（自定义message和data）
     *
     * @param message 自定义message
     * @param data 自定义data
     * @return 结果
     */
    public static Rs success(String message, Object data){
        Rs rs = success();
        rs.setMessage(message);
        rs.setData(data);
        return rs;
    }

    /**
     * 失败（默认）
     *
     * @return 结果
     */
    public static Rs faild(){
        Rs rs = new Rs();
        rs.setSuccess(false);
        rs.setMessage("处理失败");
        rs.setData(null);
        return rs;
    }

    /**
     * 失败（自定义message）
     *
     * @param message 自定义message
     * @return 结果
     */
    public static Rs faild(String message){
        Rs rs = faild();
        rs.setMessage(message);
        return rs;
    }


    /**
     * 失败（自定义message）
     *
     * @param data 自定义data
     * @return 结果
     */
    public static Rs faild(Object data){
        Rs rs = faild();
        rs.setData(data);
        return rs;
    }


    /**
     * 失败（自定义message）
     *
     * @param message 自定义message
     * @param data 自定义data
     * @return 结果
     */
    public static Rs faild(String message, Object data){
        Rs rs = faild();
        rs.setMessage(message);
        rs.setData(data);
        return rs;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
