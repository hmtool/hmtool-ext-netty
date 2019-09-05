package tech.mhuang.ext.netty.client;

import tech.mhuang.ext.netty.model.Host;

/**
 *
 * netty 客户端抽象
 *
 * @author mhuang
 * @since 1.0.0
 */
public abstract class AbstractNettyClient extends Host implements MyNettyClient{
	
	private static final long serialVersionUID = 1L;

	public AbstractNettyClient(){}
	
	public AbstractNettyClient(int port){}
	
	public AbstractNettyClient(String host, int port) {
		super(host, port);
	}
}
