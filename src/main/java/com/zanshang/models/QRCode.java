package com.zanshang.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Lookis on 5/4/15.
 */
@Document(collection = "qrcode")
public class QRCode {

    @Id
    private String id;

    @Field
    private String content;

    private QRCode() {
    }

    public QRCode(String content) {
        this.content = content;
        this.id = DigestUtils.md5Hex(content);
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        QRCode qrCode = (QRCode) o;

        return id.equals(qrCode.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
