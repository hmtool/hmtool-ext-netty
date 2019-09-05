package tech.mhuang.ext.netty.coder;

import tech.mhuang.ext.netty.constans.Const;
import tech.mhuang.ext.netty.utils.TypeConvert;
import tech.mhuang.ext.netty.vo.BaseRespMessage;
import tech.mhuang.ext.netty.vo.HeaderRespStruct;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * 自定义编码器
 *
 * @author mhuang
 * @since 1.0.0
 */
@ChannelHandler.Sharable
public class ServerEncoder extends MessageToByteEncoder<BaseRespMessage>{

	private static byte FLAG = (byte)(0x7C & 0xff);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, BaseRespMessage msg, ByteBuf out) throws Exception {
		HeaderRespStruct header = msg.getHeader();
		//标识头
		out.writeByte(FLAG);
		//返回消息
		//版本号
		out.writeBytes(TypeConvert.LongToByteArray(header.getMsgId(), 2));
		//消息长度
		out.writeBytes(TypeConvert.LongToByteArray(header.getMsgNo(), 4));
		//消息长度
		out.writeBytes(TypeConvert.LongToByteArray(header.getMsgLength(), 4));
		//消息ID
		out.writeBytes(TypeConvert.LongToByteArray(header.getResult(), 1));
		//当前包
		out.writeBytes(TypeConvert.LongToByteArray(header.getCurrentkNum(), 2));
		//总
		out.writeBytes(TypeConvert.LongToByteArray(header.getTotalkNum(), 2));
		byte[] data = msg.getBody();
		if(msg.getBody() != null){
			//数据加密
			Const.encrypt(data);
			//数据
			out.writeBytes(data);
			//校验码
			out.writeByte(msg.checkcode());
		}else{
			out.writeByte((byte)0x00 & 0xff);
		}
		//标识头
		out.writeByte(FLAG);
	}
}
