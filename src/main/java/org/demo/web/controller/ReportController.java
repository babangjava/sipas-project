package org.demo.web.controller;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.demo.bean.jpa.Suratkeluar;
import org.demo.bean.jpa.Suratmasuk;
import org.demo.business.service.DocumentService;
import org.demo.business.service.SuratKeluarService;
import org.demo.business.service.SuratMasukService;
import org.demo.web.common.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController extends AbstractController {

    private static final String MAIN_ENTITY_NAME = "reportModel";

    private static final String JSP_LIST_SURAT_MASUK = "report/suratmasuk/list";
    private static final String JSP_LIST_SURAT_KELUAR = "report/suratkeluar/list";

    @Resource
    private SuratMasukService suratMasukService; // Injected by Spring
    @Resource
    private SuratKeluarService suratKeluarService; // Injected by Spring
    @Resource
    private DocumentService documentService; // Injected by Spring

    public ReportController() {
        super(ReportController.class, MAIN_ENTITY_NAME);
        log("ReportController created.");
    }

    private void populateModelSuratMasuk(Model model, ReportModel reportModel, List<Suratmasuk> list) {
        model.addAttribute("reportModel", reportModel);
        model.addAttribute("list", list);
    }

    private void populateModelSuratKeluar(Model model, ReportModel reportModel, List<Suratkeluar> list) {
        model.addAttribute("reportModel", reportModel);
        model.addAttribute("list", list);
    }

    @RequestMapping("/suratmasuk")
    public String suratmasuk(Model model) {
        log("Action 'suratmasuk'");
        populateModelSuratMasuk(model, new ReportModel(), new ArrayList<Suratmasuk>());
        return JSP_LIST_SURAT_MASUK;
    }

    @RequestMapping("/suratmasuk/submit")
    public String create(@Valid ReportModel reportModel, BindingResult bindingResult, Model model) {
        log("Action 'create'");
        try {
            if (!bindingResult.hasErrors()) {
                if (reportModel.getTanggalAwal().after(reportModel.getTanggalAkhir())) {
                    messageHelper.addMessage(model, new Message(MessageType.DANGER, "error.report.date"));
                    return JSP_LIST_SURAT_MASUK;
                }
                List<Suratmasuk> result = suratMasukService.findAllByDate(reportModel.getTanggalAwal(),reportModel.getTanggalAkhir());
                populateModelSuratMasuk(model, reportModel, result);
                return JSP_LIST_SURAT_MASUK;
            } else {
                return JSP_LIST_SURAT_MASUK;
            }
        } catch (Exception e) {
            log("Action 'submit' : Exception - " + e.getMessage());
            messageHelper.addException(model, "global.error.create", e);
            return JSP_LIST_SURAT_MASUK;
        }
    }

    @RequestMapping("/suratkeluar")
    public String suratkeluar(Model model) {
        log("Action 'suratkeluar'");
        populateModelSuratKeluar(model, new ReportModel(), new ArrayList<Suratkeluar>());
        return JSP_LIST_SURAT_KELUAR;
    }

    @RequestMapping("/suratkeluar/submit")
    public String suratkeluar(@Valid ReportModel reportModel, BindingResult bindingResult, Model model) {
        log("Action 'create'");
        try {
            if (!bindingResult.hasErrors()) {
                if (reportModel.getTanggalAwal().after(reportModel.getTanggalAkhir())) {
                    messageHelper.addMessage(model, new Message(MessageType.DANGER, "error.report.date"));
                    return JSP_LIST_SURAT_KELUAR;
                }
                List<Suratkeluar> result = suratKeluarService.findAllByDate(reportModel.getTanggalAwal(),reportModel.getTanggalAkhir());
                populateModelSuratKeluar(model, reportModel, result);
                return JSP_LIST_SURAT_KELUAR;
            } else {
                return JSP_LIST_SURAT_KELUAR;
            }
        } catch (Exception e) {
            log("Action 'submit' : Exception - " + e.getMessage());
            messageHelper.addException(model, "global.error.create", e);
            return JSP_LIST_SURAT_KELUAR;
        }
    }

    @RequestMapping(value = "/suratmasuk/pdf", method = RequestMethod.GET)
    public String getPdfReport(Model model, HttpServletResponse response) {
        List<Suratmasuk> entities = (List<Suratmasuk>) suratMasukService.findAll();
        JRDataSource dataSource = new JRBeanCollectionDataSource(entities);
        model.addAttribute("dataSource", dataSource);
        return "pdfReport";
    }

    @RequestMapping(value = "/suratmasuk/xls", method = RequestMethod.GET)
    public String getXlsReport(Model model, HttpServletResponse response) {
        List<Suratmasuk> entities = (List<Suratmasuk>) suratMasukService.findAll();
        JRDataSource dataSource = new JRBeanCollectionDataSource(entities);
        model.addAttribute("dataSource", dataSource);
        return "xlsReport";
    }

}
