package cn.org.toolkit;


import cn.org.toolkit.files.FileHelper;
import cn.org.toolkit.guava.GuavaCacheManager;
import cn.org.toolkit.redisson.RedissonManager;
import cn.org.toolkit.result.m1.ResultTemplate;
import cn.org.toolkit.shell.ShellExec;
import cn.org.toolkit.token.JwtToken;
import cn.org.toolkit.utility.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.googlecode.javaewah.EWAHCompressedBitmap;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import io.netty.buffer.ByteBufUtil;
import jodd.util.ArraysUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.redisson.Redisson;
import org.redisson.RedissonLocalCachedMap;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.chrono.ChronoPeriod;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

import static cn.org.toolkit.files.FileHelper.transform;
import static cn.org.toolkit.files.FileHelper.writeCsv;
import static cn.org.toolkit.files.FileHelper.writeTxt;


/**
 * Unit test for simple App.
 */
@Slf4j
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
        List<StringBuilder> builderList1 = transform(Arrays.asList(map, map1));

       /* writeCsv("/Users/admin/Desktop/11111.csv", builderList1, Arrays.asList("我是csv表头", "2", "3"));
        writeCsv("/Users/admin/Desktop/22222.csv", "UTF-8", builderList1, Arrays.asList("我是csv表头", "2", "3"));
        writeTxt("/Users/admin/Desktop/33333.txt", builderList1, Arrays.asList("我是txt表头", "2", "3"));*/
        //writeTxt("/Users/admin/Desktop/33333.txt", builderList1, Arrays.asList("我是txt表头", "2", "3"));
        writeCsv("/Users/admin/Desktop/22222.csv", "UTF-8", builderList1, Arrays.asList("我是csv表头", "2", "3"));
        A a = new A();
        a.setA("12");
        a.setB("22");
        A a1 = new A();
        a1.setA("12111");
        a1.setB("221111");

        List<StringBuilder> transform = transform(Arrays.asList(a, a1), A.class);
        System.out.println(transform);

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
            URL url = new URL("http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTL361ia1tEEJGic1aKwn9EufnywamqIXaIsj8v4FGmZ1zGweIYw6UThB5XSicvpXfLN6m4rSNfHA1xBA/132");
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

    @Test
    public void test2(){
        int a  = 10;
        System.out.println(Integer.toBinaryString(a));
        int i = a >> 3;
        System.out.println(i);
    }
    @Test
    public void test3(){
        long l = DateUtility.toMillis(LocalDateTime.of(2019, 5, 31, 23, 59));
        System.out.println(l);
        String s = DateUtility.toString(l);
        System.out.println(s);


        long now = 1557713432475l;
        long l1 = now + 2000;
        System.out.println("now---"+DateUtility.toString(now));
        System.out.println("plus---"+DateUtility.toString(l1));

        LocalDateTime  ll= DateUtility.toLocalDateTime(1557713432475l);


        LocalDateTime lll = ll.plusDays(2);
        System.out.println("48 hours"+DateUtility.toString(lll));
        System.out.println(DateUtility.toString(lll.minusSeconds(120)));

        long time = 24 * 3600 * 2 - 3600;
        System.out.println(time * 1000);

    }
    @Test
    public void test4() throws IOException {
        LocalDateTime clock = LocalDateTime.now().plusMinutes(10);
        System.out.println(DateUtility.toString(clock)+"--long:"+DateUtility.toMillis(clock));
        long l = DateUtility.toMillis(LocalDateTime.now()) ;
        System.out.println(l);

        System.out.println(DateUtility.toString(l));
        long ll =1557898260741l;
        System.out.println(l/1000);
        System.out.println(ll/1000);

        long llll = 1557774000000l;
        System.out.println(DateUtility.toString(llll));
        LocalTime localTime = DateUtility.toLocalDateTime(llll).toLocalTime();

        System.out.println(localTime);
        long lll = (25983489l+10) * 1000 * 60 ;
        System.out.println(DateUtility.toString(lll));

        System.out.println(DateUtility.toString(l-1000));
    }
    @Test
    public void googleBitMap() {
        EWAHCompressedBitmap ewahBitmap1 =EWAHCompressedBitmap.bitmapOf(0, 2, 55, 64, 1<<30);
        EWAHCompressedBitmap ewahBitmap2 =EWAHCompressedBitmap.bitmapOf(1, 3, 64, 1<<30);
        System.out.println("bitmap 1: "+ ewahBitmap1);
        System.out.println("bitmap 2: "+ ewahBitmap2);
        EWAHCompressedBitmap orbitmap = ewahBitmap1.or(ewahBitmap2);
        System.out.println("bitmap 1 OR bitmap 2: "+ orbitmap);
        System.out.println("memory usage: "+ orbitmap.sizeInBytes() +" bytes");

       /* ByteArrayOutputStream bos =new ByteArrayOutputStream();
        // Note: you could use a file output steam instead of ByteArrayOutputStream ewahBitmap1.serialize(newDataOutputStream(bos));
        EWAHCompressedBitmap ewahBitmap1new =new EWAHCompressedBitmap();
        byte[] bout = bos.toByteArray();
        ewahBitmap1new.deserialize(new DataInputStream(new ByteArrayInputStream(bout)));
        System.out.println("bitmap 1 (recovered) : "+ ewahBitmap1new);
        if (!ewahBitmap1.equals(ewahBitmap1new)) throw new RuntimeException("Will not happen");*/
        //// we can use a ByteBuffer as backend for a bitmap// which allows memory-mapped bitmaps//ByteBuffer bb =ByteBuffer.wrap(bout);

        Map<String,Object> map=new HashMap<>();
        map.put("text","Hi~ 恭喜您赢得分享会限量体验名额！✌️\n" +
                "\n" +
                "⏰抓紧时间\uD83D\uDC49<a href=\"https://f11.sjbly.cn/y19/0422/1916/00yta_o.jpg\">点击添加微信直达分享会</a>\n" +
                "\n" +
                "【添加微信您将获得】\n" +
                "✅3期分享会精品PDF干货资源；\n" +
                "✅1对1私人自由行私享定制体验；\n" +
                "\n" +
                "欢迎将海报转发给好友，一起参加分享会！\uD83C\uDF0D");
        map.put("text_customer","VjI-g52wYC-j4Y_XXCRsOrZeRuT-39RsKax4PsK8XzI");
        Object o = JSONObject.toJSON(map);
        System.out.println(o);

    }

    @Test
    public void test5(){

        byte a =123;
        System.out.println(ByteUtility.toString(a));



    }

    @Test
    public void test6() throws IOException {
        long l = DateUtility.toMillis(DateUtility.toDate("2019-06-30 23:59:00"));
        System.out.println(l);

        System.out.println(DateUtility.toString(1561909008124l));
    }

    @Test
    public void test7() {
        String s="{\n" +
                "    \"code\": 0,\n" +
                "    \"msg\": \"success\",\n" +
                "    \"data\": {\n" +
                "        \"S06476852600207579\": [\n" +
                "            {\n" +
                "                \"order_sub_id\": \"S06476852600207579\",\n" +
                "                \"cat_id\": \"113001\",\n" +
                "                \"order_type\": \"package\",\n" +
                "                \"plan_type\": 0,\n" +
                "                \"order_status\": \"1\",\n" +
                "                \"user_id\": \"207579\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"order_sub_id\": \"S06476852600207578\",\n" +
                "                \"cat_id\": \"113221\",\n" +
                "                \"order_type\": \"package\",\n" +
                "                \"plan_type\": 0,\n" +
                "                \"order_status\": \"1\",\n" +
                "                \"user_id\": \"12121\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"S06476852600207573\": [\n" +
                "            {\n" +
                "                \"order_sub_id\": \"S06476852600207579\",\n" +
                "                \"cat_id\": \"113301\",\n" +
                "                \"order_type\": \"package\",\n" +
                "                \"plan_type\": 0,\n" +
                "                \"order_status\": \"1\",\n" +
                "                \"user_id\": \"203579\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        List<Map<String, Object>> mapList = JsonUtility.dynamicParseArray(s,"data","cat_id","user_id");

        System.out.println(mapList);



    }


    @Test
    public void test8() throws Exception {
        String s = "{\"code\":0,\"msg\":\"\",\"data\":[]}";
        JSONObject parseObject = JSONObject.parseObject(s);
        System.out.println(parseObject);
        Object data = parseObject.get("data");
        if (data instanceof Map){
            System.out.println(123);

        }

    }

    @Test
    public void test9(){

        List<Long> transform = ListUtility.transform(Arrays.asList("12"), Long.class);
        System.out.println(transform);

    }

}
