package cn.org.toolkit.utility;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * @author deacon
 * @since 2019/4/30
 */
public class ByteUtility {
    /**
     * int转byte数组
     *
     * @param value   int value
     * @param hex     is true use hex
     * @return
     */
    public static byte[] toBytes(int value, boolean hex) {
        byte[] src = new byte[4];
        if (hex) {
            src[0] = (byte) ((value >> 24) & 0xFF);
            src[1] = (byte) ((value >> 16) & 0xFF);
            src[2] = (byte) ((value >> 8) & 0xFF);
            src[3] = (byte) (value & 0xFF);
        } else {
            src[0] = (byte) (value >> 24);
            src[1] = (byte) (value >> 16);
            src[2] = (byte) (value >> 8);
            src[3] = (byte) (value);
        }
        return src;
    }


    /**
     * 字符串转二进制数组
     *
     * @param mac
     * @return
     */
    public static byte[] toByte(String mac) {
        byte[] data = new byte[mac.length() / 2];
        for (int i = 0; i < mac.length(); i += 2) {
            String ss = mac.substring(i, i + 2);
            data[i / 2] = Integer.valueOf(ss, 16).byteValue();
        }
        return data;
    }

    /**
     * 将short转成byte[2]
     *
     * @param a
     * @return
     */
    public static byte[] toByte(short a) {
        byte[] b = new byte[2];
        b[0] = (byte) (a >> 8);
        return b;
    }

    /**
     * 将short转成byte[2]
     * b[1] = (byte) (a);
     *
     * @param a
     * @param b
     * @param offset b中的偏移量
     */
    public static void toByte(short a, byte[] b, int offset) {
        b[offset] = (byte) (a >> 8);
        b[offset + 1] = (byte) (a);
    }

    /**
     * long转byte[8]
     *
     * @param a
     * @param b
     * @param offset b的偏移量
     */
    public static void toByte(long a, byte[] b, int offset) {
        b[offset + 0] = (byte) (a >> 56);
        b[offset + 1] = (byte) (a >> 48);
        b[offset + 2] = (byte) (a >> 40);
        b[offset + 3] = (byte) (a >> 32);

        b[offset + 4] = (byte) (a >> 24);
        b[offset + 5] = (byte) (a >> 16);
        b[offset + 6] = (byte) (a >> 8);
        b[offset + 7] = (byte) (a);
    }

    /**
     * long转byte[8]
     *
     * @param a
     * @return
     */
    public static byte[] toByte(long a) {
        byte[] b = new byte[4 * 2];
        b[0] = (byte) (a >> 56);
        b[1] = (byte) (a >> 48);
        b[2] = (byte) (a >> 40);
        b[3] = (byte) (a >> 32);

        b[4] = (byte) (a >> 24);
        b[5] = (byte) (a >> 16);
        b[6] = (byte) (a >> 8);
        b[7] = (byte) (a >> 0);

        return b;
    }

    /**
     * 将byte[2]转换成short
     *
     * @param b
     * @return
     */
    public static short toShort(byte[] b) {
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }

    /**
     * 将byte[2]转换成short
     *
     * @param b
     * @param offset
     * @return
     */
    public static short toShort(byte[] b, int offset) {
        return (short) (((b[offset] & 0xff) << 8) | (b[offset + 1] & 0xff));
    }

    /**
     * byte数组转int
     *
     * @param b
     * @return
     */
    public static int toInt(byte[] b) {
        return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16)
                | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
    }

    /**
     * byte数组转int
     *
     * @param b
     * @param offset
     * @return
     */
    public static int toInt(byte[] b, int offset) {
        return ((b[offset++] & 0xff) << 24) | ((b[offset++] & 0xff) << 16)
                | ((b[offset++] & 0xff) << 8) | (b[offset++] & 0xff);
    }

    /**
     * 十六进制字符串转化为十进制
     *
     * @param hex
     * @return
     */
    public static int toInt(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    /**
     * byte[8]转long
     *
     * @param b
     * @param offset b的偏移量
     * @return
     */
    public static long toLong(byte[] b, int offset) {
        return ((((long) b[offset + 0] & 0xff) << 56)
                | (((long) b[offset + 1] & 0xff) << 48)
                | (((long) b[offset + 2] & 0xff) << 40)
                | (((long) b[offset + 3] & 0xff) << 32)

                | (((long) b[offset + 4] & 0xff) << 24)
                | (((long) b[offset + 5] & 0xff) << 16)
                | (((long) b[offset + 6] & 0xff) << 8) | (((long) b[offset + 7] & 0xff) << 0));
    }

    /**
     * byte[8]转long
     *
     * @param b
     * @return
     */
    public static long toLong(byte[] b, boolean m) {
        if (m) {
            return ((b[0] & 0xff) << 56) | ((b[1] & 0xff) << 48)
                    | ((b[2] & 0xff) << 40) | ((b[3] & 0xff) << 32) |

                    ((b[4] & 0xff) << 24) | ((b[5] & 0xff) << 16)
                    | ((b[6] & 0xff) << 8) | (b[7] & 0xff);
        } else {
            long a = 0;
            a |= (long) (b[0] & 0xFF);
            a |= (long) (b[1] & 0xFF) << 8;
            a |= (long) (b[2] & 0xFF) << 16;
            a |= (long) (b[3] & 0xFF) << 24;
            a |= (long) (b[4] & 0xFF) << 32;
            a |= (long) (b[5] & 0xFF) << 40;
            a |= (long) (b[6] & 0xFF) << 48;
            a |= (long) (b[7] & 0xFF) << 56;
            return a;
        }
    }

    /**
     * byte[] to string
     * @param val
     * @return
     */
    public static String toString(byte[] val){
        return StringUtils.toEncodedString(val,Charset.defaultCharset());
    }

    /**
     * byte[] to string
     * @param val       byte []
     * @param charSet   charset
     * @return
     */
    public static String toString(byte[] val,String charSet){
        if (StringUtils.isBlank(charSet)){
            toString(val);
        }
        return StringUtils.toEncodedString(val,Charset.forName(charSet));
    }
}
