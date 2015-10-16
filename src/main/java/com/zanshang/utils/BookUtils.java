package com.zanshang.utils;

import com.zanshang.constants.BookType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Lookis on 7/6/15.
 */
@Service
public class BookUtils {

    @Autowired
    MessageSource messageSource;

    public BookType parse(String txt, Locale locale) {
        for (BookType bookType : BookType.values()) {
            if (StringUtils.equals(messageSource.getMessage(bookType.getMessageCode(), null, locale), txt.trim())) {
                return bookType;
            }
        }
        return BookType.OTHER;
    }

    public String parse(BookType type, Locale locale) {
        try {
            return messageSource.getMessage(type.getMessageCode(), null, locale);
        }catch (NoSuchMessageException e) {
            return messageSource.getMessage(BookType.OTHER.getMessageCode(), null, locale);
        }
    }
}
