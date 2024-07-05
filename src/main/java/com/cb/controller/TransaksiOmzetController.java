package com.cb.controller;

import com.cb.model.OmzetCabang;
import com.cb.repository.CabangRepository;
import com.cb.repository.TransaksiOmzetRepository;
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
import java.time.LocalDate;

@Controller
public class TransaksiOmzetController {
    @Autowired
    private TransaksiOmzetRepository transaksiOmzetRepository;
    @Autowired
    private CabangRepository cabangRepository;

    @GetMapping("/transaksi/omzet/list")
    public ModelMap cabang(Principal principal, @PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("transaksiOmzet", transaksiOmzetRepository.findByNamaCabangContainingIgnoreCase(value, pageable));
        } else {
            String namaCabangByEmail = getNamaCabangByEmail(principal);
            return new ModelMap().addAttribute("transaksiOmzet", transaksiOmzetRepository.findByNamaCabangContainingIgnoreCase(namaCabangByEmail,pageable));
        }
    }

    @GetMapping("/transaksi/omzet/form")
    public ModelMap tampilkanForm(Principal principal, @RequestParam(value = "id", required = false) OmzetCabang omzetCabang ) {
        if (omzetCabang == null) {
            omzetCabang = new OmzetCabang();
            omzetCabang.setTglTransaksi(LocalDate.now());
        }
        String namaCabangByEmail = getNamaCabangByEmail(principal);
        omzetCabang.setNamaCabang(namaCabangByEmail);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("transaksiOmzet", omzetCabang);
        modelMap.addAttribute("namaCabang", namaCabangByEmail);
        return modelMap;
    }

    @PostMapping("/transaksi/omzet/form")
    public String simpan(@Valid @ModelAttribute("transaksiOmzet") OmzetCabang omzetCabang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "transaksi/omzet/form";
        }
        transaksiOmzetRepository.save(omzetCabang);
        status.setComplete();
        return "redirect:/transaksi/omzet/list";
    }

    @GetMapping("/transaksi/omzet/delete")
    public ModelMap deleteConfirm(Principal principal, @RequestParam(value = "id", required = true) OmzetCabang omzetCabang ) {
        return new ModelMap("transaksiOmzet", omzetCabang);
    }
    @PostMapping("/transaksi/omzet/delete")
    public Object delete(@ModelAttribute OmzetCabang cabang , SessionStatus status) {
        try{
            transaksiOmzetRepository.delete(cabang);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", cabang.getNamaCabang())
                    .addObject("entityName", "transaksiOmzet")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/transaksi/omzet/list");
        }
        status.setComplete();
        return "redirect:/transaksi/omzet/list";
    }

    private String getNamaCabangByEmail(Principal principal) {
        String email = principal.getName();
        String namaCabang  = cabangRepository.findByEmail(email).getNamaCabang();
        return namaCabang;
    }


}


