package tech.mhuang.ext.netty.utils;

import java.io.UnsupportedEncodingException;

/**
 *
 * BCD码工具类
 *
 * @author mhuang
 * @since 1.0.0
 */
public class BCDHelper {

    /**
     * 用BCD码压缩数字字符串
     * @param str 压缩的字符串
     * @return
     */
    public static byte[] StrToBCD(String str) {

        return StrToBCD(str, str.length());
    }

    public static byte[] StrToBCD(String str, int numlen) {
        if (numlen % 2 != 0)
            numlen++;

        while (str.length() < numlen) {
            str = "0" + str;
        }

        byte[] bStr = new byte[str.length() / 2];
        char[] cs = str.toCharArray();
        int i = 0;
        int iNum = 0;
        for (i = 0; i < cs.length; i += 2) {

            int iTemp = 0;
            if (cs[i] >= '0' && cs[i] <= '9') {
                iTemp = (cs[i] - '0') << 4;
            } else {
                // 判断是否为a~f
                if (cs[i] >= 'a' && cs[i] <= 'f') {
                    cs[i] -= 32;
                }
                iTemp = (cs[i] - '0' - 7) << 4;
            }
            // 处理低位
            if (cs[i + 1] >= '0' && cs[i + 1] <= '9') {
                iTemp += cs[i + 1] - '0';
            } else {
                // 判断是否为a~f
                if (cs[i + 1] >= 'a' && cs[i + 1] <= 'f') {
                    cs[i + 1] -= 32;
                }
                iTemp += cs[i + 1] - '0' - 7;
            }
            bStr[iNum] = (byte) iTemp;
            iNum++;
        }
        return bStr;

    }

    public static String FStrLen(String str, int len) {
        if (str.length() < len) {// String.format("%04X", Sdd & 0xFFFF);
            str = "0" + str;
        }
        return str;
    }

    private static String hexString="0123456789ABCDEF";

    private static int hex2Dec(char ch)
    {
        if(ch == '0') return 0;
        if(ch == '1') return 1;
        if(ch == '2') return 2;
        if(ch == '3') return 3;
        if(ch == '4') return 4;
        if(ch == '5') return 5;
        if(ch == '6') return 6;
        if(ch == '7') return 7;
        if(ch == '8') return 8;
        if(ch == '9') return 9;
        if(ch == 'a') return 10;
        if(ch == 'A') return 10;
        if(ch == 'B') return 11;
        if(ch == 'b') return 11;
        if(ch == 'C') return 12;
        if(ch == 'c') return 12;
        if(ch == 'D') return 13;
        if(ch == 'd') return 13;
        if(ch == 'E') return 14;
        if(ch == 'e') return 14;
        if(ch == 'F') return 15;
        if(ch == 'f') return 15;
        else return -1;

    }

    public static String BytesToHexString(byte[] bts) {
        return BytesToHexString(bts, 0, bts.length);
    }


    public static String BytesToHexString(byte[] bts, int offset, int count) {
        StringBuilder sb = new StringBuilder(bts.length * 2);
        for (int i = 0; i < count; i++) {
            sb.append(Integer.toHexString(bts[i + offset]));
        }
        return sb.toString();
    }

    public static int BoolToByte(boolean bl) {

        return bl ? 1 : 0 << 0;
    }


    public static byte[] encode_ret_bytes(String str) throws UnsupportedEncodingException {
        //根据默认编码获取字节数组
        byte[] bytes=str.getBytes("gbk");
        return bytes;
    }

    public static String encode(String str) throws UnsupportedEncodingException {
        //根据默认编码获取字节数组
        byte[] bytes=str.getBytes("gbk");
        StringBuilder sb=new StringBuilder(bytes.length*2);
        //将字节数组中每个字节拆解成2位16进制整数
        for(int i=0;i<bytes.length;i++)
        {

            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
        }
        return sb.toString();
    }


    public static String decode(String hexStr)
    {
        if( null==hexStr || "".equals(hexStr) || (hexStr.length())%2 != 0 )
        {
            return null;
        }

        int byteLength = hexStr.length() /2;
        byte[] bytes=new byte[ byteLength ];


        int temp=0;
        for(int i=0;i<byteLength;i++)
        {
            temp = hex2Dec(hexStr.charAt(2*i))*16+hex2Dec(hexStr.charAt(2*i+1));
            bytes[i]=(byte)( temp<128 ? temp : temp-256 ) ;
        }
        return new String(bytes);


    }

    public static String word2String(int value) {
        int cmp = 0xF000;
        int i = 1;
        value &= 0xffff;//表明是WORD类型，过滤掉前面无用数据
        String ret = "";

        while((value&cmp) == 0)
        {
            ret += "0";
            cmp >>= 4;
            i++;
            if(i>4)
                break;
        }
        if(i<5)
        {
            ret += Integer.toHexString(value);
        }
        return  ret;
    }

    public  static String dword2String(long value) {
        int cmp = 0xF0000000;
        int i = 1;
        value &= 0xffffffff;//dword类型，过滤掉前面无用数据
        String ret = "";

        while((value&cmp) == 0)
        {
            ret += "0";
            cmp >>= 4;
            i++;
            if(i>8)
                break;
        }
        if(i<9)
        {
            ret += Long.toHexString(value);
        }
        return  ret;
    }
}
