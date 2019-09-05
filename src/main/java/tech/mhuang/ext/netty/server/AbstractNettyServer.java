package tech.mhuang.ext.netty.server;

import tech.mhuang.ext.netty.model.Port;

/**
 *
 * netty 服务 抽象
 *
 * @author mhuang
 * @since 1.0.0
 */
public abstract class AbstractNettyServer extends Port implements MyNettyServer{

	private static final long serialVersionUID = 1L;

	public AbstractNettyServer(){
	}
	
	public AbstractNettyServer(int port){
		super(port);
	}
}
