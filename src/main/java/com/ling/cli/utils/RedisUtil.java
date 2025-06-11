package com.ling.cli.utils;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * <a href="https://github.com/whvcse/RedisUtil">该工具来自这里</a>
 *
 * @author WangFan
 * @version 1.1
 */
@Data
public class RedisUtil {
    private static StringRedisTemplate redisTemplate;

    static {
        StringRedisTemplate stringRedisTemplate = SpringContextHolder.getBeans(StringRedisTemplate.class);
        if (ObjectUtil.isEmpty(stringRedisTemplate)) {
            throw new RuntimeException("未注入StringRedisTemplate");
        } else {
            redisTemplate = stringRedisTemplate;
        }
    }

    public static void setRedisTemplate(StringRedisTemplate redisTemp) {
        redisTemplate = redisTemp;
    }

    /** -------------------key相关操作--------------------- */

    /**
     * 删除key
     *
     * @param key 键
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys 键列表
     */
    public static void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 序列化key
     *
     * @param key 键
     */
    public static byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * 是否存在key
     *
     * @param key 键
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param timeout 时间数值
     * @param unit    时间单位
     */
    public static Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 指定过期时间
     *
     * @param key  键
     * @param date 过期时间
     */
    public static Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的key
     *
     * @param pattern 匹配模式
     */
    public static Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key     键
     * @param dbIndex 数据库索引
     */
    public static Boolean move(String key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 键
     */
    public static Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key  键
     * @param unit 时间单位
     */
    public static Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key 键
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 从当前数据库中随机返回一个 key
     */
    public static String randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 旧键
     * @param newKey 新键
     */
    public static void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newKey 不存在时，将 oldKey 改名为 newKey
     *
     * @param oldKey 旧键
     * @param newKey 新键
     */
    public static Boolean renameIfAbsent(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 键
     */
    public static DataType type(String key) {
        return redisTemplate.type(key);
    }

    /** -------------------string相关操作--------------------- */

    /**
     * 设置指定 key 的值
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     */
    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 返回 key 中字符串值的子字符
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     */
    public static String getRange(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     *
     * @param key   键
     * @param value 值
     */
    public static String getAndSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     *
     * @param key    键
     * @param offset 偏移量
     */
    public static Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 批量获取
     *
     * @param keys 键列表
     */
    public static List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
     *
     * @param key   键
     * @param value 值,true为1, false为0
     */
    public static boolean setBit(String key, long offset, boolean value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setBit(key, offset, value));
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public static void setEx(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   键
     * @param value 值
     * @return 之前已经存在返回false, 不存在返回true
     */
    public static boolean setIfAbsent(String key, String value) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @param key    键
     * @param value  值
     * @param offset 从指定位置开始覆写
     */
    public static void setRange(String key, String value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 获取字符串的长度
     *
     * @param key 键
     */
    public static Long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 批量添加
     *
     * @param maps 键值对
     */
    public static void multiSet(Map<String, String> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param maps 键值对
     * @return 之前已经存在返回false, 不存在返回true
     */
    public static boolean multiSetIfAbsent(Map<String, String> maps) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().multiSetIfAbsent(maps));
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key       键
     * @param increment 增长值
     */
    public static Long incrBy(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * @param key       键
     * @param increment 增长值
     */
    public static Double incrByFloat(String key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 追加到末尾
     *
     * @param key   键
     * @param value 追加值
     */
    public static Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /** -------------------hash相关操作------------------------- */

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   键
     * @param field 字段
     */
    public static Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key 键
     */
    public static Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key    键
     * @param fields 字段
     */
    public static List<Object> hMultiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    /**
     * @param key     键
     * @param hashKey hashKey
     * @param value   值
     * @description: 设置hash值
     */
    public static void hPut(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * @param key  键
     * @param maps hash值
     * @description: 批量设置hash值
     */
    public static void hPutAll(String key, Map<String, String> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key     键
     * @param hashKey hashKey
     * @param value   值
     */
    public static Boolean hPutIfAbsent(String key, String hashKey, String value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key    键
     * @param fields 字段
     */
    public static Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key   键
     * @param field 字段
     */
    public static boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       键
     * @param field     字段
     * @param increment 增量
     */
    public static Long hIncrBy(String key, Object field, long increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key   键
     * @param field 字段
     * @param delta 增量
     */
    public static Double hIncrByFloat(String key, Object field, double delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key 键
     */
    public static Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key 键
     */
    public static Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 键
     */
    public static List<Object> hValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key     键
     * @param options 迭代参数
     */
    public static Cursor<Entry<Object, Object>> hScan(String key, ScanOptions options) {
        return redisTemplate.opsForHash().scan(key, options);
    }

    /** ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   键
     * @param index 索引
     */
    public static String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   键
     * @param start 开始位置, 0是开始位置
     * @param end   结束位置, -1返回所有
     */
    public static List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 存储在list头部
     *
     * @param key   键
     * @param value 值
     */
    public static Long lLeftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 批量存储在list头部
     *
     * @param key   键
     * @param value 值...
     */
    public static Long lLeftPushAll(String key, String... value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 批量存储在list头部
     *
     * @param key   键
     * @param value 值列表
     */
    public static Long lLeftPushAll(String key, Collection<String> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 当list存在的时候才在list头部加入元素值
     *
     * @param key   键
     * @param value 值
     */
    public static Long lLeftPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 在列表 key 中找到第一个值等于 pivot 的元素，然后在它左边插入一个 value 元素
     *
     * @param key   键
     * @param pivot 基准值
     * @param value 插入值
     */
    public static Long lLeftPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 将 value 插入到 key 对应的列表的最右边（即尾部）
     *
     * @param key   键
     * @param value 值
     */
    public static Long lRightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 批量存储在list尾部
     *
     * @param key   键
     * @param value 值...
     */
    public static Long lRightPushAll(String key, String... value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 批量存储在list尾部
     *
     * @param key   键
     * @param value 值列表
     */
    public static Long lRightPushAll(String key, Collection<String> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 当list存在的时候才在list尾部加入元素值
     *
     * @param key   键
     * @param value 值
     */
    public static Long lRightPushIfPresent(String key, String value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 在列表 key 中找到第一个值等于 pivot 的元素，然后在它右边插入一个 value 元素
     *
     * @param key   键
     * @param pivot 基准值
     * @param value 插入值
     */
    public static Long lRightPush(String key, String pivot, String value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key   键
     * @param index 位置
     * @param value 值
     */
    public static void lSet(String key, long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除 List 的左边（头部）一个元素并返回它
     *
     * @param key 键
     * @return 删除的元素
     */
    public static String lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除 List 的左边（头部）一个元素并返回它,如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键
     * @param timeout 等待时间
     * @param unit    时间单位
     */
    public static String lBLeftPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除 List 的右边（尾部）一个元素并返回它
     *
     * @param key 键
     * @return 删除的元素
     */
    public static String lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除 List 的右边（尾部）一个元素并返回它,如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键
     * @param timeout 等待时间
     * @param unit    时间单位
     */
    public static String lBRightPop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      源列表
     * @param destinationKey 目标列表
     */
    public static String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                destinationKey);
    }

    /**
     * 移除列表的最后一个元素，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      源列表
     * @param destinationKey 目标列表
     * @param timeout        等待时间
     * @param unit           时间单位
     */
    public static String lBRightPopAndLeftPush(String sourceKey, String destinationKey,
                                               long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于value的元素
     *
     * @param key   键
     * @param index index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *              index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value 值
     */
    public static Long lRemove(String key, long index, String value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 裁剪list
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     */
    public static void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     */
    public static Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /** --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param key    键
     * @param values 值...
     */
    public static Long sAdd(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * set移除元素
     *
     * @param key    键
     * @param values 值...
     */
    public static Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 键
     */
    public static String sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param key     源集合
     * @param value   值
     * @param destKey 目标集合
     */
    public static Boolean sMove(String key, String value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key 键
     */
    public static Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key   键
     * @param value 值
     */
    public static Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 返回 key 和 otherKey 两个集合的交集（即共同存在的元素）
     *
     * @param key1 集合1
     * @param key2 集合2
     */
    public static Set<String> sIntersect(String key1, String key2) {
        return redisTemplate.opsForSet().intersect(key1, key2);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key       键
     * @param otherKeys 其他集合
     */
    public static Set<String> sIntersect(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param key      键
     * @param otherKey 其他集合
     * @param destKey  目标集合
     */
    public static Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param key       键
     * @param otherKeys 其他集合
     * @param destKey   目标集合
     */
    public static Long sIntersectAndStore(String key, Collection<String> otherKeys,
                                          String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key1 集合1
     * @param key2 集合2
     */
    public static Set<String> sUnion(String key1, String key2) {
        return redisTemplate.opsForSet().union(key1, key2);
    }

    /**
     * 获取key集合与多个集合的并集
     *
     * @param key       键
     * @param otherKeys 其他集合
     */
    public static Set<String> sUnion(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param key      键
     * @param otherKey 其他集合
     * @param destKey  目标集合
     */
    public static Long sUnionAndStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param key       键
     * @param otherKeys 其他集合
     * @param destKey   目标集合
     */
    public static Long sUnionAndStore(String key, Collection<String> otherKeys,
                                      String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      键
     * @param otherKey 其他集合
     */
    public static Set<String> sDifference(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     *
     * @param key       键
     * @param otherKeys 其他集合
     */
    public static Set<String> sDifference(String key, Collection<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param key      键
     * @param otherKey 其他集合
     * @param destKey  目标集合
     */
    public static Long sDifference(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey,
                destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param key       键
     * @param otherKeys 其他集合
     * @param destKey   目标集合
     */
    public static Long sDifference(String key, Collection<String> otherKeys,
                                   String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                destKey);
    }

    /**
     * 获取集合所有元素
     *
     * @param key 键
     */
    public static Set<String> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key 键
     */
    public static String sRandomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     *
     * @param key   键
     * @param count 个数
     */
    public static List<String> sRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param key   键
     * @param count 个数
     */
    public static Set<String> sDistinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 使用给定的 ScanOptions 对 key 对应的 Set 进行 惰性遍历（非阻塞扫描），返回一个 Cursor 用于逐个访问集合中的元素。
     *
     * @param key     键
     * @param options ScanOptions
     */
    public static Cursor<String> sScan(String key, ScanOptions options) {
        return redisTemplate.opsForSet().scan(key, options);
    }

    /**------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    public static Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key    键
     * @param values 值
     */
    public static Long zAdd(String key, Set<TypedTuple<String>> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * 移除有序集合中给定的成员，不存在的成员将被忽略。
     *
     * @param key    键
     * @param values 值...
     */
    public static Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param key   键
     * @param value 值
     * @param delta 增量
     */
    public static Double zIncrementScore(String key, String value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param key   键
     * @param value 值
     * @return 0表示第一位
     */
    public static Long zRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param key   键
     * @param value 值
     * @return 0表示第一位
     */
    public static Long zReverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     */
    public static Set<String> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     */
    public static Set<TypedTuple<String>> zRangeWithScores(String key, long start,
                                                           long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public static Set<String> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public static Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                  double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * 从有序集合 key 中，查找 score 在 [min, max] 范围内的元素，按 score 升序排列，从偏移量 start 开始，最多返回 end 个元素，同时返回其对应的 score。
     *
     * @param key   键
     * @param min   最小值
     * @param max   最大值
     * @param start 开始位置
     * @param count 个数
     */
    public static Set<TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                  double min, double max, long start, long count) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
                start, count);
    }

    /**
     * 从有序集合 key 中，按 score 降序 排序，获取 从索引 start 到 end 的元素值（不包含分数）。
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     */
    public static Set<String> zReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 从有序集合 key 中按分数从高到低排序，获取从索引 start 到 end 范围内的元素，并返回它们的值和对应的 score（分数）。
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     */
    public static Set<TypedTuple<String>> zReverseRangeWithScores(String key,
                                                                  long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start,
                end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public static Set<String> zReverseRangeByScore(String key, double min,
                                                   double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 从有序集合 key 中获取 score 在 [min, max] 范围内的元素，并按分数从高到低排序返回，同时包含每个元素的 score 和 value
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public static Set<TypedTuple<String>> zReverseRangeByScoreWithScores(
            String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
                min, max);
    }

    /**
     * 从 Redis 的 ZSet（有序集合）中查找 score 在 [min, max] 范围内的元素，按 score 从高到低排序，跳过前 start 个，返回 end 个元素值（不含分数）
     *
     * @param key   键
     * @param min   最小值
     * @param max   最大值
     * @param start 开始位置
     * @param count 个数
     */
    public static Set<String> zReverseRangeByScore(String key, double min,
                                                   double max, long start, long count) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
                start, count);
    }

    /**
     * 根据score值获取集合元素数量
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public static Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取集合大小
     *
     * @param key 键
     */
    public static Long zSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取集合中value元素的score值
     *
     * @param key   键
     * @param value 值
     */
    public static Double zScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     */
    public static Long zRemoveRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param key 键
     * @param min 最小值
     * @param max 最大值
     */
    public static Long zRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 将 ZSet 集合1 和 集合2 中的元素进行并集运算（按照元素的 score 相加）
     * 然后把结果存入新的 ZSet 目标集合 中，并返回新集合的元素个数。
     *
     * @param key1    集合1
     * @param key2    集合2
     * @param destKey 目标集合
     */
    public static Long zUnionAndStore(String key1, String key2, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key1, key2, destKey);
    }

    /**
     * 将 ZSet 集合1 和 list 中所有元素进行 并集操作
     * 并将结果存储在目标 ZSet destKey 中
     * 最后返回新 ZSet destKey 的元素个数。
     *
     * @param key1    集合1
     * @param list    集合2
     * @param destKey 目标集合
     */
    public static Long zUnionAndStore(String key1, Collection<String> list,
                                      String destKey) {
        return redisTemplate.opsForZSet()
                .unionAndStore(key1, list, destKey);
    }

    /**
     * 计算 ZSet 集合1 和 集合2 的交集（即同时存在于两个集合中的元素）
     * 并将交集的结果存储到目标 ZSet destKey 中
     * 返回的是新 ZSet destKey 中的元素个数。
     *
     * @param key1    集合1
     * @param key2    集合2
     * @param destKey 目标集合
     */
    public static Long zIntersectAndStore(String key1, String key2,
                                          String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key1, key2, destKey);
    }

    /**
     * 交计算 ZSet 集合1 和 list 的交集（即同时存在于两个集合中的元素）
     * 并将交集的结果存储到目标 ZSet destKey 中
     * 返回的是新 ZSet destKey 中的元素个数。
     *
     * @param key     集合1
     * @param list    集合2
     * @param destKey 目标集合
     */
    public static Long zIntersectAndStore(String key, Collection<String> list,
                                          String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, list, destKey);
    }

    /**
     * @param key 要扫描的 ZSet 的键
     * @param options 扫描的选项，通常通过 ZSetScanOptions 来指定
     * @return 该方法返回一个 Cursor<Tuple>，其中 Tuple 是一个包含 元素值（value） 和 分数（score） 的对象
     */
    public static Cursor<TypedTuple<String>> zScan(String key, ScanOptions options) {
        return redisTemplate.opsForZSet().scan(key, options);
    }
}