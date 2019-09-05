package tech.mhuang.ext.netty.model;

import java.io.Serializable;

/**
 *
 * 主机
 *
 * @author mhuang
 * @since 1.0.0
 */
public class Host extends Port implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_HOST = "127.0.0.1";
	
	protected String host = DEFAULT_HOST;

	public Host(){
	}
	
	public Host(String host,int port){
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
