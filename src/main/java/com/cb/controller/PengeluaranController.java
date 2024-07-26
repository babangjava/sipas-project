package com.cb.controller;

import com.cb.model.PengeluaranCabang;
import com.cb.repository.CabangRepository;
import com.cb.repository.PengeluaranRepository;
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
public class PengeluaranController {
    @Autowired
    private PengeluaranRepository pengeluaranRepository;
    @Autowired
    private CabangRepository cabangRepository;

    @GetMapping("/transaksi/pengeluaran/list")
    public ModelMap cabang(Principal principal, @PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("pengeluaran", pengeluaranRepository.findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(value, pageable));
        } else {
            String namaCabangByEmail = getNamaCabangByEmail(principal);
            return new ModelMap().addAttribute("pengeluaran", pengeluaranRepository.findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(namaCabangByEmail,pageable));
        }
    }

    @GetMapping("/transaksi/pengeluaran/form")
    public ModelMap tampilkanForm(Principal principal, @RequestParam(value = "id", required = false) PengeluaranCabang pengeluaranCabang ) {
        if (pengeluaranCabang == null) {
            pengeluaranCabang = new PengeluaranCabang();
            pengeluaranCabang.setTglTransaksi(LocalDate.now());
        }
        String namaCabangByEmail = getNamaCabangByEmail(principal);
        pengeluaranCabang.setNamaCabang(namaCabangByEmail);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("pengeluaran", pengeluaranCabang);
        modelMap.addAttribute("namaCabang", namaCabangByEmail);
        return modelMap;
    }

    @PostMapping("/transaksi/pengeluaran/form")
    public String simpan(@Valid @ModelAttribute("pengeluaran") PengeluaranCabang pengeluaranCabang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "transaksi/pengeluaran/form";
        }
        pengeluaranRepository.save(pengeluaranCabang);
        status.setComplete();
        return "redirect:/transaksi/pengeluaran/list";
    }

    @GetMapping("/transaksi/pengeluaran/delete")
    public ModelMap deleteConfirm(Principal principal, @RequestParam(value = "id", required = true) PengeluaranCabang pengeluaranCabang ) {
        return new ModelMap("pengeluaran", pengeluaranCabang);
    }
    @PostMapping("/transaksi/pengeluaran/delete")
    public Object delete(@ModelAttribute PengeluaranCabang cabang , SessionStatus status) {
        try{
            pengeluaranRepository.delete(cabang);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", cabang.getNamaCabang())
                    .addObject("entityName", "pengeluaran")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/transaksi/pengeluaran/list");
        }
        status.setComplete();
        return "redirect:/transaksi/pengeluaran/list";
    }

    private String getNamaCabangByEmail(Principal principal) {
        String email = principal.getName();
        String namaCabang  = cabangRepository.findByEmail(email).getNamaCabang();
        return namaCabang;
    }


}


