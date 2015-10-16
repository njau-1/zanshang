package com.zanshang.notify;

import com.zanshang.notify.constants.TemplateName;

import java.util.Map;

/**
 * Created by xuming on 15/9/7.
 */
public interface MessageTemplateEngine {

    String render(String templateName,Map<String, String > model);
}
