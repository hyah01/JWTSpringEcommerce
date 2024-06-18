//package genspark.example.JWTSpringEcommerce.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class FrontEndController {
//        @GetMapping("/")
//        public String index(){
//            return "index";
//        }
//
//        @GetMapping("/admin")
//        public String home(Model model, Authentication authentication){
//            if (authentication != null && authentication.getPrincipal() instanceof OAuth2User){
//                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
//                String name = oauth2User.getAttribute("name");
//                model.addAttribute("name",name);
//            }
//            return "admin";
//        }
//
//        @GetMapping("/login")
//        public String loging(){
//            return "login";
//        }
//
//        @GetMapping("logout-success")
//        public String logoutSuccess(){
//            return "logout-success";
//        }
//
//
//}
