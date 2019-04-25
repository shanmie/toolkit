package cn.org.toolkit.result.m1;

import cn.org.toolkit.enums.Code;
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
public class ResultTemplate<T> {
    private T body;
    private int code;
    private String msg;

    ResultTemplate(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    /**
     * success 固定head 固定code码
     * @return
     */
    public static ResultTemplate ok() {
        ResultTemplate resultTemplate = new ResultTemplate(Code.ok);
        return resultTemplate;
    }

    /**
     * success 固定head 固定code码
     *
     * @param body
     * @param <T>
     * @return
     */
    public static <T> ResultTemplate ok(T body) {
        ResultTemplate resultTemplate = new ResultTemplate(Code.ok);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * fail 固定head
     *
     * @return
     */
    public static ResultTemplate fail() {
        return new ResultTemplate(Code.fail);
    }


    /**
     * fail 固定head 自定义 int码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplate fail(int code , String msg) {
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setCode(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
    }


    /**
     * hystrix 固定head
     *
     * @return
     */
    public static ResultTemplate hystrix(){
        return new ResultTemplate(Code.e002);
    }


    /**
     * hystrix 固定head 自定义 int码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplate hystrix(int code , String msg){
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setCode(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
    }
}
