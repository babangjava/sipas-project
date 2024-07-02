package com.cb.controller;

import com.cb.model.Cabang;
import com.cb.model.User;
import com.cb.repository.CabangRepository;
import com.cb.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CabangController {
    @Autowired
    private CabangRepository cabangRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/cabang/list")
    public ModelMap cabang(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("cabang", cabangRepository.findByNamaCabangContainingIgnoreCase(value, pageable));
        } else {
            return new ModelMap().addAttribute("cabang", cabangRepository.findAll(pageable));
        }
    }

    @GetMapping("/cabang/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) Cabang cabang ) {
        if (cabang == null) {
            cabang = new Cabang();
        }
        List<User> allUser = userRepository.findAll();
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("cabang", cabang);
        modelMap.addAttribute("users", allUser);
        return modelMap;
    }
    @PostMapping("/cabang/form")
    public String simpan(@Valid @ModelAttribute("cabang") Cabang cabang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "cabang/form";
        }
        cabangRepository.save(cabang);
        status.setComplete();
        return "redirect:/cabang/list";
    }

    @GetMapping("/cabang/delete")
    public ModelMap deleteConfirm(@RequestParam(value = "id", required = true) Cabang cabang ) {
        return new ModelMap("cabang", cabang);
    }
    @PostMapping("/cabang/delete")
    public Object delete(@ModelAttribute Cabang cabang , SessionStatus status) {
        try{
            cabangRepository.delete(cabang);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", cabang.getNamaCabang())
                    .addObject("entityName", "cabang")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/cabang/list");
        }
        status.setComplete();
        return "redirect:/cabang/list";
    }
}


