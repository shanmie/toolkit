package cn.org.toolkit.shell;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import sun.awt.OSInfo;

import java.io.IOException;

/**
 * @author deacon
 * @since 2019/4/9
 */
@Slf4j
public class ShellExec {
    public static void exec(String args) {
        try {
            if (OSInfo.OSType.WINDOWS == OSInfo.getOSType()) {
                log.info("暂时不支持windows命令");
                return;
            } else if (OSInfo.OSType.UNKNOWN == OSInfo.getOSType()) {
                log.info("不认识此操作系统");
                return;
            }
            log.info("current os={}, thread name={},thread id={}", OSInfo.getOSType().name(),
                    Thread.currentThread().getName(), Thread.currentThread().getId());
            CommandLine cmd = CommandLine.parse(args);
            DefaultExecutor dex = new DefaultExecutor();
            dex.execute(cmd);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    public static void stop(){
        System.exit(1);
    }
}
