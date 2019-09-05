package tech.mhuang.ext.netty.vo;

/**
 *
 * 通用校验码检查接口
 *
 * @author mhuang
 * @since 1.0.0
 */
public interface BaseCheck {
	
	default byte checkcode(byte[] buf){
		if (buf == null){
			return 0;
		}
		int i = 0, r = 0, size = buf.length;

		for (; i < size; i++) {
			r = (buf[i] ^ r) & 0xff;
		}

		return (byte) r;
	}
}
