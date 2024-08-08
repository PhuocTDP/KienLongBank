package com.klb.caches.config;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

@Slf4j
public class LoggingCache implements Cache {

  private final String name;
  private final Cache delegate;

  public LoggingCache(String name, Cache delegate) {
    this.name = name;
    this.delegate = delegate;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Object getNativeCache() {
    return delegate.getNativeCache();
  }

  @Override
  public ValueWrapper get(Object key) {
    ValueWrapper value = delegate.get(key);
    if (value != null) {
      log.info("...Cache hit for key: {}", key);
    } else {
      log.info("...Cache miss for key: {}", key);
    }
    return value;
  }

  @Override
  public <T> T get(Object key, Class<T> type) {
    return delegate.get(key, type);
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    return delegate.get(key, valueLoader);
  }

  @Override
  public void put(Object key, Object value) {
    log.info("...Cache put for key: {}", key);
    delegate.put(key, value);
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    log.info("...Cache putIfAbsent for key: {}", key);
    return delegate.putIfAbsent(key, value);
  }

  @Override
  public void evict(Object key) {
    log.info("...Cache evict for key: {}", key);
    delegate.evict(key);
  }

  @Override
  public void clear() {
    log.info("...Cache clear");
    delegate.clear();
  }
}
