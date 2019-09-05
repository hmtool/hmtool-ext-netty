package tech.mhuang.ext.netty.utils;

import java.io.UnsupportedEncodingException;

/**
 * 字节转换大端工具类
 *
 * @author mhuang
 * @since 1.0.0
 */
public class TypeConvert {

    /**
     * byte数组转long
     *
     * @param v_byte 转换的byte数组
     * @param v_nBeginPos 转换的下标
     * @param nBytes 转换的长度
     * @return Long值
     */
    public static long ByteArrayToLong(byte v_byte[], int v_nBeginPos, int nBytes) {
        long ret = 0L;
        for (int i = 0; i < nBytes; i++) {
            int nTmp = v_byte[(v_nBeginPos + (nBytes - 1)) - i];
            if (nTmp < 0) {
                nTmp += 256;
            }
            ret += nTmp << i * 8;
        }
        return ret;
    }

    /**
     * long转数组
     *
     * @param nData Long值
     * @param v_bytes byte数组
     * @param nBeginPos 转换的下标
     * @param nBytes 转换的长度
     */
    public static void LongToByteArray(long nData, byte v_bytes[], int nBeginPos, int nBytes) {
        for (int i = 0; i < nBytes; i++){
            v_bytes[(nBeginPos + (nBytes - 1)) - i] = (byte) (int) (nData >>> i * 8);
        }
    }

    /**
     * Long转数组
     *
     * @param nValue 转换的long
     * @param nBytes 转换的长度
     * @return 返回对应的bate数组
     */
    public static byte[] LongToByteArray(long nValue, int nBytes) {
        byte bValue[] = new byte[nBytes];
        LongToByteArray(nValue, bValue, 0, nBytes);
        return bValue;
    }

    /**
     * String转byte数组
     * @param sValue 转换的String
     * @return 返回对应的byte数组
     */
    public static byte[] StringToByteArray(String sValue) {
        int nBytes = 0;
        try {
            nBytes = sValue.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        byte bValue[] = new byte[nBytes];
        UtilFun.Memset(bValue, 0, (byte) 0, nBytes);
        try {
            UtilFun.memcpy(bValue, sValue.getBytes("GBK"), sValue.getBytes("GBK").length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bValue;
    }

}
