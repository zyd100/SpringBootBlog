package com.zyd.blog.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;


public class RedisUtil {

  @SuppressWarnings("unchecked")
  private static final RedisTemplate<String, Object> redisTemplate =
      SpringContextUtil.getBean("redisTemplate", RedisTemplate.class);

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

  private static final Long DEFAULT_TIME = 60L;

  /**********************************************************************************
   * redis-公共操作
   **********************************************************************************/

  /**
   * 
   * <p>
   * Title: generateKey
   * </p>
   * <p>
   * Description: 生成模板key值
   * </p>
   * 
   * @param values 需要合并的字符串
   * @return
   */
  public static String generateKey(String... values) {
    if (values.length < 1) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (String str : values) {
      sb.append(str);
      sb.append("#");
    }

    return sb.substring(0, sb.length() - 1);
  }

  /**
   * 指定缓存失效时间
   *
   * @param key 键
   * @param time 时间(秒)
   * @return
   */
  public static boolean expire(String key, long time) {
    try {
      if (time > 0) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    } catch (Exception e) {
      LOGGER.error("【redis：指定缓存失效时间-异常】", e);
      return false;
    }
  }

  /**
   * 根据key 获取过期时间
   *
   * @param key 键 不能为null
   * @return 时间(秒) 返回0代表为永久有效;如果该key已经过期,将返回"-2";
   */
  public static long getExpire(String key) {
    return redisTemplate.getExpire(key, TimeUnit.SECONDS);
  }

  /**
   * 判断key是否存在
   *
   * @param key 键
   * @return true 存在 false不存在
   */
  public static boolean exists(String key) {
    try {
      return redisTemplate.hasKey(key);
    } catch (Exception e) {
      LOGGER.error("【redis：判断{}是否存在-异常】", key, e);
      return false;
    }
  }


  /**********************************************************************************
   * redis-String类型的操作
   **********************************************************************************/

  /**
   * 普通缓存放入
   *
   * @param key 键
   * @param value 值
   * @return true成功 false失败
   */
  public static boolean set(String key, Object value) {
    try {
      redisTemplate.opsForValue().set(key, value);
      return true;
    } catch (Exception e) {
      LOGGER.error("【redis：普通缓存放入-异常】", e);
      return false;
    }
  }

  /**
   * 普通缓存放入并设置时间
   *
   * @param key 键
   * @param value 值
   * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
   * @return true成功 false 失败
   */
  public static boolean set(String key, Object value, long time) {
    try {
      if (time > 0) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
      } else {
        set(key, value);
      }
      return true;
    } catch (Exception e) {
      LOGGER.error("【redis：普通缓存放入并设置时间-异常】", e);
      return false;
    }
  }

  /**
   * 
   * <p>
   * Title: setByDefaultTime
   * </p>
   * <p>
   * Description: 普通缓存放入并设置默认时间(60s)
   * </p>
   * 
   * @param key 键
   * @param value 值
   * @return true成功 false 失败
   */
  public static boolean setByDefaultTime(String key, Object value) {
    try {
      set(key, value, DEFAULT_TIME);
      return true;
    } catch (Exception e) {
      LOGGER.error("【redis：普通缓存放入并设置时间-异常】", e);
      return false;
    }
  }

  /**
   * 递增
   *
   * @param key 键
   * @param delta 要增加几(大于0)
   * @return
   */
  public static long incr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    return redisTemplate.opsForValue().increment(key, delta);
  }

  /**
   * 递减
   *
   * @param key 键
   * @param delta 要减少几(小于0)
   * @return
   */
  public static long decr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    return redisTemplate.opsForValue().increment(key, -delta);
  }

  /**
   * 删除缓存
   *
   * @param key 可以传一个值 或多个
   */
  @SuppressWarnings("unchecked")
  public static void del(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(key[0]);
      } else {
        redisTemplate.delete(CollectionUtils.arrayToList(key));
      }
    }
  }


  /**
   * 获取缓存
   *
   * @param key redis的key
   * @param clazz value的class类型
   * @param <T>
   * @return value的实际对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T get(String key, Class<T> clazz) {
    Object obj = key == null ? null : redisTemplate.opsForValue().get(key);
    if (!obj.getClass().isAssignableFrom(clazz)) {
      throw new ClassCastException("类转化异常");
    }
    return (T) obj;
  }

  /**
   * 获取泛型
   *
   * @param key 键
   * @return 值
   */
  public static Object get(String key) {
    return key == null ? null : redisTemplate.opsForValue().get(key);
  }

  /**
   * 
   * <p>
   * Title: getKey
   * </p>
   * <p>
   * Description: 获取符合规则的key
   * </p>
   * 
   * @param pattern 前缀+'*'
   * @return 字符串合集
   */
  public static Set<String> getKey(String pattern) {
    return pattern == null ? null : redisTemplate.keys(pattern);
  }
}
