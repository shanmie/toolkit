package cn.org.toolkit;

import lombok.Data;

/**
 * @author deacon
 * @since 2019/4/24
 */
@Data
public class WxEventMsg {
    String ToUserName;
    String FromUserName;
    String CreateTime;
    String MsgType;
    String Event;
    String EventKey;
    String Ticket;
}
