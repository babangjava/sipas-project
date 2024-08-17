package com.cb.controller;

import com.cb.model.BahanBaku;
import com.cb.model.BahanBakuTerpakai;
import com.cb.model.Gudang;
import com.cb.repository.BahanBakuRepository;
import com.cb.repository.BahanTerpakaiRepository;
import com.cb.repository.CabangRepository;
import com.cb.repository.GudangRepository;
import com.cb.repository.dao.TransactionalBlock;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BahanTerpakaiController {
    @Autowired
    private BahanTerpakaiRepository bahanTerpakaiRepository;
    @Autowired
    private CabangRepository cabangRepository;
    @Autowired
    private BahanBakuRepository bahanBakuRepository;
    @Autowired
    private GudangRepository gudangRepository;
    @Autowired
    private TransactionalBlock transactionalBlock;

    @GetMapping("/transaksi/bahan-terpakai/list")
    public ModelMap bahanTerpakai(Principal principal, @PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model) {
        String namaCabangByEmail = getNamaCabangByEmail(principal);
        LocalDate now = LocalDate.now();
        return new ModelMap().addAttribute("bahanTerpakai", bahanTerpakaiRepository.findByNamaCabangContainingIgnoreCaseAndTglTransaksiBetweenOrderByTglTransaksiDesc(namaCabangByEmail, now, now, pageable));
    }

    @GetMapping("/transaksi/bahan-terpakai/form")
    public ModelMap tampilkanForm(Principal principal, @RequestParam(value = "id", required = false) BahanBakuTerpakai bahanBakuTerpakai, Model model) {
        if (bahanBakuTerpakai == null) {
            bahanBakuTerpakai = new BahanBakuTerpakai();
            bahanBakuTerpakai.setTglTransaksi(LocalDate.now());
        }
        String namaCabangByEmail = getNamaCabangByEmail(principal);
        bahanBakuTerpakai.setNamaCabang(namaCabangByEmail);
        List<BahanBaku> allBahanBaku = (List<BahanBaku>) bahanBakuRepository.findAll();
        for (int i = 0; i < allBahanBaku.size(); i++) {
            allBahanBaku.get(i).setQty(0);
        }
        Iterable<Gudang> allGudang = gudangRepository.findAll();
        bahanBakuTerpakai.setBahanBakuList(allBahanBaku);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("bahanTerpakai", bahanBakuTerpakai);
        modelMap.addAttribute("namaCabang", namaCabangByEmail);
        model.addAttribute("gudangList", allGudang);
        return modelMap;
    }

    @PostMapping("/transaksi/bahan-terpakai/form")
    public String simpan(@Valid @ModelAttribute("bahanTerpakai") BahanBakuTerpakai bahanBakuTerpakai, BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "transaksi/bahan-terpakai/form";
        }
        transactionalBlock.saveTransactionBahanTerpakai(bahanBakuTerpakai);
        status.setComplete();
        return "redirect:/transaksi/bahan-terpakai/list";
    }


    private String getNamaCabangByEmail(Principal principal) {
        String email = principal.getName();
        String namaCabang = cabangRepository.findByEmail(email).getNamaCabang();
        return namaCabang;
    }
}
