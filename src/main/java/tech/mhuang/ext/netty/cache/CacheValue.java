package tech.mhuang.ext.netty.cache;

import lombok.Data;

@Data
public class CacheValue {
    private int crtCount;//当前包数
    private int tlCount;//总包书
    private byte[] data;//当前报的数据
    
    public void addData(byte[] data){
        if(this.data == null){
            this.data = data;
        }else{
            byte[] mergeData = new byte[this.data.length + data.length];
            System.arraycopy(this.data, 0, mergeData, 0, this.data.length);  
            System.arraycopy(data, 0, mergeData, this.data.length, data.length);  
            this.data = mergeData;
        }
        
    }
    public CacheValue(){}
    
    public CacheValue(int crtCount,int tlCount,byte[] data){
        this.crtCount = crtCount;
        this.tlCount = tlCount;
        this.data = data;
    }
}
