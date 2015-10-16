package com.zanshang.notify.template;

import com.zanshang.notify.MessageTemplateEngine;
import com.zanshang.notify.constants.NotifyBusinessType;
import com.zanshang.notify.constants.TemplateName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuming on 15/9/7.
 */
public class StringTemplateEngine implements MessageTemplateEngine {

    Map<NotifyBusinessType,String> config = new HashMap<NotifyBusinessType, String>();

    public StringTemplateEngine() {
        this.config.put(NotifyBusinessType.DEMO, "helloworld %s");
    }

    @Override
    public String render(String template, Map<String, String> model) {
        return String.format(config.get(template), model.toString());
    }
}
