package com.cb.controller;

import com.cb.model.Gudang;
import com.cb.repository.GudangRepository;
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
public class GudangController {
    @Autowired
    private GudangRepository gudangRepository;

    @GetMapping("/gudang/list")
    public ModelMap gudang(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("gudang", gudangRepository.findByNamaGudangContainingIgnoreCase(value, pageable));
        } else {
            return new ModelMap().addAttribute("gudang", gudangRepository.findAll(pageable));
        }
    }

    @GetMapping("/gudang/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) Gudang gudang ) {
        if (gudang == null) {
            gudang = new Gudang();
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("gudang", gudang);
        return modelMap;
    }
    @PostMapping("/gudang/form")
    public String simpan(@Valid @ModelAttribute("gudang") Gudang gudang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "gudang/form";
        }
        gudangRepository.save(gudang);
        status.setComplete();
        return "redirect:/gudang/list";
    }

    @GetMapping("/gudang/delete")
    public ModelMap deleteConfirm(@RequestParam(value = "id", required = true) Gudang gudang ) {
        return new ModelMap("gudang", gudang);
    }
    @PostMapping("/gudang/delete")
    public Object delete(@ModelAttribute Gudang gudang , SessionStatus status) {
        try{
            gudangRepository.delete(gudang);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", gudang.getNamaGudang())
                    .addObject("entityName", "gudang")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/gudang/list");
        }
        status.setComplete();
        return "redirect:/gudang/list";
    }
}
