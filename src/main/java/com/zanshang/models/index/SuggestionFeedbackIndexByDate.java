package com.zanshang.models.index;

import com.zanshang.framework.index.BaseArrayIndex;
import com.zanshang.models.SuggestionFeedback;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by xuming on 15/9/25.
 */
@Document(collection = "suggestionfeedback_indexby_date")
public class SuggestionFeedbackIndexByDate extends BaseArrayIndex<String, SuggestionFeedback> {

    public SuggestionFeedbackIndexByDate(String key) {
        super(key);
    }

}
