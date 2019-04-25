package cn.org.toolkit.result.m2;

import cn.org.toolkit.enums.Code;
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
public class ResultTemplateHead<T,E> {
    private T head;
    private E body;
    private int code;
    private String msg;

    ResultTemplateHead(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    /**
     * success 固定head 固定code码
     * @return
     */
    public static ResultTemplateHead ok() {
        ResultTemplateHead resultTemplate = new ResultTemplateHead(Code.ok);
        return resultTemplate;
    }

    /**
     * success 固定head 固定code码
     *
     * @param body
     * @param <T>
     * @return
     */
    public static <T> ResultTemplateHead ok(T body) {
        ResultTemplateHead resultTemplate = new ResultTemplateHead(Code.ok);
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
    public static <T, E> ResultTemplateHead ok(T head, E body) {
        ResultTemplateHead resultTemplate = new ResultTemplateHead(Code.ok);
        resultTemplate.setHead(head);
        resultTemplate.setBody(body);
        return resultTemplate;
    }

    /**
     * fail 固定head
     *
     * @return
     */
    public static ResultTemplateHead fail() {
        return new ResultTemplateHead(Code.fail);
    }


    /**
     * fail 固定head 自定义 int码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplateHead fail(int code , String msg) {
        ResultTemplateHead resultTemplate = new ResultTemplateHead();
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
    public static <T, E> ResultTemplateHead fail(int code, String msg ,T head, E body) {
        ResultTemplateHead resultTemplate = new ResultTemplateHead();
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
    public static ResultTemplateHead hystrix(){
        return new ResultTemplateHead(Code.e002);
    }



    /**
     * hystrix 固定head 自定义 int码
     * @param code
     * @param msg
     * @return
     */
    public static ResultTemplateHead hystrix(int code , String msg){
        ResultTemplateHead resultTemplate = new ResultTemplateHead();
        resultTemplate.setCode(code);
        resultTemplate.setMsg(msg);
        return resultTemplate;
    }

}