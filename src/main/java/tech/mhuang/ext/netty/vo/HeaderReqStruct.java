package tech.mhuang.ext.netty.vo;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * 消息头 = [2+2+4+8] 16个字节
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class HeaderReqStruct implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 版本号(2个字节)
	 */
	private int version;

	/**
	 * 消息id（2个字节）
	 */
	private int msgId;

	/**
	 * 消息流水号（建议发送得时候从0叠加）
	 */
	private int msgNo;

	/**
	 * 数据长度（只包含数据包的大小、4个字节）
	 */
	private int msgLength;

	/**
	 * 加密方式（0代表不加密，1代表加密）
	 */
	private int encryptType = 0x01;

	/**
	 * 加密得值。默认位
	 */
	private int encryptValue = 0x7b;

	/**
	 * 当前包数
	 */
	private int currentkNum = 0x01;

	/**
	 * 总包数
	 */
	private int totalkNum = 0x01;
	
	public HeaderReqStruct(){
	}
	public HeaderReqStruct(int msgId,int msgLength){
		this.msgId = msgId;
		this.msgLength = msgLength;
	}
	public HeaderReqStruct(int msgId,int msgLength,int msgNo){
		this.msgId = msgId;
		this.msgLength = msgLength;
		this.msgNo = msgNo;
	}
}
