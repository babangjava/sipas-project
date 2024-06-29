package org.demo.web.controller;

import org.demo.bean.jpa.DocumentEntity;
import org.demo.bean.jpa.Suratmasuk;
import org.demo.business.service.DocumentService;
import org.demo.business.service.MenuItemService;
import org.demo.business.service.SuratMasukService;
import org.demo.web.common.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/suratmasuk")
public class SuratMasukController extends AbstractController {

	//--- Variables names ( to be used in JSP with Expression Language )
	private static final String MAIN_ENTITY_NAME = "suratmasuk";
	private static final String MAIN_LIST_NAME   = "list";

	//--- JSP pages names ( View name in the MVC model )
	private static final String JSP_FORM   = "suratmasuk/form";
	private static final String JSP_VIEW   = "suratmasuk/view";
	private static final String JSP_LIST   = "suratmasuk/list";

	//--- SAVE ACTION ( in the HTML form )
	private static final String SAVE_ACTION_CREATE   = "/suratmasuk/create";
	private static final String SAVE_ACTION_UPDATE   = "/suratmasuk/update";

	//--- Main entity service
	@Resource
    private SuratMasukService service; // Injected by Spring
	@Resource
	private MenuItemService menuItemService; // Injected by Spring
	@Resource
	private DocumentService documentService; // Injected by Spring

	public SuratMasukController() {
		super(SuratMasukController.class, MAIN_ENTITY_NAME );
		log("SuratMasukController created.");
	}

	private void populateModel(Model model, Suratmasuk suratMasuk, FormMode formMode) {
		//--- Main entity
		model.addAttribute(MAIN_ENTITY_NAME, suratMasuk);
		model.addAttribute("menuitem", menuItemService.findByHeader("Devisi"));
		if ( formMode == FormMode.CREATE ) {
			model.addAttribute(MODE, MODE_CREATE); // The form is in "create" mode
			model.addAttribute(SAVE_ACTION, SAVE_ACTION_CREATE); 			
			//--- Other data useful in this screen in "create" mode (all fields)
		}
		else if ( formMode == FormMode.UPDATE ) {
			model.addAttribute(MODE, MODE_UPDATE); // The form is in "update" mode
			model.addAttribute(SAVE_ACTION, SAVE_ACTION_UPDATE); 			
			//--- Other data useful in this screen in "update" mode (only non-pk fields)
		}
	}

	@RequestMapping()
	public String list(Model model) {
		log("Action 'list'");
		List<Suratmasuk> list = new ArrayList<>();
		model.addAttribute(MAIN_LIST_NAME, list);		
		return JSP_LIST;
	}

	@RequestMapping("/form")
	public String formForCreate(Model model) {
		log("Action 'formForCreate'");
		//--- Populates the model with a new instance
		Suratmasuk suratMasuk = new Suratmasuk();
		populateModel( model, suratMasuk, FormMode.CREATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/form/{idMenu}")
	public String formForUpdate(Model model, @PathVariable("idMenu") Integer idMenu ) {
		log("Action 'formForUpdate'");
		//--- Search the entity by its primary key and stores it in the model 
		Suratmasuk suratMasuk = service.findById(idMenu);
		populateModel( model, suratMasuk, FormMode.UPDATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/view/{idMenu}")
	public String view(Model model, @PathVariable("idMenu") Integer idMenu ) {
		log("Action 'view'");
		Suratmasuk suratMasuk = service.findById(idMenu);
		populateModel(model, suratMasuk, FormMode.UPDATE);
		if(suratMasuk.getDocumentId()!=null){
			DocumentEntity id = documentService.findById(suratMasuk.getDocumentId());
			model.addAttribute("fileName",id.getName());
		}
		return JSP_VIEW;
	}

	@RequestMapping(value = "/create" ) // GET or POST
	public String create(@Valid Suratmasuk suratMasuk, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'create'");
		try {
			if (!bindingResult.hasErrors()) {
				Suratmasuk created = null;
				if(suratMasuk.getFileUpload().isEmpty()){
					bindingResult.rejectValue("fileUpload", "global.error.document.required");
					populateModel( model, suratMasuk, FormMode.CREATE);
					return JSP_FORM;
				}else{
					created = service.saveWithDocument(suratMasuk);
				}
				model.addAttribute(MAIN_ENTITY_NAME, created);

				//---
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				return redirectToForm(httpServletRequest, suratMasuk.getId() );
			} else {
				populateModel( model, suratMasuk, FormMode.CREATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			log("Action 'create' : Exception - " + e.getMessage() );
			messageHelper.addException(model, "global.error.create", e);
			populateModel( model, suratMasuk, FormMode.CREATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/update" ) // GET or POST
	public String update(@Valid Suratmasuk suratMasuk, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'update'");
		try {
			if (!bindingResult.hasErrors()) {
				//--- Perform database operations
				Suratmasuk saved ;
				if(suratMasuk.getFileUpload().isEmpty()){
					saved = service.save(suratMasuk);
				}else{
					saved = service.saveWithDocument(suratMasuk);
				}
				model.addAttribute(MAIN_ENTITY_NAME, saved);
				//--- Set the result message
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				log("Action 'update' : update done - redirect");
				return redirectToForm(httpServletRequest, suratMasuk.getId());
			} else {
				log("Action 'update' : binding errors");
				populateModel( model, suratMasuk, FormMode.UPDATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			messageHelper.addException(model, "global.error.update", e);
			log("Action 'update' : Exception - " + e.getMessage() );
			populateModel( model, suratMasuk, FormMode.UPDATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/delete/{idMenu}") // GET or POST
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("idMenu") Integer idMenu) {
		log("Action 'delete'" );
		try {
			service.delete( idMenu );
			//--- Set the result message
			messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"delete.ok"));	
		} catch(Exception e) {
			messageHelper.addException(redirectAttributes, "global.error.delete", e);
		}
		return redirectToList();
	}

	@RequestMapping(value = "/download/{id}") // GET or POST
	public void download(HttpServletResponse response, @PathVariable("id") Integer id) throws IOException {
		DocumentEntity entity = documentService.findById(id);
		response.setHeader("Content-Disposition", "inline; filename=" + entity.getName() + "");
		response.setContentType(entity.getType());

		OutputStream outStream = response.getOutputStream();
		outStream.write(entity.getData());
		outStream.flush();
		outStream.close();
	}

	@RequestMapping(value = "/data-table",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String,Object> list(@RequestBody AdvanceSearch params) {
		return service.findAll(params);
	}

}
