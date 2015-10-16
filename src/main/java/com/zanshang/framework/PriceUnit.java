package com.zanshang.framework;

/**
 * Created by Lookis on 7/8/15.
 */
public enum PriceUnit {

    YUAN {
        @Override
        public long toYuan(long source) {
            return source;
        }

        @Override
        public long toCent(long source) {
            return source * 100;
        }
    },
    /**
     *
     */
    CENT {
        @Override
        public long toYuan(long source) {
            return source / 100;
        }

        @Override
        public long toCent(long source) {
            return source;
        }
    };

    public long toYuan(long source) {
        throw new AbstractMethodError();
    }

    public long toCent(long source) {
        throw new AbstractMethodError();
    }

}
