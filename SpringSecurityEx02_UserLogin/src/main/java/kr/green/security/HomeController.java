package kr.green.security;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		model.addAttribute("user", getPrincipal() );
		return "home";
	}
	@RequestMapping(value = "/home")
	public String homePage(Model model) {
		model.addAttribute("msg", "누구나 접근 가능한 페이지입니다.");
		model.addAttribute("user", getPrincipal() );
		return "welcome";
	}
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	@RequestMapping(value = {"/admin","/admin2"})
	public String admin(Model model) {
		model.addAttribute("msg", "관리자 전용 페이지 입니다.");
		model.addAttribute("user", getPrincipal() );
		return "admin";
	}
	@RequestMapping(value = {"/dba","/dba2"})
	public String dba(Model model) {
		model.addAttribute("msg", "DB 관리자 전용 페이지 입니다.");
		model.addAttribute("user", getPrincipal() );
		return "dba";
	}
	@RequestMapping(value = "/Access_Denied")
	public String access_Denied(Model model) {
		model.addAttribute("msg", "등록되지 않은 사용자 이거나, 비밀번호가 일치하지 않습니다.");
		return "access_Denied";
	}
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// 인증 정보를 얻어낸다
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 인증 정보가 있다면
		if(authentication!=null) {
			// 로그아웃을 시킨다.
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		return "redirect:/";
	}
	
	
	
	// 인증 정보를 시큐리티에서 얻어내는 메서드
	 private String getPrincipal(){
	        String userName = null;
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	 
	        if (principal instanceof UserDetails) {
	            userName = ((UserDetails)principal).getUsername();
	        } else {
	            userName = principal.toString();
	        }
	        return userName;
	 }
}
