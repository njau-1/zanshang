package com.zanshang.controllers.web;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zanshang.utils.Ajax;
import com.zanshang.utils.Json;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lookis on 5/9/15.
 */
@Controller
@RequestMapping(value = StorageController.CONTROLLER_PATH)
public class StorageController {

    protected final Log logger = LogFactory.getLog(this.getClass());
    public static final String CONTROLLER_PATH = "/storage";
    public static final String IMAGE_CALLBACK_PATH = "/image/callback";
    public static final String IMAGE_PATHKEY = "/image/%s";

    @Value("${SERVER_CONTEXT}")
    String SERVER_CONTEXT;

    Auth auth = Auth.create("vBqs0zdPDJVAEpZ2lVKJcL_75hsdBvcYaF8oL68J", "qxOpK8bDxopmelgabhFtcpvCJ5_MqTLAZGsV3zyt");

    @RequestMapping(value = "/image/uptoken", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,String> generateUptoken(HttpServletRequest request){
        StringBuffer hostnameBuilder = new StringBuffer();
        hostnameBuilder.append(SERVER_CONTEXT);
//        hostnameBuilder.append("http://");
//        hostnameBuilder.append(request.getServerName());
//        if(request.getServerPort() != 80){
//            hostnameBuilder.append(":");
//            hostnameBuilder.append(request.getServerPort());
//        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        hostnameBuilder.append(request.getContextPath());
        hostnameBuilder.append(CONTROLLER_PATH);
        hostnameBuilder.append(IMAGE_CALLBACK_PATH);
        String token =  auth.uploadToken("image",null, 3600,
                new StringMap()
                        .putNotEmpty("mimeLimit", "image/jpeg;image/png;image/gif")
                        .putNotEmpty("callbackUrl", hostnameBuilder.toString())
                        .putNotNull("callbackFetchKey", 1)
                        .putNotEmpty("callbackBody", "format=$(imageInfo.format)"));
        Map<String,String> retMap = new HashMap<>();
        retMap.put("uptoken", token);
        return retMap;
    }

    //todo: 增加回调接口权限验证 http://developer.qiniu.com/docs/v6/api/overview/up/response/callback.html#callback-security
    @RequestMapping(value = StorageController.IMAGE_CALLBACK_PATH)
    @ResponseBody
    public String uploadImage(@RequestParam("format")String format){
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(ObjectId.get().toHexString());
        switch (format)
        {
            case "jpeg":
            case "jpg":
                responseBuilder.append(".jpg");
                break;
            case "png":
                responseBuilder.append(".png");
                break;
            case "gif":
                responseBuilder.append(".gif");
                break;
            default:
                responseBuilder.append(".");
                responseBuilder.append(format);
                logger.error("Qiniu callback with undefined image format:"+format);
                break;
        }
        String path = String.format(IMAGE_PATHKEY, responseBuilder.toString());
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("key", path);
        retMap.put("payload", Ajax.ok(path));
        return Json.toJson(retMap);
    }

}
