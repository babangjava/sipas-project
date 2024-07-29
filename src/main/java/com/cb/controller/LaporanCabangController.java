package com.cb.controller;

import com.cb.dto.LaporanCabang;
import com.cb.repository.dao.TransactionalBlock;
import com.cb.repository.daoImplementation.TransactionalBlockImpl;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Month;
import java.util.*;

@Controller
public class LaporanCabangController {
    @Autowired
    private TransactionalBlock transactionalBlock;
    @Autowired
    private TransactionalBlockImpl transactionalBlockImpl;

    @GetMapping("laporan-cabang/list")
    public ModelMap bahanBaku(@PageableDefault(size = 10) Pageable pageable, @RequestParam(name = "value", required = false) String value, Model model) {
        ModelMap modelMap = new ModelMap();
        Page<LaporanCabang> laporanCabangs = transactionalBlock.laporanKeuntunganHarian(pageable);
        modelMap.addAttribute("listCabang", laporanCabangs);
        return modelMap;
    }

    @GetMapping("laporan-cabang/bulan")
    public ModelMap laporanBulananShort(@PageableDefault(size = 10) Pageable pageable, Model model) {
        ModelMap modelMap = new ModelMap();
        Page<LaporanCabang> laporanCabangs = transactionalBlock.laporanKeuntunganBulananShort(pageable);
        modelMap.addAttribute("listCabang", laporanCabangs);
        return modelMap;
    }

    private static final Map<String, String> bulanMap = new HashMap<>();

    static {
        bulanMap.put("Januari", "JANUARY");
        bulanMap.put("Februari", "FEBRUARY");
        bulanMap.put("Maret", "MARCH");
        bulanMap.put("April", "APRIL");
        bulanMap.put("Mei", "MAY");
        bulanMap.put("Juni", "JUNE");
        bulanMap.put("Juli", "JULY");
        bulanMap.put("Agustus", "AUGUST");
        bulanMap.put("September", "SEPTEMBER");
        bulanMap.put("Oktober", "OCTOBER");
        bulanMap.put("November", "NOVEMBER");
        bulanMap.put("Desember", "DECEMBER");
    }

    @GetMapping("/bulanan/{bulan}")
    public String viewBulanan(@PathVariable("bulan") String bulan,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              ModelMap modelMap) {
        int bulanNumber;
        try {
            String bulanInggris = bulanMap.getOrDefault(bulan, null);
            if (bulanInggris == null) {
                throw new IllegalArgumentException("Invalid month: " + bulan);
            }
            bulanNumber = Month.valueOf(bulanInggris).getValue();
        } catch (IllegalArgumentException e) {
            modelMap.addAttribute("errorMessage", "Invalid month: " + bulan);
            return "laporan-cabang/bulanan";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<LaporanCabang> laporanDetails = transactionalBlock.laporanKeuntunganBulanan(bulanNumber, pageable);

        modelMap.addAttribute("listCabang", laporanDetails);

        return "laporan-cabang/bulanan";
    }

    @GetMapping("/downloadBulanan")
    public ResponseEntity<byte[]> downloadBulanan(@RequestParam("bulan") String bulan) {
        int bulanNumber;
        try {
            String bulanInggris = bulanMap.getOrDefault(bulan, null);
            if (bulanInggris == null) {
                throw new IllegalArgumentException("Invalid month: " + bulan);
            }
            bulanNumber = Month.valueOf(bulanInggris).getValue();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        Page<LaporanCabang> laporanDetails = transactionalBlock.laporanKeuntunganBulanan(bulanNumber, PageRequest.of(0, Integer.MAX_VALUE));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Keuntungan Bulanan");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("No");
            header.createCell(1).setCellValue("Nama Bulan");
            header.createCell(2).setCellValue("Total Bahan Baku");
            header.createCell(3).setCellValue("Total Omzet");
            header.createCell(4).setCellValue("Total Pengeluaran");
            header.createCell(5).setCellValue("Keuntungan");

            int rowNum = 1;
            for (LaporanCabang laporan : laporanDetails) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(laporan.getBulan());
                row.createCell(2).setCellValue(laporan.getTotalBahanBaku());
                row.createCell(3).setCellValue(laporan.getTotalOmzet());
                row.createCell(4).setCellValue(laporan.getTotalPengeluaran());
                row.createCell(5).setCellValue(laporan.getKeuntungan());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] bytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=KeuntunganBulanan.xlsx");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }
}


