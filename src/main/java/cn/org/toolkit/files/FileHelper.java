package cn.org.toolkit.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author deacon
 * @since 2019/3/18
 * 支持 txt csv 文件写入和文件内容读取
 */

@Slf4j
public class FileHelper {

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
     * @param clazzName 内部参数类型 实体类
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
                    //访问私有变量
                    fields[i].setAccessible(true);
                    builder.append(fields[i].get(c));
                    if (i == i1 - 1) {
                        continue;
                    }
                    builder.append(",");
                }
                if (i1==0){
                    return null;
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
     * 写文件 txt
     * @param filePath
     * @param fileHeader 文件头
     */
    public static void writeTxt(final String filePath, List<StringBuilder> builders, List<String> fileHeader) {
        try {
            FileWriter bufferedWriter = new FileWriter(filePath);
            CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.EXCEL.withHeader(fileHeader.toArray(new String[fileHeader.size()])));
            for (StringBuilder str : builders) {
                csvPrinter.printRecord(str);
            }
            log.info("====> write txt file success path is={}", filePath);
            csvPrinter.flush();
            csvPrinter.close();
        } catch (Exception e) {
            log.error("write file error", e);
        }
    }


    public static void writeCsv(final String filePath, List<StringBuilder> builders, List<String> fileHeader) {
        writeCsv(filePath,"GBK",builders,fileHeader);
    }

    /**
     * 写文件 csv 可针对不同的操作系统不同的编码设置对应excel或csv编码
     * 如果实在不行 Linux/mac系统可以配合脚本转换 也可调ShellExec类函数
     * @param charsetName
     * @param filePath
     * @param builders
     * @param fileHeader
     */
    public static void writeCsv(final String filePath, final String charsetName, List<StringBuilder> builders, List<String> fileHeader) {
        try {
            BufferedWriter out = Files.newBufferedWriter(Paths.get(filePath), Charset.forName(charsetName));
            CSVPrinter csvPrinter = new CSVPrinter(out, CSVFormat.EXCEL.withHeader(fileHeader.toArray(new String[fileHeader.size()])));
            for (StringBuilder str : builders) {
                String[] split = StringUtils.split(str.toString(), ',');
                csvPrinter.printRecord(Arrays.asList(split));
            }
            csvPrinter.flush();
            csvPrinter.close();
            log.info("====> write csv file success path is={}", filePath);
        } catch (Exception e) {
            log.error("write file error", e);
        }
    }

    /**
     *
     * @param filePath
     * @param fileHeader
     * @param skipHeader
     * @return
     */
    public static List<CSVRecord> read(final String filePath, List<String> fileHeader, boolean skipHeader) {
        try {
            if (StringUtils.isBlank(filePath)){
                return Collections.emptyList();
            }
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            if (skipHeader) {
                CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(fileHeader.toArray(new String[fileHeader.size()])).withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
                CSVParser csvParser = new CSVParser(reader, csvFormat);
                return csvParser.getRecords();
            } else {
                CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(fileHeader.toArray(new String[fileHeader.size()])).withIgnoreHeaderCase().withTrim();
                CSVParser csvParser = new CSVParser(reader, csvFormat);
                return csvParser.getRecords();
            }
        } catch (Exception e) {
            log.error("read file error",e);
        }
        return Collections.emptyList();
    }



}
