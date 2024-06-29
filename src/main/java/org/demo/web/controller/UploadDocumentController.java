package org.demo.web.controller;

import org.demo.bean.jpa.Menuitem;
import org.demo.bean.jpa.Uploaddocument;
import org.demo.business.service.MenuItemService;
import org.demo.business.service.UploadDocumentService;
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
@RequestMapping("/uploaddocument")
public class UploadDocumentController extends AbstractController {

	//--- Variables names ( to be used in JSP with Expression Language )
	private static final String MAIN_ENTITY_NAME = "uploaddocument";
	private static final String MAIN_LIST_NAME   = "list";

	//--- JSP pages names ( View name in the MVC model )
	private static final String JSP_FORM   = "uploaddocument/form";
	private static final String JSP_LIST   = "uploaddocument/list";

	//--- SAVE ACTION ( in the HTML form )
	private static final String SAVE_ACTION_CREATE   = "/uploaddocument/create";
	private static final String SAVE_ACTION_UPDATE   = "/uploaddocument/update";

	//--- Main entity service
	@Resource
    private UploadDocumentService service; // Injected by Spring
	@Resource
	private MenuItemService menuItemService;

	public UploadDocumentController() {
		super(UploadDocumentController.class, MAIN_ENTITY_NAME );
		log("MenuitemController created.");
	}

	private void populateModel(Model model, Uploaddocument uploaddocument, FormMode formMode) {
		//--- Main entity
		model.addAttribute(MAIN_ENTITY_NAME, uploaddocument);
		model.addAttribute("menuitem", menuItemService.findByHeader("UploadDocument"));
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
		List<Uploaddocument> list = new ArrayList<>();
		model.addAttribute(MAIN_LIST_NAME, list);		
		return JSP_LIST;
	}

	@RequestMapping("/form")
	public String formForCreate(Model model) {
		log("Action 'formForCreate'");
		//--- Populates the model with a new instance
		Uploaddocument uploaddocument = new Uploaddocument();
		populateModel( model, uploaddocument, FormMode.CREATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/form/{id}")
	public String formForUpdate(Model model, @PathVariable("id") Integer id ) {
		log("Action 'formForUpdate'");
		//--- Search the entity by its primary key and stores it in the model 
		Uploaddocument uploaddocument = service.findById(id);
		populateModel(model, uploaddocument, FormMode.UPDATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/create" ) // GET or POST
	public String create(@Valid Uploaddocument uploaddocument, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'create'");
		try {
			if (!bindingResult.hasErrors()) {
				Uploaddocument created;
				if(uploaddocument.getFileUpload().isEmpty()){
					bindingResult.rejectValue("fileUpload", "global.error.document.required");
					populateModel( model, uploaddocument, FormMode.CREATE);
					return JSP_FORM;
				}else{
					created = service.save(uploaddocument);
				}
				model.addAttribute(MAIN_ENTITY_NAME, created);

				//---
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				return redirectToForm(httpServletRequest, uploaddocument.getId() );
			} else {
				populateModel( model, uploaddocument, FormMode.CREATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			log("Action 'create' : Exception - " + e.getMessage() );
			messageHelper.addException(model, "global.error.create", e);
			populateModel( model, uploaddocument, FormMode.CREATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/update" ) // GET or POST
	public String update(@Valid Uploaddocument uploaddocument, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'update'");
		try {
			if (!bindingResult.hasErrors()) {
				//--- Perform database operations
				Uploaddocument byId = service.findById(uploaddocument.getId());
				uploaddocument.setData(byId.getData());

				Uploaddocument saved = service.save(uploaddocument);
				model.addAttribute(MAIN_ENTITY_NAME, saved);
				//--- Set the result message
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				log("Action 'update' : update done - redirect");
				return redirectToForm(httpServletRequest, uploaddocument.getId());
			} else {
				log("Action 'update' : binding errors");
				populateModel( model, uploaddocument, FormMode.UPDATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			messageHelper.addException(model, "global.error.update", e);
			log("Action 'update' : Exception - " + e.getMessage() );
			populateModel( model, uploaddocument, FormMode.UPDATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/delete/{id}") // GET or POST
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("id") Integer id) {
		log("Action 'delete'" );
		try {
			service.delete( id );
			//--- Set the result message
			messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"delete.ok"));	
		} catch(Exception e) {
			messageHelper.addException(redirectAttributes, "global.error.delete", e);
		}
		return redirectToList();
	}

	@RequestMapping(value = "/download/{id}") // GET or POST
	public void download(HttpServletResponse response, @PathVariable("id") Integer id) throws IOException {
		Uploaddocument entity = service.findById(id);
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

	@RequestMapping(value = "/list-onchange/{param}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Menuitem> listOnchange( @PathVariable("param") String param) {
		return menuItemService.findByHeader(param);
	}


}
