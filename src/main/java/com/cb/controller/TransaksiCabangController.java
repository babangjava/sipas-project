package com.cb.controller;

import com.cb.model.BahanBaku;
import com.cb.model.Gudang;
import com.cb.model.TransaksiBahanBakuCabang;
import com.cb.repository.*;
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

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TransaksiCabangController {
    @Autowired
    private TransaksiCabangRepository transaksiCabangRepository;
    @Autowired
    private CabangRepository cabangRepository;
    @Autowired
    private BahanBakuRepository bahanBakuRepository;
    @Autowired
    private GudangRepository gudangRepository;
    @Autowired
    private TransactionalBlock transactionalBlock;

    @GetMapping("/transaksi/cabang/list")
    public ModelMap cabang(Principal principal, @PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("transaksiCabang", transaksiCabangRepository.findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(value, pageable));
        } else {
            String namaCabangByEmail = getNamaCabangByEmail(principal);
            return new ModelMap().addAttribute("transaksiCabang", transaksiCabangRepository.findByNamaCabangContainingIgnoreCaseOrderByTglTransaksiDesc(namaCabangByEmail,pageable));
        }
    }

    @GetMapping("/transaksi/cabang/form")
    public ModelMap tampilkanForm(Principal principal, @RequestParam(value = "id", required = false) TransaksiBahanBakuCabang transaksiBahanBakuCabang , Model model) {
        if (transaksiBahanBakuCabang == null) {
            transaksiBahanBakuCabang = new TransaksiBahanBakuCabang();
            transaksiBahanBakuCabang.setTglTransaksi(LocalDate.now());
        }
        String namaCabangByEmail = getNamaCabangByEmail(principal);
        transaksiBahanBakuCabang.setNamaCabang(namaCabangByEmail);
        Iterable<BahanBaku> allBahanBaku = bahanBakuRepository.findAll();
        Iterable<Gudang> allGudang = gudangRepository.findAll();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("transaksiCabang", transaksiBahanBakuCabang);
        modelMap.addAttribute("namaCabang", namaCabangByEmail);
        model.addAttribute("bahanBakuList", allBahanBaku);
        model.addAttribute("gudangList", allGudang);
        return modelMap;
    }

    @PostMapping("/transaksi/cabang/form")
    public String simpanTransaksiCabang(@Valid @ModelAttribute("transaksiCabang") TransaksiBahanBakuCabang transaksiBahanBakuCabang, BindingResult errors, SessionStatus status) {
        System.out.println("Nama Bahan: " + transaksiBahanBakuCabang.getNamaBahan());
        System.out.println("Qty: " + transaksiBahanBakuCabang.getQty());
        System.out.println("Type: " + transaksiBahanBakuCabang.getType());
        System.out.println("Harga: " + transaksiBahanBakuCabang.getHarga());
        System.out.println("Total: " + transaksiBahanBakuCabang.getTotal());

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "transaksi/cabang/form";
        }
        try {
            transaksiBahanBakuCabang.setTotal(transaksiBahanBakuCabang.getHarga() * transaksiBahanBakuCabang.getQty());
            transaksiCabangRepository.save(transaksiBahanBakuCabang);
            status.setComplete();
        } catch (Exception e) {
            e.printStackTrace();
            errors.reject("simpanGagal", "Gagal menyimpan data");
            return "transaksi/cabang/form";
        }
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

    @GetMapping("/api/bahan-baku/{namaBahan}")
    @ResponseBody
    public Optional<BahanBaku> getBahanBakuByNama(@PathVariable String namaBahan) {
        return bahanBakuRepository.findByNamaBahan(namaBahan);
    }

    private String getNamaCabangByEmail(Principal principal) {
        String email = principal.getName();
        String namaCabang  = cabangRepository.findByEmail(email).getNamaCabang();
        return namaCabang;
    }

    // Menambahkan metode baru untuk memfilter transaksiBahanBakuCabangList
    @GetMapping("/transaksi/cabang/list/filtered")
    public String showFilteredTransaksiBahanBakuCabang(Model model) {
        // Mengambil semua transaksi dan mengubahnya menjadi List
        List<TransaksiBahanBakuCabang> transaksiBahanBakuCabangList = StreamSupport
                .stream(transaksiCabangRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        // Filter transaksi yang memiliki qty lebih dari 0
        List<TransaksiBahanBakuCabang> filteredTransaksi = transaksiBahanBakuCabangList.stream()
                .filter(transaksi -> transaksi.getQty() != null && transaksi.getQty() > 0)
                .collect(Collectors.toList());

        // Menambahkan daftar transaksi yang sudah difilter ke model
        model.addAttribute("transaksiBahanBakuCabangList", filteredTransaksi);

        return "transaksiBahanBakuCabang";
    }
}


