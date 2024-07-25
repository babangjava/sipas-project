package com.cb.controller;

import com.cb.model.BahanBaku;
import com.cb.model.Gudang;
import com.cb.model.StokBarang;
import com.cb.repository.BahanBakuRepository;
import com.cb.repository.GudangRepository;
import com.cb.repository.StokBarangRepository;
import com.cb.repository.dao.TransactionalBlock;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class StokBarangController {
    @Autowired
    private StokBarangRepository stokBarangRepository;
    @Autowired
    private GudangRepository gudangRepository;
    @Autowired
    private BahanBakuRepository bahanBakuRepository;
    @Autowired
    private TransactionalBlock transactionalBlock;

    @GetMapping("/stok-barang/list")
    public ModelMap stokBarang(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("stokBarang", gudangRepository.findByNamaGudangContainingIgnoreCase(value, pageable));
        } else {
            return new ModelMap().addAttribute("stokBarang", gudangRepository.findByNamaGudangContainingIgnoreCase("",pageable));
        }
    }

    @GetMapping("/stok-barang/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) Gudang gudang ) {
        if (gudang == null) {
            gudang = new Gudang();
        }
        ModelMap  modelMap = new ModelMap();
        List<BahanBaku> allBahanBaku = (List<BahanBaku>) bahanBakuRepository.findAll();
        for (BahanBaku item : allBahanBaku) {
            item.setQty(0);
        }
        gudang.setBahanBakuList(allBahanBaku);
        modelMap.addAttribute("stokBarang", gudang);
        return modelMap;
    }
    @PostMapping("/stok-barang/form")
    public String simpan(@Valid @ModelAttribute("stokBarang") Gudang gudang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "stok-barang/form";
        }
        transactionalBlock.saveTransactionBahanBaku(gudang);
        status.setComplete();
        return "redirect:/stok-barang/list";
    }

    @GetMapping("/stok-barang/delete")
    public ModelMap deleteConfirm(@RequestParam(value = "id", required = true) StokBarang stokBarang ) {
        return new ModelMap("stokBarang", stokBarang);
    }
    @PostMapping("/stok-barang/delete")
    public Object delete(@ModelAttribute StokBarang stokBarang , SessionStatus status) {
        try{
            stokBarangRepository.delete(stokBarang);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", stokBarang.getNamaBahan())
                    .addObject("entityName", "stokBarang")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/stok-barang/list");
        }
        status.setComplete();
        return "redirect:/stok-barang/list";
    }

    @GetMapping("/data/bahan-baku/{namaBahan}")
    @ResponseBody
    public Optional<BahanBaku> getBahanBakuNama(@PathVariable String namaBahan) {
        return bahanBakuRepository.findByNamaBahan(namaBahan);
    }

}
