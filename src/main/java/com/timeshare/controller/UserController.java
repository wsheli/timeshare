package com.timeshare.controller;

import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by adam on 2016/6/11.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController{

    @Autowired
    UserService userService;

    @RequestMapping(value = "/to-userinfo")
    public String toUserInfo() {
        return "userinfo";
    }

    @RequestMapping(value = "/to-upload-img")
    public String toUploadImg(Model model) {
        UserInfo userInfo = getCurrentUser("");
        ImageObj imageObj = userService.findUserImg(userInfo.getUserId(), Contants.ITEM_SHOW_IMG);
        String objId = "";
        String imgType = "";
        String imageId = "";
        if(imageObj == null){
            objId = userInfo.getUserId();
            imgType = Contants.ITEM_SHOW_IMG;
        }else{
            objId = imageObj.getObjId();
            imgType = imageObj.getImageType();
            imageId = imageObj.getImageId();
        }
        model.addAttribute("objId",objId);
        model.addAttribute("imageType",imgType);
        model.addAttribute("imageId",imageId);
        return "uploadimg";
    }
    @RequestMapping(value = "/save-img")
    public void saveUserImg(@RequestParam String imageId,@RequestParam String imgUrl){
        ImageObj obj = new ImageObj();
        //TODO userId
        obj.setObjId("admin");
        obj.setImageUrl(imgUrl);
        obj.setImageType(Contants.ITEM_SHOW_IMG);
        userService.saveOrUpdateImg(obj);
    }

}
