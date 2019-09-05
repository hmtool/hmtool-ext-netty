package tech.mhuang.ext.netty.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 通用消息[标识头(1)+消息头(16)+消息体(?)+校验码(1)+标识尾(1)]
 *
 * @author mhuang
 * @since 1.0.0
 */
public class BaseMessage implements Serializable, BaseCheck {

    private static final long serialVersionUID = 1L;

    /**
     * 消息头
     */
    @Setter
    @Getter
    private HeaderReqStruct header = new HeaderReqStruct();

    /**
     * 消息体
     */
    @Getter
    private byte[] body;

    public void setBody(byte[] body) {
        this.body = body;
        header.setMsgLength(body.length);
    }

    public BaseMessage() {

    }

    public byte checkcode() {
        return checkcode(body);
    }
}
