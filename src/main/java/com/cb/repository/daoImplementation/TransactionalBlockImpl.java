package com.cb.repository.daoImplementation;

import com.cb.dto.LaporanCabang;
import com.cb.model.*;
import com.cb.repository.BahanBakuRepository;
import com.cb.repository.BahanTerpakaiRepository;
import com.cb.repository.StokBarangRepository;
import com.cb.repository.TransaksiCabangRepository;
import com.cb.repository.dao.TransactionalBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = false)
public class TransactionalBlockImpl implements TransactionalBlock {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransaksiCabangRepository transaksiCabangRepository;
    @Autowired
    private StokBarangRepository stokBarangRepository;
    @Autowired
    private BahanTerpakaiRepository bahanTerpakaiRepository;

    @Override
    public TransaksiBahanBakuCabang saveTransactionBahanBaku(TransaksiBahanBakuCabang obj) {
        List<TransaksiBahanBakuCabang> transaksiBahanBakuCabangList = new ArrayList<>();
        for (BahanBaku item : obj.getBahanBakuList()) {
            if(item.getQty()!=0){
                TransaksiBahanBakuCabang transaksiBahanBakuCabang = new TransaksiBahanBakuCabang();
                transaksiBahanBakuCabang.setId(null);
                transaksiBahanBakuCabang.setTglTransaksi(obj.getTglTransaksi());
                transaksiBahanBakuCabang.setNamaGudang(obj.getNamaGudang());
                transaksiBahanBakuCabang.setNamaCabang(obj.getNamaCabang());
                // setup nbahan
                transaksiBahanBakuCabang.setNamaBahan(item.getNamaBahan());
                transaksiBahanBakuCabang.setType(item.getType());
                transaksiBahanBakuCabang.setHarga(item.getHarga());
                transaksiBahanBakuCabang.setQty(item.getQty());
                transaksiBahanBakuCabang.setTotal(item.getQty() * item.getHarga());
                // untuk mengurangi stok otomatis
                StokBarang stokBarang = stokBarangRepository.findByNamaGudangAndNamaBahanContainingIgnoreCase(obj.getNamaGudang(), item.getNamaBahan());
                stokBarang.setStok(stokBarang.getStok() - item.getQty());
                stokBarangRepository.save(stokBarang);

                transaksiBahanBakuCabangList.add(transaksiBahanBakuCabang);
            }
        }

        Iterable<TransaksiBahanBakuCabang> transaksiBahanBakuCabangSaved = transaksiCabangRepository.saveAll(transaksiBahanBakuCabangList);
        return obj;
    }

    @Override
    public Gudang saveTransactionBahanBaku(Gudang obj) {
        List<StokBarang> stokBarangList = new ArrayList<>();
        for (BahanBaku item : obj.getBahanBakuList()) {
            if (item.getQty() != 0) {
                StokBarang stokBarang = stokBarangRepository.findByNamaGudangAndNamaBahanContainingIgnoreCase(obj.getNamaGudang(), item.getNamaBahan());
                if (stokBarang == null) {
                    stokBarang = new StokBarang();
                    stokBarang.setId(null);
                }
                stokBarang.setTglTransaksi(LocalDate.now());
                stokBarang.setNamaGudang(obj.getNamaGudang());
                // setup nbahan
                stokBarang.setNamaBahan(item.getNamaBahan());
                stokBarang.setType(item.getType());
                stokBarang.setHarga(item.getHarga());
                stokBarang.setQty(item.getQty());

                Integer stokBarangTerakhir = stokBarangRepository.getStokBarangTerakhir(obj.getNamaGudang(), item.getNamaBahan());
                if (stokBarangTerakhir == null) {
                    stokBarangTerakhir = 0;
                }
                stokBarang.setStok(item.getQty() + stokBarangTerakhir);

                stokBarangList.add(stokBarang);
            }
        }
        stokBarangRepository.saveAll(stokBarangList);
        return obj;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LaporanCabang> laporanKeuntunganHarian(Pageable pageable) {
        LocalDate localDate = LocalDate.now(); // For reference
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = localDate.format(formatter);

        String sql = "SELECT nama_cabang, tgl_transaksi FROM transaksi_bahan_baku_cabang WHERE CAST(tgl_transaksi AS DATE)='" + formattedString + "' GROUP BY nama_cabang, tgl_transaksi ORDER BY nama_cabang ASC";

        List<LaporanCabang> laporanCabangHeaders = jdbcTemplate.query(sql, (rs, rowNum) -> new LaporanCabang(rs.getString("nama_cabang"), rs.getDate("tgl_transaksi").toLocalDate()));
        for (LaporanCabang item : laporanCabangHeaders) {
            Double totalTransaksiBahanBaku = jdbcTemplate.queryForObject("SELECT SUM(total) FROM transaksi_bahan_baku_cabang where nama_cabang='" + item.getNamaCabang() + "' AND CAST(tgl_transaksi AS DATE)='" + formattedString + "'", Double.class);
            if (totalTransaksiBahanBaku == null) {
                totalTransaksiBahanBaku = 0.0;
            }
            Double totalTransaksiOmzet = jdbcTemplate.queryForObject("SELECT SUM(omzet) FROM omzet_cabang where nama_cabang='" + item.getNamaCabang() + "' AND CAST(tgl_transaksi AS DATE)='" + formattedString + "'", Double.class);
            if (totalTransaksiOmzet == null) {
                totalTransaksiOmzet = 0.0;
            }
            Double totalTransaksiPengeluaran = jdbcTemplate.queryForObject("SELECT SUM(harga) FROM pengeluaran_cabang where nama_cabang='" + item.getNamaCabang() + "' AND CAST(tgl_transaksi AS DATE)='" + formattedString + "'", Double.class);
            if (totalTransaksiPengeluaran == null) {
                totalTransaksiPengeluaran = 0.0;
            }
            item.setTotalPengeluaran(totalTransaksiPengeluaran);
            item.setTotalOmzet(totalTransaksiOmzet);
            item.setTotalBahanBaku(totalTransaksiBahanBaku);

            Double keuntungan = 0.0;
            if (totalTransaksiOmzet != 0.0) {
                keuntungan = item.getTotalOmzet() - item.getTotalBahanBaku();
            }
            item.setKeuntungan(keuntungan);
        }
        return new PageImpl<>(laporanCabangHeaders, pageable, laporanCabangHeaders.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LaporanCabang> laporanKeuntunganBulanan(String bulan, Pageable pageable) {
       String sql = "SELECT nama_cabang, tgl_transaksi FROM transaksi_bahan_baku_cabang WHERE TO_CHAR(tgl_transaksi, 'YYYY-MM')='"+bulan+"' GROUP BY nama_cabang, tgl_transaksi ORDER BY tgl_transaksi DESC, nama_cabang ASC";

        List<LaporanCabang> laporanCabangHeaders = jdbcTemplate.query(sql, (rs, rowNum) -> new LaporanCabang(rs.getString("nama_cabang"), rs.getDate("tgl_transaksi").toLocalDate()));
        for (LaporanCabang item : laporanCabangHeaders) {
            Double totalTransaksiBahanBaku = jdbcTemplate.queryForObject("SELECT SUM(total) FROM transaksi_bahan_baku_cabang where nama_cabang='" + item.getNamaCabang() + "' AND CAST(tgl_transaksi AS DATE)='" + item.getTglTransaksi() + "'", Double.class);
            if (totalTransaksiBahanBaku == null) {
                totalTransaksiBahanBaku = 0.0;
            }
            Double totalTransaksiOmzet = jdbcTemplate.queryForObject("SELECT SUM(omzet) FROM omzet_cabang where nama_cabang='" + item.getNamaCabang() + "' AND CAST(tgl_transaksi AS DATE)='" + item.getTglTransaksi() + "'", Double.class);
            if (totalTransaksiOmzet == null) {
                totalTransaksiOmzet = 0.0;
            }
            Double totalTransaksiPengeluaran = jdbcTemplate.queryForObject("SELECT SUM(harga) FROM pengeluaran_cabang where nama_cabang='" + item.getNamaCabang() + "' AND CAST(tgl_transaksi AS DATE)='" + item.getTglTransaksi() + "'", Double.class);
            if (totalTransaksiPengeluaran == null) {
                totalTransaksiPengeluaran = 0.0;
            }
            item.setTotalPengeluaran(totalTransaksiPengeluaran);
            item.setTotalOmzet(totalTransaksiOmzet);
            item.setTotalBahanBaku(totalTransaksiBahanBaku);

            Double keuntungan = 0.0;
            if (totalTransaksiOmzet != 0.0) {
                keuntungan = item.getTotalOmzet() - item.getTotalBahanBaku();
            }
            item.setKeuntungan(keuntungan);
        }
        return new PageImpl<>(laporanCabangHeaders, pageable, laporanCabangHeaders.size());
    }

    @Override
    public BahanBakuTerpakai saveTransactionBahanTerpakai(BahanBakuTerpakai obj) {
        List<BahanBakuTerpakai> bahanBakuTerpakaiList = new ArrayList<>();

        for (BahanBaku item : obj.getBahanBakuList()) {
            if (item.getQty() != 0) {
                BahanBakuTerpakai bahanBakuTerpakai = new BahanBakuTerpakai();
                bahanBakuTerpakai.setId(null);
                bahanBakuTerpakai.setTglTransaksi(obj.getTglTransaksi());
                bahanBakuTerpakai.setNamaGudang(obj.getNamaGudang());
                bahanBakuTerpakai.setNamaCabang(obj.getNamaCabang());
                bahanBakuTerpakai.setNamaBahan(item.getNamaBahan());
                bahanBakuTerpakai.setType(item.getType());
                bahanBakuTerpakai.setHarga(item.getHarga());
                bahanBakuTerpakai.setQty(item.getQty());
                bahanBakuTerpakai.setTotal(item.getQty() * item.getHarga());

                List<TransaksiBahanBakuCabang> matchingTransactions = transaksiCabangRepository.findByNamaGudangAndNamaBahan(obj.getNamaGudang(), item.getNamaBahan());

                for (TransaksiBahanBakuCabang transaksiBahanBakuCabang : matchingTransactions) {
                    int updatedQty = transaksiBahanBakuCabang.getQty() - item.getQty();
                    transaksiBahanBakuCabang.setQty(updatedQty);

                    transaksiCabangRepository.save(transaksiBahanBakuCabang);
                }

                bahanBakuTerpakaiList.add(bahanBakuTerpakai);
            }
        }

        // Save all the BahanBakuTerpakai objects to the repository
        bahanTerpakaiRepository.saveAll(bahanBakuTerpakaiList);

        return obj;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<String> laporanKeuntunganBulananShort(Pageable pageable) {
        String sql = "SELECT TO_CHAR(tgl_transaksi, 'YYYY-MM') AS bulan FROM transaksi_bahan_baku_cabang GROUP BY bulan ORDER BY bulan DESC";
        List<String> bulanList = (List<String>) jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("bulan"));
        return new PageImpl<>(bulanList, pageable, bulanList.size());
    }



}
