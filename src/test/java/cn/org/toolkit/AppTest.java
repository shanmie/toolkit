package cn.org.toolkit;


import cn.org.toolkit.files.FileStored;
import cn.org.toolkit.guava.GuavaCacheManager;
import cn.org.toolkit.redisson.RedissonManager;
import cn.org.toolkit.result.m1.ResultTemplate;
import cn.org.toolkit.shell.ShellExec;
import cn.org.toolkit.token.JwtToken;
import cn.org.toolkit.utility.ListUtility;
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

import static cn.org.toolkit.files.FileStored.*;


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
        Map<String,Object> map = new HashMap<>();
        map.put("user",null);
        map.put("re",null);
        map.put("s",null);
        System.out.println(ResultTemplate.ok(map));
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
    public void testFilesSupport(){
        Map<String,Object> map = new HashMap<>();
        map.put("123","123");
        map.put("456","你好");
        map.put("654","654");
        Map<String,Object> map1 = new HashMap<>();
        map1.put("1213","11123");
        map1.put("4516","多少");
        map1.put("6514","6514");
        List<StringBuilder> builderList1 = FileStored. transform(Arrays.asList(map,map1));

        writeCsv("/Users/admin/Desktop/11111.csv",builderList1, Arrays.asList("我是csv表头", "2", "3"));
        writeCsv("/Users/admin/Desktop/22222.csv", "UTF-8",builderList1, Arrays.asList("我是csv表头", "2", "3"));
        writeTxt("/Users/admin/Desktop/33333.txt", builderList1, Arrays.asList("我是txt表头", "2", "3"));

    }
    @Test
    public void testFilesSupportRead(){
        String s = " ";
        System.out.println(s.length());
        List<CSVRecord> read = FileStored.read("/Users/admin/Desktop/2222.txt", Arrays.asList("1", "2", "3"), true);
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
    @Test
    public void testShellExe(){
        ShellExec s = new ShellExec("ls");
        s.addArg(" -l").exec();
        ShellExec.exec("pwd");
    }
    @Test
    public void test(){
        String s ="";
        String[] strings = ListUtility.get(s);
        List<String> string = ListUtility.getString(s);
        String stringFirst = ListUtility.getStringFirst(s, "11");
        System.out.println(strings.toString());
        System.out.println(string);
        System.out.println(stringFirst);
        String b = "123,2323,455,34343 抓紧时间点击添加微信直达分享会";
        int integerFirst = ListUtility.getIntegerFirst(b, 0);
        int integerLast = ListUtility.getIntegerLast(b, 0);
        System.out.println(integerFirst);
        System.out.println(integerLast);

    }

}
