package tech.mhuang.ext.netty.constans;



/**
 *
 * 常量
 *
 * @author mhuang
 * @since 1.0.0
 */
public class Const {

	public static final int P0100 = 0x0100;//登录
	
	public static final int P0002 = 0x0002;//心跳
	
	public static final int P0802 = 0x0802;//心跳回复
	
	public static final int m1 = 10000000;
	public static final int a1 = 20000000;
	public static final int c1 = 30000000;
	
	public final static int KEY = 123;
	
	public static void encrypt(byte[] buffer){
		encrypt(KEY,buffer);
	}
	
	public static void encrypt(int key,byte[] buffer){
		int size = buffer.length;
		int idx = 0;
		if(0 == key){
			key = 1;
		}
		int mkey = m1;
		while(idx < size){
			key = a1 * (key % mkey) +c1;
			buffer[idx++] ^= ((key>>20) & 0xff);
		}
	}
	
	public static byte getCheckCode(byte[] buf) {
		int i = 0;
		int r = 0;
		int size = buf.length;
		for (; i < size; i++) {
			r = (buf[i] ^ r) & 0xff;
		}
		return (byte) r;
	}
}
