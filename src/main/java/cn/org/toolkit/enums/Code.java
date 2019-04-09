package cn.org.toolkit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Package: cn.org.toolkit.enums
 * @ClassName: Code
 * @Description:
 * @Author: mac-pro
 * @CreateDate: 2019/2/21 下午12:14
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum Code {
    ok(0,"ok"),
    fail(-1,"fail"),
    e002(-2,"服务熔断")
    ;
    private int code;
    private String msg;
}

