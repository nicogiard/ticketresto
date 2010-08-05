package controllers;

import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Profile extends Controller {

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user.fullname);
		}
	}

	public static void index() {
		Double defaultTicketValue = null;
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			defaultTicketValue = user.defaultTicketValue;
		}
		render(defaultTicketValue);
	}

	public static void save(Double defaultTicketValue) {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			if (defaultTicketValue == null) {
				user.resetDefaultTicketValue();
			} else {
				user.defaultTicketValue = defaultTicketValue;
			}
			user.save();
		}
		index();
	}
}