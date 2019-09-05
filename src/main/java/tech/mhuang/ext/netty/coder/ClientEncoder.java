package tech.mhuang.ext.netty.coder;

import tech.mhuang.ext.netty.constans.Const;
import tech.mhuang.ext.netty.utils.TypeConvert;
import tech.mhuang.ext.netty.vo.HeaderReqStruct;
import tech.mhuang.ext.netty.vo.BaseMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * 客户端消息编码
 *
 * @author mhuang
 * @since 1.0.0
 */
@ChannelHandler.Sharable
public class ClientEncoder extends MessageToByteEncoder<BaseMessage>{

	private static byte FLAG = (byte)(0x7C & 0xff);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, BaseMessage msg, ByteBuf out) throws Exception {
		if(msg==null||msg.getHeader()==null){
			throw new Exception("The encode message is null");
		}
		HeaderReqStruct header = msg.getHeader();
		
		//标识头
		out.writeByte(FLAG);
		
		//消息头
		out.writeBytes(TypeConvert.LongToByteArray(header.getVersion(), 2));//版本号
		out.writeBytes(TypeConvert.LongToByteArray(header.getMsgId(), 2));//消息ID
		out.writeBytes(TypeConvert.LongToByteArray(header.getMsgNo(), 8));//接入码
		out.writeBytes(TypeConvert.LongToByteArray(header.getMsgLength(), 4));//消息长度
		out.writeBytes(TypeConvert.LongToByteArray(header.getEncryptType(), 1));//接入码
		out.writeBytes(TypeConvert.LongToByteArray(header.getEncryptValue(), 1));//接入码
		out.writeBytes(TypeConvert.LongToByteArray(header.getCurrentkNum(), 2));//当前
		out.writeBytes(TypeConvert.LongToByteArray(header.getTotalkNum(), 2));//总
		//数据如果不为空
		byte[] data = msg.getBody();
		if(msg.getBody() != null){
			//判断是否加密
			if(header.getEncryptType() == 1){
				//数据加密
				Const.encrypt(header.getEncryptValue(),data);
			}
			//数据
			out.writeBytes(data);
			//校验码
			out.writeByte(msg.checkcode());
		}else{
			out.writeByte((byte)0x00 & 0xff);
		}
		
		//标识尾
		out.writeByte(FLAG);
	}
}
