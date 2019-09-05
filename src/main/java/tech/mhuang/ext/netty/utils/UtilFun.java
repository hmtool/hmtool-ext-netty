package tech.mhuang.ext.netty.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 工具类（处理String）
 *
 * @author mhuang
 * @since 1.0.0
 */
public class UtilFun
{
    public UtilFun()
    {
    }

    public static boolean memcmp(byte b1[], byte b2[], int nCmpLen)
    {
        if (nCmpLen > b1.length || nCmpLen > b2.length) {
            return false;
        }
        for (int i = 0; i < nCmpLen; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }

        return true;
    }

    public static int memfind(byte bSource[], byte bToFind[], int nStart, int nEnd, int nToFindLen)
    {
        for (int nPos = nStart; nPos <= (nEnd + 1) - nToFindLen; nPos++)
        {
            boolean bFind = true;
            for (int i = 0; i < nToFindLen; i++)
            {
                if (bSource[nPos + i] == bToFind[i])
                    continue;
                bFind = false;
                break;
            }

            if (bFind)
                return nPos;
        }

        return -1;
    }

    public static boolean memcpy(byte b1[], byte b2[], int nCopyLen)
    {
        return memcpy(b1, 0, b2, 0, nCopyLen);
    }

    public static boolean memcpy(byte b1[], int nb1Pos, byte b2[], int nb2Pos, int nCopyLen)
    {
        if (nCopyLen > b1.length || nCopyLen > b2.length){
            return false;
        }
        for (int i = 0; i < nCopyLen; i++){
            b1[i + nb1Pos] = b2[i + nb2Pos];
        }
        return true;
    }

    public static void Memset(byte b1[], int nPos, byte bValue, int nLen)
    {
        for (int i = 0; i < nLen; i++){
            b1[nPos + i] = bValue;
        }
    }

    public static String ByteArrayToString(byte b1[], int nBeginPos, int nLen)
    {
        return ByteArrayToString(b1, nBeginPos, nLen, "GBK");
    }

    public static String ByteArrayToString(byte b1[], int nBeginPos, int nLen, String sCharSet)
    {
        try
        {
            String sRet = new String(b1, nBeginPos, nLen, sCharSet);
            int nIndex = sRet.indexOf('\0');
            if (nIndex >= 0)
                sRet = sRet.substring(0, nIndex);
            return sRet;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public static String GetParameter(String vs_src, String vs_var_name)
    {
        String sSaveSrc = vs_src;
        vs_src.toLowerCase();
        vs_var_name.toLowerCase();
        String ls_var = (new StringBuilder(String.valueOf(vs_var_name))).append("=").toString();
        int nBeginPos = vs_src.indexOf(ls_var, 0);
        if (nBeginPos < 0)
            return null;
        int nEndPos = vs_src.indexOf("&", nBeginPos);
        if (nEndPos < 0)
            return sSaveSrc.substring(nBeginPos + ls_var.length());
        else
            return sSaveSrc.substring(nBeginPos + ls_var.length(), nEndPos);
    }

    public static String toChinese(String strvalue)
    {
        if (strvalue == null)
            return null;
        try
        {
            strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
            return strvalue;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String toIso8859_1(String strvalue)
    {
        if (strvalue == null)
            return null;
        try
        {
            strvalue = new String(strvalue.getBytes("GBK"), "ISO8859_1");
            return strvalue;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String getEnterLine(String strvalue)
    {
        for (int nPos = strvalue.indexOf('\r'); nPos > 0; nPos = strvalue.indexOf('\r'))
            strvalue = (new StringBuilder(String.valueOf(strvalue.substring(0, nPos)))).append("<br>").append(strvalue.length() <= nPos + 1 ? "" : strvalue.substring(nPos + 1, strvalue.length())).toString();

        return strvalue;
    }

    public static String ChangeHtmlQuotSign(String strvalue)
    {
        for (int nPos = strvalue.indexOf('"'); nPos > 0; nPos = strvalue.indexOf('"'))
            strvalue = (new StringBuilder(String.valueOf(strvalue.substring(0, nPos)))).append("&quot;").append(strvalue.length() <= nPos + 1 ? "" : strvalue.substring(nPos + 1, strvalue.length())).toString();

        return strvalue;
    }

    public static String ChangeOracleChar39(String strvalue)
    {
        for (int nPos = strvalue.indexOf('\''); nPos > 0; nPos = strvalue.indexOf('\'', nPos + 2))
            strvalue = (new StringBuilder(String.valueOf(strvalue.substring(0, nPos + 1)))).append("'").append(strvalue.length() <= nPos + 1 ? "" : strvalue.substring(nPos + 1, strvalue.length())).toString();

        return strvalue;
    }

    public static String GetNameFromPath(String sPath, String sRootChar)
    {
        int nIndex = sPath.lastIndexOf(sRootChar);
        return sPath.substring(nIndex + 1, sPath.length());
    }

    public static String Format(double dValue, String sFormat)
    {
        DecimalFormat dFomt = new DecimalFormat();
        dFomt.applyLocalizedPattern(sFormat);
        return new String(dFomt.format(dValue));
    }

    public static String Format(Date dDate, String sFormat)
    {
        SimpleDateFormat ft = new SimpleDateFormat(sFormat);
        return ft.format(dDate);
    }

    public static String native2ascii(String str)
    {
        StringBuffer sb = new StringBuffer(1000);
        sb.setLength(0);
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c > '\377')
            {
                sb.append("\\u");
                int j = c >>> 8;
                String tmp = Integer.toHexString(j);
                if (tmp.length() == 1)
                    sb.append("0");
                sb.append(tmp);
                j = c & 0xff;
                tmp = Integer.toHexString(j);
                if (tmp.length() == 1)
                    sb.append("0");
                sb.append(tmp);
            } else
            {
                sb.append(c);
            }
        }

        return new String(sb);
    }

    public static String bytesToHexString(byte src[])
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
            return null;
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append(hv);
        }

        return stringBuilder.toString();
    }
}
