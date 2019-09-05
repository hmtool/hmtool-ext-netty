package tech.mhuang.ext.netty.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Netty缓存数据处理
 *
 * @author mhuang
 * @since 1.0.0
 */
public class CacheData {

    private static Map<CacheKey, CacheValue> cacheDataMap = new ConcurrentHashMap<>();
    
    public static void add(String channelId,int msgId,int crtCount,int tlCount,byte[] data){
        CacheKey cacheKey = new CacheKey(channelId,msgId);
        CacheValue cacheValue = new CacheValue();
        cacheValue = cacheDataMap.getOrDefault(cacheKey, cacheValue);
        cacheValue.setCrtCount(crtCount);
        cacheValue.setTlCount(tlCount);
        cacheValue.addData(data);
        cacheDataMap.put(cacheKey, cacheValue);
    }
    
    public static void add(String channelId,int msgId,int crtCount,int tlCount,byte[] sourceData,byte[] data){
        CacheKey cacheKey = new CacheKey(channelId,msgId);
        CacheValue cacheValue = new CacheValue();
        cacheValue = cacheDataMap.getOrDefault(cacheKey, cacheValue);
        cacheValue.setCrtCount(crtCount);
        cacheValue.setTlCount(tlCount);
        cacheValue.setData(sourceData);
        cacheValue.addData(data);
        cacheDataMap.put(cacheKey, cacheValue);
    }
    
    public static CacheValue get(String channelId,int msgId){
        CacheKey cacheKey = new CacheKey(channelId,msgId);
        return cacheDataMap.get(cacheKey);
    }
    
    public static CacheValue remove(String channelId,int msgId){
        CacheKey cacheKey = new CacheKey(channelId,msgId);
        return cacheDataMap.remove(cacheKey);
    }
}
