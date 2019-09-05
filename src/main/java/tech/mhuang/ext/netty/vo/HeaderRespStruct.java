package tech.mhuang.ext.netty.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息头 = [2+2+4+8] 16个字节
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class HeaderRespStruct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private int msgId;

    /**
     * 消息序列号
     */
    private int msgNo;

    /**
     * 数据长度（只包含数据包的大小、4个字节）
     */
    private int msgLength;

    /**
     * 结果   0 失败 1成功   2 鉴权失败 3 数据不合法  4 消息不存在
     */
    private byte result;

    /**
     * 当前包
     */
    private int currentkNum = 0x01;

    /**
     * 总包
     */
    private int totalkNum = 0x01;

    public HeaderRespStruct() {

    }

    public HeaderRespStruct(int msgId, int msgLength) {
        this.msgId = msgId;
        this.msgLength = msgLength;
    }
}
