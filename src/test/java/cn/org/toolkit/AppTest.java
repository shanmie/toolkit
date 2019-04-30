package cn.org.toolkit;


import cn.org.toolkit.files.FileHelper;
import cn.org.toolkit.guava.GuavaCacheManager;
import cn.org.toolkit.redisson.RedissonManager;
import cn.org.toolkit.result.m1.ResultTemplate;
import cn.org.toolkit.shell.ShellExec;
import cn.org.toolkit.token.JwtToken;
import cn.org.toolkit.utility.ArrayUtility;
import cn.org.toolkit.utility.ListUtility;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBufUtil;
import jodd.util.ArraysUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collector;

import static cn.org.toolkit.files.FileHelper.writeCsv;
import static cn.org.toolkit.files.FileHelper.writeTxt;


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
    public void testJwt() {
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
    public void testRedis() {
        Redisson red = RedissonManager.getInstance();
        RAtomicLong aLong = red.getAtomicLong("hello");
        aLong.set(2);
        System.out.println(aLong);
        RBucket<Object> bucket = red.getBucket("");//将key存入redis
        //bucket.set();
    }

    @Test
    public void testResultTemplate() {
        System.out.println(JSONObject.toJSONString(ResultTemplate.ok()));
        System.out.println(JSONObject.toJSONString(ResultTemplate.ok(new HashMap() {{
            put("data", "data");
        }})));
        Map<String, Object> map = new HashMap<>();
        map.put("user", null);
        map.put("re", null);
        map.put("s", null);
        System.out.println(ResultTemplate.ok(map));
    }

    @Test
    public void testGuavaCache() throws InterruptedException {
        Cache<Object, Object> cache = GuavaCacheManager.getCacheExpire(2, 3);
        cache.stats();
        cache.put("aa", "ds");
        cache.put("bb", "ds");
        cache.put("cc", "ds");
        System.out.println(cache.getIfPresent("aa"));
        System.out.println(cache.getIfPresent("bb"));
        System.out.println(cache.getIfPresent("cc"));
        while (true) {
            Thread.sleep(5000);
            System.out.println(cache.getIfPresent("aa"));
            System.out.println(cache.getIfPresent("bb"));
            System.out.println(cache.getIfPresent("cc"));
            System.out.println(GuavaCacheManager.getValue("123"));
            System.out.println(GuavaCacheManager.getValue(456));
        }

    }

    @Test
    public void testFilesSupport() {
        Map<String, Object> map = new HashMap<>();
        map.put("123", "123");
        map.put("456", "你好");
        map.put("654", "654");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("1213", "11123");
        map1.put("4516", "多少");
        map1.put("6514", "6514");
        List<StringBuilder> builderList1 = FileHelper.transform(Arrays.asList(map, map1));

        writeCsv("/Users/admin/Desktop/11111.csv", builderList1, Arrays.asList("我是csv表头", "2", "3"));
        writeCsv("/Users/admin/Desktop/22222.csv", "UTF-8", builderList1, Arrays.asList("我是csv表头", "2", "3"));
        writeTxt("/Users/admin/Desktop/33333.txt", builderList1, Arrays.asList("我是txt表头", "2", "3"));

    }

    @Test
    public void testFilesSupportRead() {
        String s = " ";
        System.out.println(s.length());
        List<CSVRecord> read = FileHelper.read("/Users/admin/Desktop/2222.txt", Arrays.asList("1", "2", "3"), true);
        System.out.println(read);
        for (CSVRecord c : read) {
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
    public void testShellExe() {
        ShellExec s = new ShellExec("ls");
        s.addArg(" -l").exec();
        ShellExec.exec("pwd");
    }

    @Test
    public void testUtility() {
        String s [] ={"aa"};
        Integer a[] ={11};
        List<Integer> integers = ArrayUtility.toList(a);
        List<String> list = ArrayUtility.toList(s);
        System.out.println(integers);
        System.out.println(list);
        String ss = "qw,12,ww,11";
        System.out.println(ArrayUtils.toString(ListUtility.get(ss)));
        System.out.println(ListUtility.getIndex(list,1,"1"));
        System.out.println(ListUtility.getIndex(integers,1,9));
        System.out.println(ListUtility.getInteger(ss));
        System.out.println(ListUtility.getLong(ss));
        System.out.println(ListUtility.getIntegerFirst(ss,0));
        System.out.println(ListUtility.getIntegerLast(ss,0));
    }

    @Test
    public void test() throws IOException {
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        try {
            // 创建URL
            URL url = new URL("http://test1.sjbly.cn/m19/0429/1447/tjf_120x120_b.jpg");
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("不存在");
            }
            InputStream inStream = conn.getInputStream();
            int len = -1;
            while ((len = inStream.read(data)) != -1) {
                outPut.write(data, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        String s = Base64.getEncoder().encodeToString(outPut.toByteArray());


        System.out.println(s);
    }




}
