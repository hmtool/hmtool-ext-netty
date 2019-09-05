package tech.mhuang.ext.netty.model;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 端口
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class Port implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_PORT = 8000;
	
	protected int port = DEFAULT_PORT;

	public Port(){}
	
	public Port(int port){
		this.port = port;
	}
}
