package tech.mhuang.ext.netty.coder;

import java.util.List;

import tech.mhuang.ext.netty.constans.Const;
import tech.mhuang.ext.netty.utils.TypeConvert;
import tech.mhuang.ext.netty.cache.CacheData;
import tech.mhuang.ext.netty.cache.CacheValue;
import tech.mhuang.ext.netty.vo.BaseMessage;
import tech.mhuang.ext.netty.vo.BaseRespMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 *
 * 服务端消息解码
 *
 * @author mhuang
 * @since 1.0.0
 */
public class ServerDecoder extends ByteToMessageDecoder{

	private final static byte FLAG = (byte)(0x7C & 0xFF);
	
	//消息头长度
	private final static int HEAD_LENGTH = 22;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		if(in.readableBytes() < HEAD_LENGTH){
			return;
		}
		
		// 防止socket字节流攻击  
        // 防止，客户端传来的数据过大  
        // 因为，太大的数据，是不合理的  
		if(in.readableBytes() > 2048){
			in.skipBytes(in.readableBytes());
		}
		 // 记录包头开始的index 
        int beginReader;  
		
        while(true){
        	// 获取包头开始的index  
            beginReader = in.readerIndex();  
            // 标记包头开始的index  
            in.markReaderIndex();  
            // 读到了协议的开始标志，结束while循环  
            if (in.readByte() == FLAG) {  
                break;  
            }
            // 未读到包头，略过一个字节  
            // 每次略过，一个字节，去读取，包头信息的开始标记  
            in.resetReaderIndex();  
            in.readByte();  

            // 当略过，一个字节之后，  
            // 数据包的长度，又变得不满足  
            // 此时，应该结束。等待后面的数据到达  
            if (in.readableBytes() < HEAD_LENGTH) {  
                return;  
            }  
        }
        
        //消息头
        byte[] header = new byte[HEAD_LENGTH];
		in.readBytes(header);
		
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.getHeader().setVersion((int) TypeConvert.ByteArrayToLong(header, 0, 2));
		baseMessage.getHeader().setMsgId((int) TypeConvert.ByteArrayToLong(header, 2, 2));
		baseMessage.getHeader().setMsgNo((int) TypeConvert.ByteArrayToLong(header, 4, 8));
		baseMessage.getHeader().setMsgLength((int) TypeConvert.ByteArrayToLong(header, 12, 4));
		baseMessage.getHeader().setEncryptType((int)TypeConvert.ByteArrayToLong(header, 16, 1));
		baseMessage.getHeader().setEncryptValue((int)TypeConvert.ByteArrayToLong(header, 17, 1));
		baseMessage.getHeader().setCurrentkNum((int)TypeConvert.ByteArrayToLong(header, 18, 2));
		baseMessage.getHeader().setTotalkNum((int)TypeConvert.ByteArrayToLong(header, 20, 2));
		
		
		//消息体
		// 判断请求数据包数据是否到齐  
		if(in.readableBytes() < baseMessage.getHeader().getMsgLength()){
			 // 还原读指针  
            in.readerIndex(beginReader);  
            return;  
		}
		byte[] body = new byte[(int) baseMessage.getHeader().getMsgLength()];
		in.readBytes(body);
		baseMessage.setBody(body);
		
		//没有读到校验码和标识位
		if(in.readableBytes() < 2){
			// 还原读指针  
            in.readerIndex(beginReader);  
            return;
		}
		
		//判断校验码是否相等
		if(in.readByte() != baseMessage.checkcode()){
			BaseRespMessage resp = new BaseRespMessage();
			resp.getHeader().setMsgId(baseMessage.getHeader().getMsgId());
			resp.getHeader().setResult((byte)(2 & 0xff));
			ctx.writeAndFlush(resp);
			in.skipBytes(in.readableBytes());
            return;
		}
		//判断是否是结束符
		if(in.readByte() != FLAG){
			//不是结束符返回状态3、抛弃目前所有数据、下次继续读取
			BaseRespMessage resp = new BaseRespMessage();
			resp.getHeader().setMsgId(baseMessage.getHeader().getMsgId());
			resp.getHeader().setResult((byte)(3 & 0xff));
			ctx.writeAndFlush(resp);
			in.skipBytes(in.readableBytes());
            return;
		}
		
		//如果加密方式用得默认
		if(baseMessage.getHeader().getEncryptType() == 1){
			Const.encrypt(baseMessage.getHeader().getEncryptValue(), body);
			baseMessage.setBody(body);
		}
		CacheValue cacheValue = CacheData.remove(ctx.channel().id().asLongText(), baseMessage.getHeader().getMsgId());
		if(baseMessage.getHeader().getTotalkNum() == 1){ //总共只有一包
		    out.add(baseMessage);
		}else{
		    //多包
		    if(baseMessage.getHeader().getCurrentkNum() == 1){
                CacheData.add(ctx.channel().id().asLongText(), baseMessage.getHeader().getMsgId(), 
                        baseMessage.getHeader().getCurrentkNum(), baseMessage.getHeader().getTotalkNum(), baseMessage.getBody());
            //不是第一包。并且缓存没有数据。属于异常包
            }else if(cacheValue == null){
                in.skipBytes(in.readableBytes());
                return;
            //最后包
            }else if(baseMessage.getHeader().getCurrentkNum() == baseMessage.getHeader().getTotalkNum()){
	            if(cacheValue.getCrtCount() + 1 == baseMessage.getHeader().getTotalkNum()){
	                cacheValue.addData(body);
	                baseMessage.setBody(cacheValue.getData());
	                out.add(baseMessage); 
	            }
	        //中间包并且当前包正常状态
	        }else if(baseMessage.getHeader().getCurrentkNum() == cacheValue.getCrtCount() + 1){
	            CacheData.add(ctx.channel().id().asLongText(), baseMessage.getHeader().getMsgId(), 
	                    baseMessage.getHeader().getCurrentkNum(), baseMessage.getHeader().getTotalkNum(), cacheValue.getData(),body);
	        }else{
	            in.skipBytes(in.readableBytes());
                return;
	        }
		}
	}
}
