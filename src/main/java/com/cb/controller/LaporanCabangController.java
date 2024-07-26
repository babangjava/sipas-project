package com.cb.controller;

import com.cb.dto.LaporanCabang;
import com.cb.model.Cabang;
import com.cb.repository.CabangRepository;
import com.cb.repository.TransaksiCabangRepository;
import com.cb.repository.TransaksiOmzetRepository;
import com.cb.repository.dao.TransactionalBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private TransactionalBlock transactionalBlock;

    @GetMapping("laporan-cabang/list")
    public ModelMap bahanBaku(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model) {
        ModelMap modelMap = new ModelMap();
        Page<LaporanCabang> laporanCabangs = transactionalBlock.laporanKeuntunganHarian(pageable);
        modelMap.addAttribute("listCabang", laporanCabangs);
        return modelMap;
    }
}


