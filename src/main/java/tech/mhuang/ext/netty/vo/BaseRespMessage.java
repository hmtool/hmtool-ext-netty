package tech.mhuang.ext.netty.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 通用应答
 *
 * @author mhuang
 * @since 1.0.0
 */
public class BaseRespMessage implements Serializable,BaseCheck{
	
	private static final long serialVersionUID = 1L;

	@Setter
	@Getter
	private HeaderRespStruct header = new HeaderRespStruct();
	
	@Getter
	private byte[] body;

	public void setBody(byte[] body) {
		this.body = body;
		this.getHeader().setMsgLength(body.length);
	}
	
	public byte checkcode(){
		return checkcode(body);
	}
}
