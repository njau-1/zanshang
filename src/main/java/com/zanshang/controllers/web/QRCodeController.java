package com.zanshang.controllers.web;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zanshang.models.QRCode;
import com.zanshang.services.QRCodeTrapdoor;
import com.zanshang.services.qrcode.QRCodeTrapdoorImpl;
import com.zanshang.utils.AkkaTrapdoor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;

/**
 * Created by Lookis on 5/4/15.
 */
@Controller
@RequestMapping(QRCodeTrapdoor.PATH)
public class QRCodeController {

    Writer writer = new QRCodeWriter();

    @Autowired
    AkkaTrapdoor akkaTrapdoor;

    QRCodeTrapdoor qrcodeService;

    private static int WIDTH = 280;

    @PostConstruct
    public void init() {
        qrcodeService = akkaTrapdoor.createTrapdoor(QRCodeTrapdoor.class, QRCodeTrapdoorImpl.class);
    }

    @RequestMapping(value = "/{codeId}.*", method = RequestMethod.GET)
    public ResponseEntity<byte[]> qrCode(@PathVariable("codeId") String codeId) throws Exception {
        QRCode qrcode = qrcodeService.get(codeId);
        if (qrcode == null) {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }
        BitMatrix bitMatrix = writer.encode(qrcode.getContent(), BarcodeFormat.QR_CODE, WIDTH, WIDTH);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, QRCodeTrapdoor.ENDING, outputStream);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_PNG);
        responseHeaders.setContentLength(outputStream.size());
        responseHeaders.setExpires(2592000l);
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), responseHeaders, HttpStatus.OK);
    }
}
