package cn.org.toolkit;


import cn.org.toolkit.lock.RDistributedLock;
import cn.org.toolkit.redisson.RedissonManager;
import cn.org.toolkit.result.m1.ResultTemplate;
import cn.org.toolkit.token.JwtToken;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;

import java.util.HashMap;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCrypto() {
        String pw = BCrypt.hashpw("12333", BCrypt.gensalt());
        System.out.println(pw);
        boolean checkpw = BCrypt.checkpw("123", pw);
        boolean checkpw2 = BCrypt.checkpw("12333", pw);
        System.out.println(checkpw);
        System.out.println(checkpw2);
    }
    @Test
    public void testJwt(){
        String name = "admin";
        String role = "SYS_ADMIN";
        String clientID = "123";
        String entityName = "entityName";
        String loginPassword = "loginPassword";

        //拼装accessToken
        String accessToken = JwtToken.createJWT(loginPassword, name, role, clientID, entityName, 604800, "mySecret");
        System.out.println(accessToken);
    }
    @Test
    public void testRedis(){
        Redisson red = RedissonManager.getRedisson();
        RAtomicLong aLong = red.getAtomicLong("hello");
        aLong.set(2);
        System.out.println(aLong);
    }
    @Test
    public void testResultTemplate(){
        System.out.println(JSONObject.toJSONString(ResultTemplate.ok()));
        System.out.println(JSONObject.toJSONString(ResultTemplate.ok(new HashMap() {{put("data","data");}})));
    }
    @Test
    public void testLock(){
        RDistributedLock.lock("",1l);
    }
    @Test
    public void testGuavaCache() throws InterruptedException {


    }
}
