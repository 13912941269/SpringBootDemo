package com.chemguan.controller;

import com.chemguan.dbmapper.VipUserMapper;
import com.chemguan.model.VipUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import java.util.List;

/**
 * Created by Administrator on 2018-07-17.
 */
@Controller
public class TestController {

    @Autowired
    private VipUserMapper dao;

    /**
     * beetl模板文件测试
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping("beetltest/{type}")
    public ModelAndView beetltest(@PathVariable String type, ModelMap modelMap){
        ModelAndView mav = new ModelAndView();
        List<VipUser> list = dao.selectAllUser();
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("email", "1367215312@qq.com");
        if ("html".equals(type)){
            mav.setViewName("/hello");
            return mav;
        }else if("json".equals(type)){
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setAttributesMap(modelMap);
            mav.setView(view);
            return mav;
        }
        return null;
    }




    /**
     * 测试
     */
    @RequestMapping("connecttest")
    public ModelAndView connecttest(){
        return new ModelAndView("connect");
    }
}
