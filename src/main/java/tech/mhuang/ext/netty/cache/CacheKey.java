package tech.mhuang.ext.netty.cache;

import lombok.Data;

@Data
class CacheKey{
    
    private String channelId = "";
    
    private int msgId;
    
    public CacheKey(){
        
    }
    @Override
    public boolean equals(Object obj) {
        Boolean checkEqs = Boolean.FALSE;
        if(this == obj){
            checkEqs = Boolean.TRUE;
        }else if(obj instanceof CacheKey){
            CacheKey cacheKey = (CacheKey)obj;
            checkEqs = channelId.equals(cacheKey.channelId) &&
                    msgId == cacheKey.msgId;
        }
        return checkEqs.booleanValue();
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + channelId.hashCode();
        result = 31 * result + msgId;
        return result;
    }
    
    public CacheKey(String channelId,int msgId){
        this.channelId = channelId;
        this.msgId = msgId;
    }
}
