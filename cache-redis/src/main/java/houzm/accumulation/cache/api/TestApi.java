package houzm.accumulation.cache.api;

import redis.clients.jedis.Jedis;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Package: houzm.accumulation.cache.api
 * Author: hzm_dream@163.com
 * Date: Created in 2018/11/9 20:58
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： test jedis api
 */
public class TestApi {

    private static Jedis jedis;

    static {
//        jedis = RedisUtil.getJedis("192.168.1.111", 6379);
//        jedis = RedisUtil.getJedis("192.168.202.111", 6379);
        jedis = RedisUtil.getJedis("47.101.152.55", 6379);
    }

    public static void main(String[] args) {
        System.out.println("jedis链接：" + jedis);
        System.out.println(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2018-11-14 13:33:14").getMillis());
        keyApi();
    }

    private static void keyApi() {
//        System.out.println("清空数据："+jedis.flushDB());
//        System.out.println("删除指定单个key(key 存在)："+jedis.del("name")); // 1
//        System.out.println("删除指定单个key(key 不存在)："+jedis.del("name")); // 0
//        System.out.println("删除指定多个key(部分key 不存在："+jedis.del("age", "addr", "name")); // 2
//        System.out.println("删除指定多个key(全部key 不存在)："+jedis.del("age", "addr", "name")); // 0
//        System.out.println("序列化指定key(key 存在)："+jedis.dump("dumpkey")); // [B@299a06ac
//        System.out.println("序列化指定key(key 不存在)："+jedis.dump("no-exist-key")); // null

//        System.out.println("检查给定key是否存在(key 存在)："+jedis.exists("name")); //检查给定key是否存在(key 存在)：true
//        System.out.println("检查给定key是否存在(key 不存在)："+jedis.exists("not-exist-key-name")); //检查给定key是否存在(key 不存在)：false

//        System.out.println("ttl查询指定键过期剩余时间 秒(key 不存在)："+jedis.ttl("not-exist-key"));//-2
//        System.out.println("ttl查询指定键过期剩余时间 秒(key 存在,但是没有设置过期时间)："+jedis.ttl("name"));//-1
//        System.out.println("ttl查询指定键过期剩余时间 秒(key 存在)："+jedis.ttl("expirekey"));//979

//        System.out.println("type 查询指定key的值类型(key 存在)："+jedis.type("name")); //string
//        System.out.println("type 查询指定key的值类型(key 不存在)："+jedis.type("name111")); //none

//        System.out.println("expire为指定key设置过期时间 秒(key 存在)："+jedis.expire("keyforexpire", 3000)); //1
//        System.out.println("查询指定键过期剩余时间 秒(key 存在)："+jedis.ttl("keyforexpire")); //3000
//        System.out.println("expire为指定key设置过期时间 秒(key 不存在)："+jedis.expire("not-exist-keyforexpire", 3000)); //0
//        System.out.println("查询指定键过期剩余时间 秒(key 不存在)："+jedis.ttl("not-exist-keyforexpire")); //-2
//        System.out.println("expireat为指定key设置过期时间 unix时间戳 秒(key 存在)："+jedis.expireAt("keyforexpireat", 1542000794000L)); //1
//        System.out.println("查询指定键过期剩余时间 秒(key 存在)："+jedis.ttl("keyforexpireat")); //1540458793121
//        System.out.println("expireat为指定key设置过期时间 unix时间戳(key 不存在)："+jedis.expireAt("not-exist-keyforexpireat", 1542000794000L)); //0
//        System.out.println("查询指定键过期剩余时间 秒(key 不存在)："+jedis.ttl("not-exist-keyforexpireat")); //-2

//        System.out.println("查找所有符合给定模式pattern的key(O(n))："+jedis.keys("*forexpire*")); //[keyforexpire, keyforexpireat]
//        System.out.println("查找所有符合给定模式pattern的key(O(n))："+jedis.keys("n?me")); //[neme, name, nkme]
//        System.out.println("查找所有符合给定模式pattern的key(O(n))："+jedis.keys("n[a,e]me")); //[neme, name]
//        System.out.println("查找所有符合给定模式pattern的key(O(n))："+jedis.keys("*not-exist*")); //[]
//        System.out.println("查找所有符合给定模式pattern的key(O(n))：" + jedis.keys("*")); //[dumpkey, counter:__rand_int__, key:__rand_int__, nkme, mylist, keyforexpireat, name, myset:__rand_int__, neme, keyforexpire]

//        System.out.println("键值迁移单个key(key 存在)："+jedis.migrate("47.101.152.55", 6379, "migratekey2", 0, 1000)); //OK
//        System.out.println("键值迁移单个key(key 不存在)："+jedis.migrate("47.101.152.55", 6379, "not-exist-migratekey", 0, 1000)); //NOKEY
//        System.out.println("键值迁移多个key(key 存在)："+jedis.migrate()); //未测试

//        System.out.println("获取多个指定key(key 全部存在)："+jedis.mget("name", "age", "migratekey")); //[namevalue, agevalue, migratevalue]
//        System.out.println("获取多个指定key(key 部分存在)："+jedis.mget("name", "age", "notexist")); //[namevalue, agevalue, null]
//        System.out.println("获取多个指定key(key 全部不存在)："+jedis.mget("notname", "notage", "notexist")); //[null, null, null]

//        System.out.println("移动指定key到指定库(key 存在且指定库有同名key)："+jedis.move("name", 1)); //0 ---注：move无效果
//        System.out.println("移动指定key到指定库(key 存在且指定库无同名key)："+jedis.move("nameformove", 1)); //1
//        System.out.println("移动指定key到指定库(key 不存在且指定库无同名key)："+jedis.move("not-nameformove", 1)); //0---注：move无效果
//        System.out.println("移动指定key到指定库(key 不存在且指定库有同名key)："+jedis.move("multi-nameformove", 1)); //0 ---注：move无效果

//        System.out.println("从内部察看指定key空闲时间(key 存在)："+jedis.objectIdletime("name")); //1353
//        System.out.println("从内部察看指定key空闲时间(key 不存在)："+jedis.objectIdletime("not-exist-name")); //null
//        System.out.println("从内部察看指定key键关联值的引用次数(key 存在)："+jedis.objectRefcount("name")); //1
//        System.out.println("从内部察看指定key键关联值的引用次数(key 不存在)："+jedis.objectRefcount("not-exist-name")); //null
//        System.out.println("从内部察看指定key关联值的内部表示形式(key 存在)："+jedis.objectEncoding("name")); //embstr
//        System.out.println("从内部察看指定key关联值的内部表示形式(key 不存在)："+jedis.objectEncoding("not-exist-name")); //null

//        System.out.println("移除指定key的生存时间(key 存在且无过期时间)："+jedis.persist("name")); //0
//        System.out.println("移除指定key的生存时间(key 存在且有过期时间)："+jedis.persist("name-expire")); //1
//        System.out.println("移除指定key的生存时间(key 不存在)："+jedis.persist("not-exist-name")); //0

//        System.out.println("pexpire为指定key设置过期时间 毫秒(key 存在)："+jedis.pexpire("keyforpexpire", TimeUnit.SECONDS.toMillis(1000))); //1
//        System.out.println("查询指定键过期剩余时间 毫秒(key 存在)："+jedis.pttl("keyforpexpire")); //1000000
//        System.out.println("pexpire为指定key设置过期时间 毫秒(key 不存在)："+jedis.pexpire("not-exist-keyforpexpire", TimeUnit.SECONDS.toMillis(3000))); //0
//        System.out.println("查询指定键过期剩余时间 毫秒(key 不存在)："+jedis.pttl("not-exist-keyforpexpire")); //-2
//        System.out.println("pexpireat为指定key设置过期时间 unix时间戳 毫秒(key 存在)："+jedis.expireAt("keyforpexpireat", 1542173594000L)); //1
//        System.out.println("查询指定键过期剩余时间 毫秒(key 存在)："+jedis.pttl("keyforexpireat")); //1540458704273152
//        System.out.println("pexpireat为指定key设置过期时间 unix时间戳 毫秒(key 不存在)："+jedis.expireAt("not-exist-keyforpexpireat", 1542000794000L)); //0
//        System.out.println("查询指定键过期剩余时间 毫秒(key 不存在)："+jedis.pttl("not-exist-keyforpexpireat")); //-2

//        System.out.println("从数据库随机获取一个key(库不为空)："+jedis.randomKey()); //name-expire
//        System.out.println("从数据库随机获取一个key(库为空)："+jedis.randomKey()); //null

//        System.out.println("rename修改指定key名称(key 存在)："+jedis.rename("neme", "nwme")); //OK
//        System.out.println("rename修改指定key名称(key 存在并且当前库中有跟newkey同名的键)："+jedis.rename("age", "name")); //OK--注：覆盖
//        System.out.println("rename修改指定key名称(key 不存在)："+jedis.rename("nkme", "nwme")); //异常：redis.clients.jedis.exceptions.JedisDataException: ERR no such key

//        System.out.println("renamenx修改指定key名称，必须newkey不存在(key 存在 newkey已存在)："+jedis.renamenx("name", "renamenxkey")); //0
//        System.out.println("renamenx修改指定key名称，必须newkey不存在(key 存在 newkey不存在)："+jedis.renamenx("migratekey4", "renamenxkey1")); //1
//        System.out.println("renamenx修改指定key名称，必须newkey不存在(key 不存在 newkey不存在)："+jedis.renamenx("not-exist-migratekey4", "renamenxkey2")); //redis.clients.jedis.exceptions.JedisDataException: ERR no such key
//        System.out.println("renamenx修改指定key名称，必须newkey不存在(key 不存在 newkey已存在)："+jedis.renamenx("not-exist-migratekey4", "renamenxkey1")); //redis.clients.jedis.exceptions.JedisDataException: ERR no such key

//        System.out.println("反序列化指定序列化值到指定key(key 不存在 序列化字符串不完整或者错误)："+jedis.restore("notname", 0, "\\x00\\x04neme\\t\\x0002\\x8e\\xd8\\xef\\x17-Q".getBytes())); //异常：redis.clients.jedis.exceptions.JedisDataException: ERR DUMP payload version or checksum are wrong
//        System.out.println("反序列化指定序列化值到指定key(key 不存在)："+jedis.restore("notname", 0, jedis.dump("name"))); //OK
//        System.out.println("反序列化指定序列化值到指定key(key 已经存在)："+jedis.restore("name", 0, "\\x00\\x04neme\\t\\x0002\\x8e\\xd8\\xef\\x17-Q".getBytes())); //异常：redis.clients.jedis.exceptions.JedisBusyException: BUSYKEY Target key name already exists.
    }
}
