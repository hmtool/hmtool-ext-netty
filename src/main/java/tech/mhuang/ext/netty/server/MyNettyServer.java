package tech.mhuang.ext.netty.server;

/**
 *
 * netty 服务端接口
 *
 * @author mhuang
 * @since 1.0.0
 */
public interface MyNettyServer {

	public void bind();
	
	public void bind(int port);
}
