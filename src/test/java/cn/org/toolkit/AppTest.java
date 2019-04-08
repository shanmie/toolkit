package cn.org.toolkit;


import cn.org.toolkit.files.FileSupport;
import cn.org.toolkit.guava.GuavaCacheManager;
import cn.org.toolkit.redisson.RedissonManager;
import cn.org.toolkit.result.m1.ResultTemplate;
import cn.org.toolkit.token.JwtToken;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;

import java.util.*;

import static cn.org.toolkit.files.FileSupport.transform;
import static cn.org.toolkit.files.FileSupport.*;


/**
 * Unit test for simple App.
 */
public class AppTest {
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
        Redisson red = RedissonManager.getInstance();
        RAtomicLong aLong = red.getAtomicLong("hello");
        aLong.set(2);
        System.out.println(aLong);
        RBucket<Object> bucket = red.getBucket("");//将key存入redis
        //bucket.set();
    }
    @Test
    public void testResultTemplate(){
        System.out.println(JSONObject.toJSONString(ResultTemplate.ok()));
        System.out.println(JSONObject.toJSONString(ResultTemplate.ok(new HashMap() {{put("data","data");}})));
    }
    @Test
    public void testGuavaCache() throws InterruptedException {
        Cache<Object, Object> cache = GuavaCacheManager.getCacheExpire(2,3);
        cache.stats();
        cache.put("aa","ds");
        cache.put("bb","ds");
        cache.put("cc","ds");
        System.out.println(cache.getIfPresent("aa"));
        System.out.println(cache.getIfPresent("bb"));
        System.out.println(cache.getIfPresent("cc"));
        while (true){
            Thread.sleep(5000);
            System.out.println(cache.getIfPresent("aa"));
            System.out.println(cache.getIfPresent("bb"));
            System.out.println(cache.getIfPresent("cc"));
            System.out.println(GuavaCacheManager.getValue("123"));
            System.out.println(GuavaCacheManager.getValue(456));
        }

    }
    @Test
    public void testFilesHandler(){
        List<A> list = new ArrayList<>();
        A a = new A();
        a.setA("你好");
        a.setB("ss");
        a.setC(123);
        list.add(a);
        A a1 = new A();
        a1.setA("sd1s");
        a1.setB("s1s");
        a1.setC(1213);
        list.add(a1);
        A a2 = new A();
        a2.setA("234");
        a2.setB("32");
        a2.setC(12323213);
        list.add(a2);


        List<StringBuilder> builderList = transform(list, "cn.org.toolkit.A");
        Map<String,Object> map = new HashMap<>();
        map.put("123","123");
        map.put("456","456");
        map.put("654","654");
        Map<String,Object> map1 = new HashMap<>();
        map1.put("1213","11123");
        map1.put("4516","45116");
        map1.put("6514","6514");
        List<StringBuilder> builderList1 =FileSupport. transform(Arrays.asList(map,map1));

        writeCsv("/Users/admin/Desktop/11111.csv", builderList, Arrays.asList("我是csv表头", "2", "3"));
        writeTxt("/Users/admin/Desktop/2222.txt", builderList, Arrays.asList("我是txt表头", "2", "3"));



    }
    @Test
    public void test(){
        String s = " ";
        System.out.println(s.length());
        List<CSVRecord> read = FileSupport.read("/Users/admin/Desktop/2222.txt", Arrays.asList("1", "2", "3"), true);
        System.out.println(read);
        for (CSVRecord c:read) {
            String s1 = c.toString();
            Map<String, String> stringStringMap = c.toMap();
            String comment = c.getComment();
            JSONObject parseObject = JSONObject.parseObject(JSON.toJSONString(c));
            System.out.println(parseObject);

            String values = c.get("values");
            System.out.println(values);
        }
    }

}
