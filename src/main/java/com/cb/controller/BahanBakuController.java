package com.cb.controller;

import com.cb.model.BahanBaku;
import com.cb.repository.BahanBakuRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class BahanBakuController {
    @Autowired
    private BahanBakuRepository bakuRepository;

    @GetMapping("/bahan-baku/list")
    public ModelMap bahanBaku(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model){
        if (value != null) {
            model.addAttribute("key", value);
            return new ModelMap().addAttribute("bahanBaku", bakuRepository.findByNamaBahanContainingIgnoreCase(value, pageable));
        } else {
            return new ModelMap().addAttribute("bahanBaku", bakuRepository.findAll(pageable));
        }
    }

    @GetMapping("/bahan-baku/form")
    public ModelMap tampilkanForm(@RequestParam(value = "id", required = false) BahanBaku bahanBaku ) {
        if (bahanBaku == null) {
            bahanBaku = new BahanBaku();
        }
        return new ModelMap("bahanBaku", bahanBaku);
    }
    @PostMapping("/bahan-baku/form")
    public String simpan(@Valid @ModelAttribute("bahanBaku") BahanBaku bahanBaku , BindingResult errors, SessionStatus status) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> System.out.println(error.toString()));
            return "bahan-baku/form";
        }
        Optional<BahanBaku> byNamaBahan = bakuRepository.findByNamaBahan(bahanBaku.getNamaBahan());
        if(byNamaBahan.isPresent()){
            errors.rejectValue("namaBahan", null, "Bahan baku sudah ada");
            return "bahan-baku/form";
        }
        System.out.println("Sebelum save: " + bahanBaku);
        bakuRepository.save(bahanBaku);
        System.out.println("Setelah save: " + bahanBaku);
        status.setComplete();
        return "redirect:/bahan-baku/list";
    }


    @GetMapping("/bahan-baku/delete")
    public ModelMap deleteConfirm(@RequestParam(value = "id", required = true) BahanBaku bahanBaku ) {
        return new ModelMap("bahanBaku", bahanBaku);
    }
    @PostMapping("/bahan-baku/delete")
    public Object delete(@ModelAttribute BahanBaku bahanBaku , SessionStatus status) {
        try{
            bakuRepository.delete(bahanBaku);
        } catch (DataIntegrityViolationException exception) {
            status.setComplete();
            return new ModelAndView("error/errorHapus")
                    .addObject("entityId", bahanBaku.getNamaBahan())
                    .addObject("entityName", "bahanBaku")
                    .addObject("errorCause", exception.getRootCause().getMessage())
                    .addObject("backLink","/bahan-baku/list");
        }
        status.setComplete();
        return "redirect:/bahan-baku/list";
    }

    @GetMapping("api/bahan-baku")
    public ResponseEntity<List<BahanBaku>> getAllBahanBaku() {
        Iterable<BahanBaku> bahanBakusIterable = bakuRepository.findAll();
        List<BahanBaku> bahanBakus = StreamSupport.stream(bahanBakusIterable.spliterator(), false)
                .collect(Collectors.toList());
        if (bahanBakus.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bahanBakus);
    }
}


