package com.anosi.asset.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.anosi.asset.controller.remote.BaseRemoteController;
import com.anosi.asset.util.URLConncetUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 请求fbox
 *
 * @ProjectName: goaland
 * @Package: com.anosi.asset.controller
 * @Description:
 * @Author: jinyao
 * @CreateDate: 2018/1/9 10:03
 * @UpdateUser: jinyao
 * @UpdateDate: 2018/1/9 10:03
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
public class FBoxController extends BaseRemoteController {

    @RequestMapping(value = "/api/client/box/grouped/boxs", method = RequestMethod.GET)
    public JSONArray getFBoxs(HttpServletRequest request) throws IOException {
        String result = URLConncetUtil.sendGetString(remoteComponent.getFullPath(request.getServletPath()),
                request.getParameterMap(), remoteComponent.getHearders());
        return JSON.parseArray(result);
    }

}
