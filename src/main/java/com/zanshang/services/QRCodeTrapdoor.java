package com.zanshang.services;

import com.zanshang.models.QRCode;

/**
 * Created by Lookis on 6/4/15.
 */
public interface QRCodeTrapdoor {

    public static final String PATH = "/qrcode";

    public static final String ENDING = "png";

    public final String pathFormat = PATH + "/%s." + ENDING;

    QRCode get(String id);

    void save(QRCode qrcode);

    void delete(String id);
}
