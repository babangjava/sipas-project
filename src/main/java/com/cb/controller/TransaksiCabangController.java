package com.cb.controller;

import com.cb.model.TransaksiBahanBakuCabang;
import com.cb.repository.CabangRepository;
import com.cb.repository.TransaksiCabangRepository;
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

import java.security.Principal;

@Controller
public class TransaksiCabangController {
    @Autowired
    private TransaksiCabangRepository transaksiCabangRepository;
    @Autowired
    private CabangRepository cabangRepository;

    @GetMapping("/transaksi/cabang/list")
    public ModelMap cabang(Principal principal, @PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("transaksiCabang", transaksiCabangRepository.findByNamaCabangContainingIgnoreCase(value, pageable));
        } else {
            String namaCabangByEmail = getNamaCabangByEmail(principal);
            return new ModelMap().addAttribute("transaksiCabang", transaksiCabangRepository.findByNamaCabangContainingIgnoreCase(namaCabangByEmail,pageable));
        }
    }

    @GetMapping("/transaksi/cabang/form")
    public ModelMap tampilkanForm(Principal principal, @RequestParam(value = "id", required = false) TransaksiBahanBakuCabang transaksiBahanBakuCabang ) {
        if (transaksiBahanBakuCabang == null) {
            transaksiBahanBakuCabang = new TransaksiBahanBakuCabang();
        }
        String namaCabangByEmail = getNamaCabangByEmail(principal);

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("transaksiCabang", transaksiBahanBakuCabang);
        modelMap.addAttribute("namaCabang", namaCabangByEmail);
        return modelMap;
    }

    @PostMapping("/transaksi/cabang/form")
    public String simpan(@Valid @ModelAttribute("transaksiCabang") TransaksiBahanBakuCabang transaksiBahanBakuCabang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "cabang/form";
        }
        transaksiCabangRepository.save(transaksiBahanBakuCabang);
        status.setComplete();
        return "redirect:/transaksi/cabang/list";
    }

    @GetMapping("/transaksi/cabang/delete")
    public ModelMap deleteConfirm(Principal principal, @RequestParam(value = "id", required = true) TransaksiBahanBakuCabang transaksiBahanBakuCabang ) {
        return new ModelMap("transaksiCabang", transaksiBahanBakuCabang);
    }
    @PostMapping("/transaksi/cabang/delete")
    public Object delete(@ModelAttribute TransaksiBahanBakuCabang transaksiBahanBakuCabang , SessionStatus status) {
        try{
            transaksiCabangRepository.delete(transaksiBahanBakuCabang);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", transaksiBahanBakuCabang.getNamaCabang())
                    .addObject("entityName", "transaksiCabang")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/transaksi/cabang/list");
        }
        status.setComplete();
        return "redirect:/transaksi/cabang/list";
    }

    private String getNamaCabangByEmail(Principal principal) {
        String email = principal.getName();
        String namaCabang  = cabangRepository.findByEmail(email).getNamaCabang();
        return namaCabang;
    }
}


