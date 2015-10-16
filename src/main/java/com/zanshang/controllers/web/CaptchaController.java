package com.zanshang.controllers.web;

import com.zanshang.config.spring.CacheConfig;
import com.zanshang.framework.Ticket;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Lookis on 7/21/15.
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    CacheManager cacheManager;

    @RequestMapping("/image")
    public ResponseEntity<byte[]> image(@Ticket String ticket) throws IOException {
        final Cache cache = cacheManager.getCache(CacheConfig.CacheKey.CAPTCHA.getName());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Captcha build = new Captcha.Builder(150, 50).addBackground().addNoise().addNoise().addText().addBorder
                ().build();
        cache.put(ticket, build.getAnswer());
        final BufferedImage image = build.getImage();
        ImageIO.write(image, "png", outputStream);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_PNG);
        responseHeaders.setContentLength(outputStream.size());
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), responseHeaders, HttpStatus.OK);
    }
}
