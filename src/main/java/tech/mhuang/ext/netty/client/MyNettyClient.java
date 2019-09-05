package tech.mhuang.ext.netty.client;

/**
 *
 * netty客户端接口
 *
 * @author mhuang
 * @since 1.0.0
 */
public interface MyNettyClient{

	public void connect();
	
	public void connect(String host,int port);
}
