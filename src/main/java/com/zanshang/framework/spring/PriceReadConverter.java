package com.zanshang.framework.spring;

import com.zanshang.framework.Price;
import com.zanshang.framework.PriceUnit;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Lookis on 7/8/15.
 */
public class PriceReadConverter implements Converter<Long, Price>{

    @Override
    public Price convert(Long source) {
        return new Price(source.longValue(), PriceUnit.CENT);
    }
}
