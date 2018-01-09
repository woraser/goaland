package com.anosi.asset.i18n;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * i18n相关controller
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.controller
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/8 16:16
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/8 16:16
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
public class I18nController {

    /***
     * 中英文切换
     * @param lang
     * @return
     */
    @RequestMapping(value = "/i18n/changeSessionLanauage", method = RequestMethod.POST)
    public ModelAndView changeSessionLanauage(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam(value = "lang") String lang) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if ("zh".equals(lang)) {
            localeResolver.setLocale(request, response, new Locale("zh"));
        } else if ("en".equals(lang)) {
            localeResolver.setLocale(request, response, new Locale("en"));
        }
        return new ModelAndView("redirect:/index");
    }

}
