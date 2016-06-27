package com.timeshare.controller;

import com.alibaba.fastjson.JSON;
import com.timeshare.utils.AccessTokenBean;
import com.timeshare.utils.WeixinOauth;
import com.timeshare.utils.WeixinUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by user on 2016/6/23.
 */
@Controller
@RequestMapping(value = "/wx")
public class WxController {

    @ResponseBody
    @RequestMapping(value = "/sanctus",method = RequestMethod.GET)
    public String sanctus(@RequestParam String signature, @RequestParam String timestamp,
                          @RequestParam String nonce, @RequestParam String echostr) {
        String result = "";

        String token = "SanctusII";
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        String[] strArr = new String[] { token, timestamp, nonce };
        java.util.Arrays.sort(strArr);
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuffer sb = new StringBuffer();
        for (String str : strArr){
            sb.append(str);
        }
        MessageDigest mdSha1 = null;
        try{
            mdSha1 = MessageDigest.getInstance("SHA-1");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        mdSha1.update(sb.toString().getBytes());
        byte[] codedBytes = mdSha1.digest();
        String codedString = new BigInteger(1, codedBytes).toString(16);
        System.out.println(codedString);
        System.out.println(signature);
        // 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (codedString.equals(signature)){
            result = echostr;
        }
        return result;
    }


//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcd8903dd6a9552eb&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Ftime%2Fwx%2Foauth&response_type=code&scope=snsapi_userinfo&state=http%3A%2F%2Fwww.baidu.com#wechat_redirect
    @RequestMapping(value = "/oauth")
    public String oauth(HttpServletRequest request){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        WeixinOauth weixinOauth = new WeixinOauth();
        AccessTokenBean accessTokenBean = weixinOauth.obtainOauthAccessToken(code);
        WeixinUser weixinUser = weixinOauth.getUserInfo(accessTokenBean.getAccess_token(), accessTokenBean.getOpenid());
        System.out.println(JSON.toJSONString(weixinUser));
        return "redirect:"+state;
    }
}