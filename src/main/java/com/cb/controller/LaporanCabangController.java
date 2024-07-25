package com.cb.controller;

import com.cb.model.Cabang;
import com.cb.repository.CabangRepository;
import com.cb.repository.TransaksiCabangRepository;
import com.cb.repository.TransaksiOmzetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class LaporanCabangController {
    @Autowired
    private TransaksiCabangRepository transaksiCabangRepository;
    @Autowired
    private TransaksiOmzetRepository transaksiOmzetRepository;
    @Autowired
    private CabangRepository cabangRepository;

    @GetMapping("/transaksi/laporan-cabang/list")
    public ModelMap bahanBaku(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        ModelMap modelMap = new ModelMap();
        List<Cabang> cabangs = (List<Cabang>) cabangRepository.findAll();
        Collections.sort(cabangs,  new Comparator<Cabang>(){
            public int compare(Cabang o1, Cabang o2){
                return o1.getNamaCabang().compareTo(o2.getNamaCabang());
            }
        });

        modelMap.addAttribute("listCabang", cabangs);
        if (value != null) {
            model.addAttribute("key", value);
            modelMap.addAttribute("transaksiCabang", transaksiCabangRepository.findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(value, pageable));
            modelMap.addAttribute("transaksiOmzet", transaksiOmzetRepository.findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(value, pageable));
            return modelMap;
        } else {
            modelMap.addAttribute("transaksiCabang", transaksiCabangRepository.findAllByOrderByTglTransaksiDesc(pageable));
            modelMap.addAttribute("transaksiOmzet", transaksiOmzetRepository.findAllByOrderByTglTransaksiDesc(pageable));
            return modelMap;
        }
    }

}


