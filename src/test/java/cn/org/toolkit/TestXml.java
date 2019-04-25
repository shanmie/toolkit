package cn.org.toolkit;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deacon
 * @since 2019/4/23
 */
public class TestXml {

    @Test
    public void testXml()  {
        String ss = "<xml><ToUserName><![CDATA[gh_8e3f0fc144cb]]></ToUserName>\n" +
                "<FromUserName><![CDATA[oW4TWt-aO94b-aNHRcNA39uJPkmo]]></FromUserName>\n" +
                "<CreateTime>1556074482</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[SCAN]]></Event>\n" +
                "<EventKey><![CDATA[101]]></EventKey>\n" +
                "<Ticket><![CDATA[gQG28DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyblJlX0J2cjRibVQxTm03OXhzMXYAAgTWzL9cAwSAOgkA]]></Ticket>\n" +
                "</xml>";

        XStream xStream = new XStream();
        xStream.alias("xml", WxEventMsg.class);
        WxEventMsg eventMsg = (WxEventMsg) xStream.fromXML(ss);
        System.out.println(eventMsg.getEvent());
    }


    static class WxEventMsgReply {
        @Data
        @XStreamAlias("xml")
        static
        class TextMsg{
            @XStreamAlias("ToUserName")
            String toUserName;
            @XStreamAlias("FromUserName")
            String fromUserName;
            @XStreamAlias("CreateTime")
            long createTime;
            @XStreamAlias("MsgType")
            String msgType;
            @XStreamAlias("Content")
            String content;
        }
        @Data
        static class ImageMsg {
            String ToUserName;
            String FromUserName;
            long CreateTime;
            String MsgType;
            Image Image;
        }
        @Data
        static class Image{
            String MediaId;
        }
        @Data
        static class ImageTextMsg{
            String ToUserName;
            String FromUserName;
            long CreateTime;
            String MsgType;
            int ArticleCount;
            Articles Articles;
        }
        @Data
        static class Articles{
            Item item;
        }
        @Data
        static class Item{
            String Title;
            String Description;
            String PicUrl;
            String Url;
        }


    }

    @Test
    public void test(){
        WxEventMsgReply.ImageTextMsg w = new WxEventMsgReply.ImageTextMsg();
        w.setToUserName("1");
        w.setFromUserName("0");
        w.setCreateTime(1233213);
        w.setMsgType("000");
        w.setArticleCount(1);

        WxEventMsgReply.Item item = new WxEventMsgReply.Item();
        item.setTitle("12");
        item.setDescription("2");
        item.setPicUrl("url");
        item.setUrl("32");
        WxEventMsgReply.Articles a = new WxEventMsgReply.Articles();
        a.setItem(item);
        w.setArticles(a);
        XStream x = new XStream();
        x.alias("xml",WxEventMsgReply.ImageTextMsg.class);
        String s1 = x.toXML(w);
        System.out.println(s1);
    }
    @Test
    public void testToXml(){
        WxEventMsgReply.TextMsg w = new WxEventMsgReply.TextMsg();
        w.setToUserName("<![CDATA[toUser]]>");
        w.setFromUserName("<![CDATA[fromUser]]>");
        w.setCreateTime(1233213);
        w.setMsgType("<![CDATA[text]]>");
        w.setContent("<![CDATA[你好]]>");
        XStream x = new XStream();
        x.processAnnotations(WxEventMsgReply.TextMsg.class);
        String s1 = x.toXML(w);
        System.out.println(s1);
    }
    @Test
    public void testToXml2(){
        WxEventMsgReply.ImageMsg w = new WxEventMsgReply.ImageMsg();
        w.setToUserName("1");
        w.setFromUserName("2");
        w.setCreateTime(12323);
        w.setMsgType("3");

        WxEventMsgReply.Image ii =  new WxEventMsgReply.Image();
        ii.setMediaId("12");
        XStream x = new XStream();
        w.setImage(ii);
        x.alias("xml",WxEventMsgReply.ImageMsg.class);
        String s1 = x.toXML(w);
        System.out.println(s1);

    }

    @Test
    public void teset(){
        //普通文本消息
        TextMessage textMessage=new TextMessage();
        textMessage.setToUserName("<![CDATA["+123+"]]>");
        textMessage.setFromUserName("<![CDATA["+123+"]]>");
        textMessage.setCreateTime(123);
        textMessage.setMsgType("<![CDATA["+"text"+"]]>");
        textMessage.setContent("<![CDATA["+"nihao"+"]]>");
        String s = RespBaseMessage.textMessageToXml(textMessage);
        System.out.println(s);
    }

    @Test
    public void te(){
        String s = null;
        String s1 = StringUtils.substringAfter(s, "_");
        System.out.println(s1);
    }
}
