package cn.org.toolkit.result.m1;

import cn.org.toolkit.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Package: com.example.demo.result.m1
 * @ClassName: ResultTemplate
 * @Description: 统一普通接口返回规范 包含head 和body 可泛型扩展
 * 成功和失败固定
 * 成功时 返回固定head(code码可扩展参照code码表 enum) 和泛型body
 * 失败时 返回固定head(code码可扩展参照code码表 enum) 没有body
 * @Author: Administrator
 * @CreateDate: 2018/4/27 20:10
 * @Version: 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResultTemplate<T,E> {
    private T head;
    private E body;
    private int code;
    private String msg;

    ResultTemplate(CodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    /**
     * success 固定head 固定code码
     * @return
     */
    public static ResultTemplate ok() {
        ResultTemplate resultTemplate = new ResultTemplate(CodeEnum.ok);
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
        ResultTemplate resultTemplate = new ResultTemplate(CodeEnum.ok);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * success 固定head 自定义 code码
     * @param code
     * @param body
     * @param <T>
     * @return
     */
    public static <T> ResultTemplate ok(CodeEnum code , T body) {
        ResultTemplate resultTemplate = new ResultTemplate(code);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * success 自定义head
     *
     * @param head
     * @param body
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> ResultTemplate ok(T head, E body) {
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setHead(head);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * fail 固定head
     *
     * @return
     */
    public static ResultTemplate fail() {
        return new ResultTemplate(CodeEnum.fail);
    }

    /**
     * fail 固定head 自定义 code码
     * @param code
     * @return
     */
    public static ResultTemplate fail(CodeEnum code) {
        return new ResultTemplate(code);
    }

    /**
     * fail 固定head 自定义 code码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplate fail(CodeEnum code , String msg) {
        ResultTemplate resultTemplate = new ResultTemplate(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
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
     * fail 自定义head
     *
     * @param head
     * @param body
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> ResultTemplate fail(int code, String msg ,T head, E body) {
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setCode(code);
        resultTemplate.setMsg(msg);
        resultTemplate.setHead(head);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * hystrix 固定head
     *
     * @return
     */
    public static ResultTemplate hystrix(){
        return new ResultTemplate(CodeEnum.e002);
    }

    /**
     * hystrix 自定义 code码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplate hystrix(CodeEnum code , String msg){
        ResultTemplate resultTemplate = new ResultTemplate(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
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


    /**
     * hystrix 固定 int码
     * @param msg
     * @return
     */
    public static ResultTemplate hystrix(String msg){
        ResultTemplate resultTemplate = new ResultTemplate();
        resultTemplate.setCode(1003);
        resultTemplate.setMsg(msg);
        return resultTemplate;
    }
}