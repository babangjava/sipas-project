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
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/change")
public class ChangePasswordController extends AbstractController {

	private static final String MAIN_ENTITY_NAME = "user";
	private static final String JSP_CHANGE_FORM   = "user/change/form";

	private static final String SAVE_ACTION_CREATE   = "/user/create";
	private static final String SAVE_ACTION_UPDATE   = "/user/update";

	@Resource
    private UserService service; // Injected by Spring

	public ChangePasswordController() {
		super(ChangePasswordController.class, MAIN_ENTITY_NAME );
		log("UserController created.");
	}

	private void populateModel(Model model, User user, FormMode formMode) {
		//--- Main entity
		model.addAttribute(MAIN_ENTITY_NAME, user);
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

	@RequestMapping("/password")
	public String formChange(Model model,Principal principal) {
		log("Action 'Update Password'");
		String username = (String) principal.getName();
		User user = service.findByUsername(username);
		populateModel( model, user, FormMode.UPDATE);
		return JSP_CHANGE_FORM;
	}

	@RequestMapping(value = "/password/update" ) // GET or POST
	public String updatePassword(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
		log("Action 'update'");
		try {
			if (!bindingResult.hasErrors()) {
				String errorMessage = null;
				if(user.getOldPassword()== null || user.getNewPassword()==null){
					errorMessage = "form.validation.update.password";
				}else{
					if(!user.getOldPassword().equals(user.getPassword())){
						errorMessage = "form.validation.password.match";
					}
				}

				if(errorMessage==null){
					user.setPassword(user.getNewPassword());
					User saved = service.save(user);
					model.addAttribute(MAIN_ENTITY_NAME, user);
					//--- Set the result message
					messageHelper.addMessage(model, new Message(MessageType.SUCCESS,"save.ok"));
					log("Action 'update' : update done - redirect");
					return JSP_CHANGE_FORM;
				}else{
					messageHelper.addMessage(model, new Message(MessageType.DANGER, errorMessage));
					populateModel( model, user, FormMode.UPDATE);
					return JSP_CHANGE_FORM;
				}
			} else {
				log("Action 'update' : binding errors");
				populateModel( model, user, FormMode.UPDATE);
				return JSP_CHANGE_FORM;
			}
		} catch(Exception e) {
			messageHelper.addException(model, "global.error.update", e);
			log("Action 'update' : Exception - " + e.getMessage() );
			populateModel( model, user, FormMode.UPDATE);
			return JSP_CHANGE_FORM;
		}
	}

}
