package com.zanshang.framework.spring;

import com.zanshang.framework.Price;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Lookis on 7/8/15.
 */
public class PriceWriteConverter implements Converter<Price, Long> {

    @Override
    public Long convert(Price source) {
        return source.getUnit().toCent(source.getPrice());
    }
}
