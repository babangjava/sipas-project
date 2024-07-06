package com.cb.controller;

import com.cb.model.Cabang;
import com.cb.model.Role;
import com.cb.model.User;
import com.cb.repository.CabangRepository;
import com.cb.repository.RoleRepository;
import com.cb.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/user/list")
    public ModelMap user(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("user", userRepository.findByEmail(value));
        } else {
            return new ModelMap().addAttribute("user", userRepository.findAll(pageable));
        }
    }

    @GetMapping("/user/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) User user ) {
        if (user == null) {
            user = new User();
        }
        return new ModelMap("user", user);
    }
    @PostMapping("/user/form")
    public String simpan(@Valid @ModelAttribute("user") User user , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "user/form";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(user.getRoleName());
        List<Role> roles =new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        status.setComplete();
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete")
    public ModelMap deleteConfirm(@RequestParam(value = "id", required = true) User user ) {
        return new ModelMap("user", user);
    }
    @PostMapping("/user/delete")
    public Object delete(@ModelAttribute User user , SessionStatus status) {
        try{
            userRepository.delete(user);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", user.getEmail())
                    .addObject("entityName", "user")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/user/list");
        }
        status.setComplete();
        return "redirect:/user/list";
    }
    // test branch development
}
