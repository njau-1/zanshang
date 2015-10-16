package com.zanshang.framework.index;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Lookis on 7/3/15.
 */
public interface IndexTrapdoor<K, V> {

    public List<V> findById(K key);

    public Page<V> findByIdAndPageable(K key, Pageable pageable);
}
