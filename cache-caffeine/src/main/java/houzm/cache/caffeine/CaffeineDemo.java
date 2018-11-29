package houzm.cache.caffeine;

import houzm.cache.common.IdWorker;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Weigher;

/**
 * Package: houzm.cache.caffeine
 * Author: houzm
 * Date: Created in 2018/10/18 18:32
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： caffeine demo
 *
 */
public class CaffeineDemo {
    private static Logger logger = LoggerFactory.getLogger(CaffeineDemo.class);
    public static void main(String[] args) {
        //1. 创建实例
//        createCaffeine();
        //2. 创建实例时，可配置的普通参数
//        initConfigCreateCaffeine();
        /**
         * 缓存填充：手动填充加载，同步填充加载，异步填充加载
         */
        //3. 手动填充加载
//        putGetKVByManual();
        //4. 同步填充加载
//        loadingCache();
        //5. 异步填充加载
//        asyncLoadingCache();
        /**
         * 值回收：基于大小，基于时间，基于引用回收
         */
        //6. 基于大小
        //6.1 基于保存键值数量大小
//        delByMaximumSize();
        //6.2 基于权重
//        delByMaximumWeight();
        //7. 基于时间
        //7.1 访问后到期
//        expireAfterAccess();
        //7.2 写入后到期
        expireAfterWrite();
        //7.3 自定义策略
//        coustomEviction();
        //8. 基于引用
//        referenceBase();
        //8. 刷新
//        refresh();
        //9. 统计
//        recordStats();
    }

    /**
     * 统计
     */
    private static void recordStats() {
        Cache<Long, String> cache = Caffeine.newBuilder().recordStats().build();
        cache.getIfPresent(1L);
        cache.getIfPresent(1L);
        cache.put(2L, "value 2");
        logger.debug("{}", cache.getIfPresent(2L));
        logger.debug("{}", cache.stats().hitCount());
        logger.debug("{}", cache.stats().missCount());
        logger.debug("{}", cache.stats().loadCount());
        logger.debug("{}", cache.stats());
    }

    /**
     * 刷新
     */
    private static void refresh() {
        LoadingCache<Long, String> cache = Caffeine.newBuilder()
                .refreshAfterWrite(1L, TimeUnit.SECONDS)
//                .expireAfterAccess(1L, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, String>() {
                    @Nullable
                    @Override
                    public String load(@Nonnull Long key) throws Exception {
//                        logger.debug("key refresh");
                        return "value "+key;
                    }

                    @Nullable
                    @Override
                    public String reload(@Nonnull Long key, @Nonnull String oldValue) {
                        logger.debug("key refresh : {}", key);
                        return "reload value "+key;
                    }
                });
        cache.put(1L, "value 1");
        logger.debug(" wait 0ms after create : {} ", cache.getIfPresent(1L));
        try {
            Thread.sleep(100);
            logger.debug(" wait 100ms after create : {} ", cache.getIfPresent(1L));
            Thread.sleep(1100);
            logger.debug(" wait 1100ms after create， refresh and return old value : {} ", cache.getIfPresent(1L));
            logger.debug(" wait 0ms after refresh : {} ", cache.getIfPresent(1L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void referenceBase() {
        Cache<Long, String> cache = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                .build();
        Cache<Long, String> cache2 = Caffeine.newBuilder()
                .softValues()
                .build();
    }

    /**
     * 自定义策略
     */
    private static void coustomEviction() {
        Cache<Long, String> cache = Caffeine.newBuilder()
                .expireAfter(new Expiry<Long, String>() {
                    @Override
                    public long expireAfterCreate(@Nonnull Long key, @Nonnull String value, long currentTime) {
                        return 1000*1000*1000;
                    }

                    @Override
                    public long expireAfterUpdate(@Nonnull Long key, @Nonnull String value, long currentTime, long currentDuration) {
                        return 1000*1000*2000;
                    }

                    @Override
                    public long expireAfterRead(@Nonnull Long key, @Nonnull String value, long currentTime, long currentDuration) {
                        return 1000*1000*3000;
                    }
                })
                .build();
        cache.put(1L, "value 1");

        try {
            Thread.sleep(900L);
            logger.debug(" wait 900ms after create : {} ", cache.getIfPresent(1L));
            Thread.sleep(200L);
            logger.debug(" wait 900ms after create : {} ", cache.getIfPresent(1L));
            cache.put(2L, "value 2");
            logger.debug(" wait 0ms after read : {} ", cache.getIfPresent(2L));
            Thread.sleep(3100L);
            logger.debug(" wait 3100ms after read : {} ", cache.getIfPresent(2L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入后到期
     */
    private static void expireAfterWrite() {
        Cache<Long, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS) //写入1s，自动删除
                .build();
        cache.put(1L, "value 1");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(" wait 500ms value is {}", cache.getIfPresent(1L));

        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(" wait 500ms+600ms value is {}", cache.getIfPresent(1L));
    }

    /**
     * 访问后到期
     */
    private static void expireAfterAccess() {
        Cache<Long, String> cache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.SECONDS) //1s，没有被访问，自动删除
                .build();
        cache.put(1L, "value 1");
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(" wait 900ms {}", cache.getIfPresent(1L));
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(" wait 900ms {}", cache.getIfPresent(1L));
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(" wait 900ms {}", cache.getIfPresent(1L));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug(" wait 1000ms {}", cache.getIfPresent(1L));
    }

    /**
     * 值回收--基于权重
     */
    private static void delByMaximumWeight() {
        LoadingCache<Long, String> cache = Caffeine.newBuilder()
                .maximumWeight(5)
                .weigher(new Weigher<Long, String>() {
                    @Override
                    public int weigh(@Nonnull Long key, @Nonnull String value) {
                        logger.debug("weighter : {}", key);
                        return key.intValue()*2;
                    }
                })
                .build(new CacheLoader<Long, String>() {
                    @Nullable
                    @Override
                    public String load(@Nonnull Long key) throws Exception {
                        return "data for key : " + key;
                    }
                });
        IntStream.rangeClosed(1, 10).forEach(key->{
            String value = cache.get(Long.valueOf(key));
            logger.debug(" key : {} , value ：{} , weight : {}", key, value, cache.estimatedSize());
        });
    }

    /**
     * 值回收--基于大小
     */
    private static void delByMaximumSize() {
        Cache<Long, String> cache = Caffeine.newBuilder()
                .maximumSize(1)
                .build();
        cache.put(1L, "value for 1");
        cache.put(2L, "value for 2");
        cache.put(3L, "value for 3");
        String value = cache.getIfPresent(1L);
        String value2 = cache.getIfPresent(2L);
        String value3 = cache.getIfPresent(3L);
        cache.cleanUp();
        logger.debug(value);
        logger.debug(value2);
        logger.debug(value3);
        logger.debug("cache mumsize : {}", cache.estimatedSize());


        LoadingCache<Long, String> loaddingCache = Caffeine.newBuilder()
                .maximumSize(1)
                .build(new CacheLoader<Long, String>() {
                    @Nullable
                    @Override
                    public String load(@Nonnull Long aLong) throws Exception {
                        return null;
                    }
                });
        loaddingCache.put(1L, "value for 1");
        loaddingCache.put(2L, "value for 2");
        loaddingCache.put(3L, "value for 3");
        String value11 = loaddingCache.get(1L);
        String value12 = loaddingCache.get(2L);
        String value13 = loaddingCache.get(3L);
        cache.cleanUp();
        logger.debug(value11);
        logger.debug(value12);
        logger.debug(value13);
        logger.debug("cache mumsize : {}", loaddingCache.estimatedSize());
    }

    /**
     * 异步填充加载
     */
    private static void asyncLoadingCache() {
        AsyncLoadingCache<Long, String> asyncCache = Caffeine.newBuilder().buildAsync(new CacheLoader<Long, String>() {
            @Nullable
            @Override
            public String load(@Nonnull Long aLong) throws Exception {
                return "value for the key : " + aLong;
            }
        });
        List<Long> keysList = new LinkedList<>();
        IntStream.rangeClosed(1, 10).forEach(key->{
            keysList.add(Long.valueOf(key));
        });
        logger.debug("======================= get all ========================");
        CompletableFuture<Map<Long, String>> allKeyValue = asyncCache.getAll(keysList);
        try {
            Map<Long, String> mapKeyValue = allKeyValue.get();
            for (Map.Entry<Long, String> entry : mapKeyValue.entrySet()) {
                logger.debug("key : {} , value : {}", entry.getKey(), entry.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            logger.debug("========================= get ============================");
            CompletableFuture<String> valueFuture = asyncCache.get(2L);
            logger.debug("key : {} , value : {}", 2L, valueFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步填充加载
     */
    private static void loadingCache() {
        LoadingCache<Long, String> cache = Caffeine.newBuilder().build(new CacheLoader<Long, String>() {
            @Nullable
            @Override
            public String load(@Nonnull Long aLong) throws Exception {
                return " data for " + aLong;
            }
        });
        long id = IdWorker.id();
        String loadingcache = cache.get(id);
        logger.debug("key not existed {}-{}", id, loadingcache);
        // 批量查找可以使用getAll方法。默认情况下，getAll将会对缓存中没有值的key分别调用CacheLoader.load方法来构建缓存的值
        List<Long> idList = new LinkedList<>();
        IntStream.rangeClosed(0, 10).forEach(key->{
            long idForAll = IdWorker.id();
            idList.add(idForAll);
        });
        for (int i = 0; i < idList.size(); i++) { //为偶数索引的键进行赋值，奇数的仍未存在于缓存
            if (i % 2 == 0) {
                Long aLong = idList.get(i);
                cache.put(aLong, "value of key:"+aLong);
            }
        }
        Map<Long, String> allKeyValue = cache.getAll(idList);
        logger.debug("keys , some are exist in cache, others are existed.} ");
        for (Map.Entry<Long, String> entry : allKeyValue.entrySet()) {
            logger.debug("key : {} , value : {}", entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加值到缓存，从缓存中获取值
     */
    private static void putGetKVByManual() {
        Cache<Long, String> cache = Caffeine.newBuilder().build();

        /*
            该方式添加键值到缓存，会覆盖旧值
         */
        long id = IdWorker.id();
        cache.put(id, "show snowflake 1");
        String value = cache.getIfPresent(id);
        logger.debug("{}-{}", id, value);

        /*
            该方式添加键值到缓存
         */
        // 如果该键存在值，取出该值并返回
        String valueExisted = cache.get(id, new Function<Long, String>() {
            @Override
            public String apply(Long aLong) {
                logger.debug("获取已经存在于缓存的key：{}", aLong);
                return "show cache.get(key, function) ";
            }
        });
        logger.debug("key existed : {}-{}", id, valueExisted);

        // 如果该键不存在，调用function获取并返回value，并将key-value设置到缓存
        // get方法以阻塞方式调用，即使在多线程并发情况下也只会被设置一次，避免了写入竞争
        // 如果调用function方法返回NULL，则cache.get返回null，如果调用该方法抛出异常，则get方法也会抛出异常。
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        long idt = IdWorker.id();
        Future<String> valuet = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String valuet = cache.get(idt, new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        String value = "线程名称: ".concat(Thread.currentThread().getName()).concat(" 并发测试get");
                        logger.debug("{} 获取不存在于缓存的key：{}-{}", Thread.currentThread().getName(), aLong, value);
                        return value;
                    }
                });
                countDownLatch.countDown();
                return valuet;
            }
        });
        Future<String> valuet2 = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String valuet = cache.get(idt, new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) {
                        String value = "线程名称: ".concat(Thread.currentThread().getName()).concat(" 并发测试get");
                        logger.debug("{} 获取不存在于缓存的key：{}-{}", Thread.currentThread().getName(), aLong, value);
                        return value;
                    }
                });
                countDownLatch.countDown();
                return valuet;
            }
        });
        try {
            logger.debug("key not existed : {}-{}", idt, valuet.get());
            logger.debug("key not existed : {}-{}", idt, valuet2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 创建实例时，可配置的普通参数
     */
    private static void initConfigCreateCaffeine() {
        Cache<Long, String> cache = Caffeine.newBuilder()
                .maximumSize(100) //存储最大记录数
                .recordStats() //统计信息
                .expireAfterAccess(1, TimeUnit.SECONDS) //n秒没访问，自动过期删除
                .expireAfterWrite(2, TimeUnit.SECONDS) //写入n秒后，自动过期删除
                .build();
    }

    /**
     * 1. 创建实例
     */
    private static void createCaffeine() {
        Cache<Long, String> cache = Caffeine.newBuilder().build();
        long id = IdWorker.id();
        cache.put(id, "show snowflake 1");
        String value = cache.getIfPresent(id);
        logger.debug("{}-{}", id, value);
    }
}
