package org.demo.web.controller;

import org.demo.bean.jpa.User;
import org.demo.business.service.MenuItemService;
import org.demo.business.service.UserService;
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
@RequestMapping("/user")
public class UserController extends AbstractController {

	//--- Variables names ( to be used in JSP with Expression Language )
	private static final String MAIN_ENTITY_NAME = "user";
	private static final String MAIN_LIST_NAME   = "list";

	//--- JSP pages names ( View name in the MVC model )
	private static final String JSP_FORM   = "user/form";
	private static final String JSP_LIST   = "user/list";

	//--- SAVE ACTION ( in the HTML form )
	private static final String SAVE_ACTION_CREATE   = "/user/create";
	private static final String SAVE_ACTION_UPDATE   = "/user/update";

	//--- Main entity service
	@Resource
    private UserService service; // Injected by Spring

	@Resource
	private MenuItemService menuItemService; // Injected by Spring

	public UserController() {
		super(UserController.class, MAIN_ENTITY_NAME );
		log("UserController created.");
	}

	private void populateModel(Model model, User user, FormMode formMode) {
		//--- Main entity
		model.addAttribute(MAIN_ENTITY_NAME, user);
		model.addAttribute("menuitem", menuItemService.findByHeader("Role"));
		model.addAttribute("menuitemUnits", menuItemService.findByHeader("Devisi"));
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
		List<User> list = service.findAll();
		model.addAttribute(MAIN_LIST_NAME, list);		
		return JSP_LIST;
	}

	@RequestMapping("/form")
	public String formForCreate(Model model) {
		log("Action 'formForCreate'");
		//--- Populates the model with a new instance
		User user = new User();
		populateModel( model, user, FormMode.CREATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/form/{idUser}")
	public String formForUpdate(Model model, @PathVariable("idUser") String idUser ) {
		log("Action 'formForUpdate'");
		//--- Search the entity by its primary key and stores it in the model 
		User user = service.findById(idUser);
		populateModel( model, user, FormMode.UPDATE);
		return JSP_FORM;
	}

	@RequestMapping(value = "/create" ) // GET or POST
	public String create(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'create'");
		try {
			if (!bindingResult.hasErrors()) {
				User byUsername = service.findByUsername(user.getUser());
				if(byUsername==null){
					User created = service.save(user);
					model.addAttribute(MAIN_ENTITY_NAME, created);

					//---
					messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS, "save.ok"));
					return redirectToForm(httpServletRequest, user.getIdUser() );
				}else{
					bindingResult.rejectValue("user", "form.validation.match");
					populateModel( model, user, FormMode.UPDATE);
					return JSP_FORM;
				}
			} else {
				populateModel( model, user, FormMode.CREATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			log("Action 'create' : Exception - " + e.getMessage() );
			messageHelper.addException(model, "global.error.create", e);
			populateModel( model, user, FormMode.CREATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/update" ) // GET or POST
	public String update(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'update'");
		try {
			if (!bindingResult.hasErrors()) {
				//--- Perform database operations
				User saved = service.save(user);
				model.addAttribute(MAIN_ENTITY_NAME, saved);
				//--- Set the result message
				messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"save.ok"));
				log("Action 'update' : update done - redirect");
				return redirectToForm(httpServletRequest, user.getIdUser());
			} else {
				log("Action 'update' : binding errors");
				populateModel( model, user, FormMode.UPDATE);
				return JSP_FORM;
			}
		} catch(Exception e) {
			messageHelper.addException(model, "global.error.update", e);
			log("Action 'update' : Exception - " + e.getMessage() );
			populateModel( model, user, FormMode.UPDATE);
			return JSP_FORM;
		}
	}

	@RequestMapping(value = "/delete/{idUser}") // GET or POST
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("idUser") String idUser) {
		log("Action 'delete'" );
		try {
			service.delete( idUser );
			//--- Set the result message
			messageHelper.addMessage(redirectAttributes, new Message(MessageType.SUCCESS,"delete.ok"));	
		} catch(Exception e) {
			messageHelper.addException(redirectAttributes, "global.error.delete", e);
		}
		return redirectToList();
	}

}
