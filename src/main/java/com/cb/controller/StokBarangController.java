package com.cb.controller;

import com.cb.model.BahanBaku;
import com.cb.model.Gudang;
import com.cb.model.StokBarang;
import com.cb.repository.BahanBakuRepository;
import com.cb.repository.GudangRepository;
import com.cb.repository.StokBarangRepository;
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

    @GetMapping("/stok-barang/list")
    public ModelMap stokBarang(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("stokBarang", stokBarangRepository.findByNamaGudangContainingIgnoreCase(value, pageable));
        } else {
            return new ModelMap().addAttribute("stokBarang", stokBarangRepository.findByNamaGudangContainingIgnoreCase("",pageable));
        }
    }

    @GetMapping("/stok-barang/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) StokBarang stokBarang ) {
        if (stokBarang == null) {
            stokBarang = new StokBarang();
            stokBarang.setTglTransaksi(LocalDate.now());
        }
        Iterable<Gudang> listGudang = gudangRepository.findAll();
        Iterable<BahanBaku> listBahan = bahanBakuRepository.findAll();
        ModelMap  modelMap = new ModelMap();
        modelMap.addAttribute("listGudang", listGudang);
        modelMap.addAttribute("listBahan", listBahan);
        modelMap.addAttribute("stokBarang", stokBarang);
        return modelMap;
    }
    @PostMapping("/stok-barang/form")
    public String simpan(@Valid @ModelAttribute("stokBarang") StokBarang stokBarang , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            return "stok-barang/form";
        }
        List<StokBarang> findTransBefore = stokBarangRepository.findByNamaGudangAndNamaBahanContainingIgnoreCaseOrderByTglTransaksiAscIdDescIdAsc(stokBarang.getNamaGudang(),stokBarang.getNamaBahan());
        if(!findTransBefore.isEmpty()){
            Integer stok = findTransBefore.get(0).getStok();
            stokBarang.setStok(stok+stokBarang.getQty());
        }else{
            stokBarang.setStok(stokBarang.getQty());
        }
        stokBarangRepository.save(stokBarang);
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
