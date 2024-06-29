package org.demo.web.controller;

import org.demo.bean.jpa.Suratkeluar;
import org.demo.bean.jpa.Suratmasuk;
import org.demo.business.service.SuratKeluarService;
import org.demo.business.service.SuratMasukService;
import org.demo.business.service.UploadDocumentService;
import org.demo.business.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {


    @Resource
    private SuratMasukService suratMasukService; // Injected by Spring
    @Resource
    private SuratKeluarService suratKeluarService; // Injected by Spring
    @Resource
    private UploadDocumentService uploadDocumentService; // Injected by Spring
    @Resource
    private UserService userService; // Injected by Spring

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
       return "login_form";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) throws ParseException {
        String role = "ROLE_USER";
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>)    SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (SimpleGrantedAuthority authority : authorities) {
            if(authority.getAuthority().equals("ROLE_ADMIN")){
                role = authority.getAuthority();
                break;
            }
        }

        request.getSession().setAttribute("role",role);

        indexObject(model);

        return "index";
    }

    private void indexObject(Model model) throws ParseException {
        Long totalSuratMasuk = suratMasukService.count();
        Long totalSuratKeluar = suratKeluarService.count();
        Long totalDocumentUpload = uploadDocumentService.count();
        Long totalUser = userService.count();

        model.addAttribute("totalSuratMasuk",totalSuratMasuk);
        model.addAttribute("totalSuratKeluar",totalSuratKeluar);
        model.addAttribute("totalDocumentUpload",totalDocumentUpload);
        model.addAttribute("totalUser",totalUser);

        List<Suratmasuk> suratMasukHariIni = suratMasukService.findAllByDate(getDateWithoutTimeUsingFormatSubstrac(), getDateWithoutTimeUsingFormat());
        model.addAttribute("suratMasukHariIniList", suratMasukHariIni);

        List<Suratkeluar> suratKeluarHariIni = suratKeluarService.findAllByDate(getDateWithoutTimeUsingFormatSubstrac(), getDateWithoutTimeUsingFormat());
        model.addAttribute("suratKeluarHariIniList", suratKeluarHariIni);
    }

    public static Date getDateWithoutTimeUsingFormat()throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(new Date()));
    }

    public static Date getDateWithoutTimeUsingFormatSubstrac()throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -7);
        return formatter.parse(formatter.format(cal.getTime()));
    }

    @RequestMapping(value = "/accessDenied")
    public String accessDenied(ModelMap model) {
        model.addAttribute("message","You don't have permission to access this page! Please contact administrator if you supposedly have the permission.");
        return "accessDenied";
    }
}
