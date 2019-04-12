package cn.org.toolkit.result.m2;

import cn.org.toolkit.enums.Code;
import cn.org.toolkit.result.m1.ResultTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Package: cn.org.toolkit.result.m2
 * @ClassName: ResultTemplateNoHead
 * @Description:
 * @Author: mac-pro
 * @CreateDate: 2019/2/21 下午12:22
 * @Version: 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResultTemplateNoHead<T> {
    private T body;
    private int code;
    private String msg;

    ResultTemplateNoHead(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    /**
     * success 固定head 固定code码
     * @return
     */
    public static ResultTemplateNoHead ok() {
        ResultTemplateNoHead resultTemplate = new ResultTemplateNoHead(Code.ok);
        return resultTemplate;
    }

    /**
     * success 固定head 固定code码
     *
     * @param body
     * @param <T>
     * @return
     */
    public static <T> ResultTemplateNoHead ok(T body) {
        ResultTemplateNoHead resultTemplate = new ResultTemplateNoHead(Code.ok);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * fail 固定head
     *
     * @return
     */
    public static ResultTemplateNoHead fail() {
        return new ResultTemplateNoHead(Code.fail);
    }


    /**
     * fail 固定head 自定义 int码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplateNoHead fail(int code , String msg) {
        ResultTemplateNoHead resultTemplate = new ResultTemplateNoHead();
        resultTemplate.setCode(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
    }


    /**
     * hystrix 固定head
     *
     * @return
     */
    public static ResultTemplateNoHead hystrix(){
        return new ResultTemplateNoHead(Code.e002);
    }


    /**
     * hystrix 固定head 自定义 int码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplateNoHead hystrix(int code , String msg){
        ResultTemplateNoHead resultTemplate = new ResultTemplateNoHead();
        resultTemplate.setCode(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
    }
}
