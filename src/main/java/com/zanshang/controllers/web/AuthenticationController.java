package com.zanshang.controllers.web;

import com.zanshang.constants.BusinessType;
import com.zanshang.constants.OAuthType;
import com.zanshang.framework.Ticket;
import com.zanshang.framework.spring.AccountAwaredAccountStatusException;
import com.zanshang.framework.wechat.WechatOAuth2AuthenticationFilter;
import com.zanshang.framework.weibo.WeiboOAuth2AuthenticationFilter;
import com.zanshang.models.EmailAccount;
import com.zanshang.services.oauth.OAuthUrlProcessService;
import com.zanshang.utils.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * Created by Lookis on 5/13/15.
 */
@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    @Value("${WECHAT_APPID}")
    String WECHAT_APPID;

    @Value("${WEIBO_APPID}")
    String WEIBO_APPID;

    @Value("${WECHAT_MP_ID}")
    String MP_ID;

    @Value("${SERVER_CONTEXT}")
    String SERVER_CONTEXT;

    @Autowired
    MessageSource messageSource;

    @Autowired
    OAuthUrlProcessService oAuthUrlProcessService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET, params = "!success")
    public Object authenticationPage(HttpServletRequest request, @Ticket String ticket) {
        ModelAndView mav = getLoginView(request, ticket);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, params = "error")
    public Object authenticationErrorPage(HttpServletRequest request, @Ticket String ticket) {
        AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes
                .AUTHENTICATION_EXCEPTION);
        logger.debug("Authentication Exception.", exception);
        if (exception instanceof AccountAwaredAccountStatusException) {
            UserDetails userDetails = ((AccountAwaredAccountStatusException) exception).getUserDetails();
            if (exception.getCause() instanceof DisabledException && userDetails instanceof EmailAccount) {
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.error", null,
                        request.getLocale()));
                mav.addObject("content", messageSource.getMessage("authentication.email.disable", new String[]{request
                        .getContextPath() + "/account/mails/activation?email=" + ((EmailAccount) userDetails).getEmail()}, request
                        .getLocale()));
                return mav;
            } else if (exception.getCause() instanceof LockedException && userDetails instanceof EmailAccount) {
                ModelAndView mav = new ModelAndView("return");
                mav.addObject("title", messageSource.getMessage("return.title.error", null, request.getLocale()));
                mav.addObject("content", messageSource.getMessage("authentication.email.locked", null, request
                        .getLocale()));
                return mav;
            } else {
                ModelAndView mav = getLoginView(request, ticket);
                return mav;
            }
        } else if (exception instanceof BadCredentialsException) {
            ModelAndView mav = getLoginView(request, ticket);
            mav.addObject("bad_credentials", true);
            return mav;
        } else {
            logger.warn("Unknown Exception.", exception);
            ModelAndView mav = getLoginView(request, ticket);
            return mav;
        }
    }

    private ModelAndView getLoginView(HttpServletRequest request, String ticket) {
        ModelAndView mav = new ModelAndView("2_1_1");
        boolean isWechat = Request.isWechat(request);
        String returnUrl = request.getParameter("return");
        returnUrl = returnUrl == null ? request.getHeader("referer"): returnUrl;
        mav.addObject("return", returnUrl);
        if(isWechat) {
            mav.addObject("wechat_redirect_uri", oAuthUrlProcessService.bulidUrl(BusinessType.LOGIN, OAuthType.WECHAT_MP, ticket, returnUrl, URLEncoder.encode(SERVER_CONTEXT + request.getContextPath() + WechatOAuth2AuthenticationFilter.CALLBACK_REQUEST_PATH)));
            mav.addObject("isWechat", isWechat);
        }else {
            mav.addObject("wechat_appid", WECHAT_APPID);
            mav.addObject("wechat_redirect_uri", oAuthUrlProcessService.bulidUrl(BusinessType.LOGIN, OAuthType.WECHAT, ticket, returnUrl, URLEncoder.encode(SERVER_CONTEXT + request.getContextPath() + WechatOAuth2AuthenticationFilter.CALLBACK_REQUEST_PATH)));
        }
        mav.addObject("weibo_appid", WEIBO_APPID);
        mav.addObject("weibo_redirect_uri", oAuthUrlProcessService.bulidUrl(BusinessType.LOGIN, OAuthType.WEIBO, ticket, returnUrl, URLEncoder.encode(SERVER_CONTEXT + request.getContextPath() + WeiboOAuth2AuthenticationFilter.CALLBACK_REQUEST_PATH)));
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, params = "success")
    public Object authenticationSuccessPage(Device device, @Ticket String ticket) {
        String returnUrl = oAuthUrlProcessService.getParamByKey(BusinessType.LOGIN, ticket, "return");
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        if(device.isMobile()) {
            return "redirect:/projects";
        }else {
            return "redirect:/";
        }
    }
}
