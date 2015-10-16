package com.zanshang.services.feedback;

import com.zanshang.framework.index.AddToIndexActor;
import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.SuggestionFeedback;
import com.zanshang.models.index.SuggestionFeedbackIndexByDate;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;

/**
 * Created by xuming on 15/9/25.
 */
public class SuggestionFeedbackSaveActor extends AddToIndexActor<String, SuggestionFeedback> {

    public SuggestionFeedbackSaveActor(ApplicationContext spring) {
        super(spring);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        getMongoTemplate().save(extractValue(o));
        super.onReceive(o);
    }

    @Override
    public SuggestionFeedback extractValue(Object o) {
        return (SuggestionFeedback) o;
    }

    @Override
    public String extractKey(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(extractValue(o).getCreateDate());
    }

    @Override
    public Class<? extends BaseArrayIndex<String, SuggestionFeedback>> indexClz() {
        return SuggestionFeedbackIndexByDate.class;
    }
}
