package tech.mhuang.ext.netty.coder;

import java.util.List;

import tech.mhuang.ext.netty.constans.Const;
import tech.mhuang.ext.netty.utils.TypeConvert;
import tech.mhuang.ext.netty.vo.BaseRespMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 *
 * 消息解码
 *
 * @author mhuang
 * @since 1.0.0
 */
public class ClientDecoder extends ByteToMessageDecoder{

	//头消息长度
	private final static int HEAD_LENGTH = 15;
	
	private final static byte FLAG = (byte)(0x7C & 0xFF);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < HEAD_LENGTH){
			return;
		}
		// 防止socket字节流攻击  
        // 防止，客户端传来的数据过大  
        // 因为，太大的数据，是不合理的  
		if(in.readableBytes() > 1024){
			in.skipBytes(in.readableBytes());
			 return;
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
			
			BaseRespMessage baseMessage = new BaseRespMessage();
			baseMessage.getHeader().setMsgId((int) TypeConvert.ByteArrayToLong(header, 0, 2));
			baseMessage.getHeader().setMsgNo((int)TypeConvert.ByteArrayToLong(header, 2, 4));
			baseMessage.getHeader().setMsgLength((int)TypeConvert.ByteArrayToLong(header, 6, 4));
			baseMessage.getHeader().setResult(header[10]);
			baseMessage.getHeader().setCurrentkNum((int)TypeConvert.ByteArrayToLong(header, 11, 2));
			baseMessage.getHeader().setTotalkNum((int)TypeConvert.ByteArrayToLong(header, 13, 2));
			
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
			
			//数据加密
			Const.encrypt(body);
			baseMessage.setBody(body);
			out.add(baseMessage);
	}
}
