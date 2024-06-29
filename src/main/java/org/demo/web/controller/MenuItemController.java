package org.demo.web.controller;

import org.demo.bean.jpa.Menuitem;
import org.demo.business.service.MenuItemService;
import org.demo.web.common.AbstractController;
import org.demo.web.common.FormMode;
import org.demo.web.common.Message;
import org.demo.web.common.MessageType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/menuitem")
public class MenuItemController extends AbstractController {

	//--- Variables names ( to be used in JSP with Expression Language )
	private static final String MAIN_ENTITY_NAME = "menuitem";
	private static final String MAIN_LIST_NAME   = "list";

	//--- JSP pages names ( View name in the MVC model )
	private static final String JSP_FORM   = "menuitem/form";
	private static final String JSP_LIST   = "menuitem/list";

	//--- SAVE ACTION ( in the HTML form )
	private static final String SAVE_ACTION_CREATE   = "/menuitem/create";
	private static final String SAVE_ACTION_UPDATE   = "/menuitem/update";

	//--- Main entity service
	@Resource
    private MenuItemService service; // Injected by Spring

	public MenuItemController() {
		super(MenuItemController.class, MAIN_ENTITY_NAME );
		log("MenuitemController created.");
	}

	private void populateModel(Model model, Menuitem menuItem, FormMode formMode) {
		//--- Main entity
		model.addAttribute(MAIN_ENTITY_NAME, menuItem);
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
		List<Menuitem> list = service.findAll();
		model.addAttribute(MAIN_LIST_NAME, list);		
		return JSP_LIST;
	}

	@RequestMapping("/form")
	public String formForCreate(Model model) {
		log("Action 'formForCreate'");
		//--- Populates the model with a new instance
		Menuitem menuItem = new Menuitem();
		populateModel( model, menuItem, FormMode.CREATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/form/{idMenu}")
	public String formForUpdate(Model model, @PathVariable("idMenu") Integer idMenu ) {
		log("Action 'formForUpdate'");
		//--- Search the entity by its primary key and stores it in the model 
		Menuitem menuItem = service.findById(idMenu);
		populateModel( model, menuItem, FormMode.UPDATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/create" ) // GET or POST
	public String create(@Valid Menuitem menuItem, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'create'");
		try {
			if (!bindingResult.hasErrors()) {
				Menuitem created = service.save(menuItem);
				model.addAttribute(MAIN_ENTITY_NAME, created);

				//---
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				return redirectToForm(httpServletRequest, menuItem.getIdMenu() );
			} else {
				populateModel( model, menuItem, FormMode.CREATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			log("Action 'create' : Exception - " + e.getMessage() );
			messageHelper.addException(model, "global.error.create", e);
			populateModel( model, menuItem, FormMode.CREATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/update" ) // GET or POST
	public String update(@Valid Menuitem menuItem, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'update'");
		try {
			if (!bindingResult.hasErrors()) {
				//--- Perform database operations
				Menuitem saved = service.save(menuItem);
				model.addAttribute(MAIN_ENTITY_NAME, saved);
				//--- Set the result message
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				log("Action 'update' : update done - redirect");
				return redirectToForm(httpServletRequest, menuItem.getIdMenu());
			} else {
				log("Action 'update' : binding errors");
				populateModel( model, menuItem, FormMode.UPDATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			messageHelper.addException(model, "global.error.update", e);
			log("Action 'update' : Exception - " + e.getMessage() );
			populateModel( model, menuItem, FormMode.UPDATE);
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

}
