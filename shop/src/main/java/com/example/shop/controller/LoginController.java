package com.example.shop.controller;

import com.example.shop.dao.logindao;
import com.example.shop.model.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/shop")
public class LoginController {
    @Autowired
    private com.example.shop.dao.logindao logindao;

    @RequestMapping(value="/login",method = {RequestMethod.POST, RequestMethod.GET})
    public String home(){
        return "login";
    }

    @RequestMapping(value="/zhuce",method = RequestMethod.GET)
    public String zhucePage(){
        return "zhuce";
    }
    @RequestMapping(value="/zhuce_check",method = RequestMethod.POST)
    public String zhuceCheck(@RequestParam("userid") String userid,
                             @RequestParam("password") String password,
                             Map<String,Object> map){
        System.out.println(userid);
        System.out.println(password);
        List<UserInfoBean> result = logindao.findByUserid(userid);
        if(result.size()!=0)
        {
            map.put("msg","用户名已存在，请换一个试试");
            return "zhuce";
        }else{
            int addresult=logindao.AddNewUser(userid,password);
            System.out.println(addresult);
            map.put("msg","注册成功");
            return "login";
            }

    }

    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public String gotouserpage(@RequestParam("userid") String userid,
                               @RequestParam("password") String password,
                               Map<String,Object> map, HttpServletRequest request){
        System.out.println(userid);
        System.out.println(password);
        if("admin".equals(userid)&&"123456".equals(password))
        {
            HttpSession session=request.getSession();
            session.setAttribute("Userid",userid);
//            attr.addFlashAttribute("Userid",userid);
            return "redirect:/admin/hello";
        }
        else{
            List<UserInfoBean> result = logindao.findByUseridAndPassword(userid,password);
            System.out.println(result.size());
            if(result.size()!=0)
            {
                HttpSession session=request.getSession();
                session.setAttribute("Userid",userid);
//                attr.addFlashAttribute("Userid",userid);
                return "redirect:/user/hello";
            }else{
            map.put("msg","账号或密码出错");
            return "login";
            }
        }

    }
}
