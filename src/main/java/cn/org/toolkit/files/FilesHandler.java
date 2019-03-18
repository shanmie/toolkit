package cn.org.toolkit.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author deacon
 * @since 2019/3/18
 */

@Slf4j
public class FilesHandler {

    /**
     * 数据转换
     * @param ls
     * @return
     */
    public static List<StringBuilder> transform(List<Map<String, Object>> ls) {
        List<StringBuilder> builderList = new ArrayList<>();
        for (Map<String, Object> map : ls) {
            int i = 0;
            int size = map.size();
            StringBuilder builder = new StringBuilder();
            for (String key : map.keySet()) {
                Object o = map.get(key);
                builder.append(o);
                i++;
                if (i == size) {
                    continue;
                }
                builder.append(",");
            }
            builderList.add(builder);
        }
        return builderList;
    }


    /**
     * 数据转换 暂时不支持子父类
     * @param ls
     * @param clazzName 全类名
     * @return
     */
    public static List<StringBuilder> transform(List<?> ls, final String clazzName) {
        if (null == ls) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(clazzName);
            List<StringBuilder> builderList = new ArrayList<>();
            for (Object c : ls) {
                Field[] fields = clazz.getDeclaredFields();
                StringBuilder builder = new StringBuilder();
                int i1 = fields.length;
                for (int i = 0; i < fields.length; i++) {
                    /** 过滤静态属性**/
                    if (Modifier.isStatic(fields[i].getModifiers())) {
                        continue;
                    }
                    /** 过滤transient 关键字修饰的属性**/
                    if (Modifier.isTransient(fields[i].getModifiers())) {
                        continue;
                    }
                    builder.append(fields[i].get(c));
                    if (i == i1 - 1) {
                        continue;
                    }
                    builder.append(",");
                }
                builderList.add(builder);
            }
            return builderList;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 写文件 支持 txt
     * @param filePath
     * @param fileHeader 文件头
     */
    public static CSVPrinter write(final String filePath, List<StringBuilder> builders, List<String> fileHeader) {
        try {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(filePath), Charset.forName("utf-8"));
            CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withHeader(fileHeader.toArray(new String[fileHeader.size()])));
            for (StringBuilder str : builders) {
                csvPrinter.printRecord(str);
            }
            log.info("====> write file success path is={}", filePath);
            csvPrinter.flush();
            return csvPrinter;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

}
