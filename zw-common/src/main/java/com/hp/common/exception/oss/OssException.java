package com.hp.common.exception.oss;

/**
 * OSS信息异常类
 * 
 * @author hp
 */
public class OssException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public OssException(String msg)
    {
        super(msg);
    }
}
