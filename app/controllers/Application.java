package controllers;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import models.Requete;
import models.User;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.libs.Crypto;
import play.mvc.Controller;

public class Application extends Controller {
	public static void index() {
		indexTicket();
	}

	public static void indexTicket() {
		flash.clear();
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			if (user == null) {
				try {
					Secure.logout();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			if (user.defaultTicketValue != null) {
				renderArgs.put("defaultTicketValue", user.defaultTicketValue);
			}
		}
		render("Application/ticket.html");
	}

	public static void indexDivision() {
		flash.clear();
		render("Application/division.html");
	}

	public static void calculateTicket(double valeurTicket, double totalAddition) {
		validation.min(valeurTicket, 0.1).message("La valeur du ticket est obligatoire");
		validation.min(totalAddition, 0.1).message("Le total de l'addition est obligatoire");
		if (validation.hasErrors()) {
			render("Application/ticket.html");
		}

		int nombreTicket = new Double(totalAddition / valeurTicket).intValue();
		double resteAPayer = totalAddition % valeurTicket;

		if (resteAPayer == 0d) {
			flash.success("Vous devez %s ticket(s)", nombreTicket);
		} else {
			flash.success("Vous devez %s ticket(s) & %s €", nombreTicket, new DecimalFormat("#0.00").format(resteAPayer));
		}

		Requete requete = new Requete(Requete.TYPE_TICKETRESTO);
		requete.dateRequest = Calendar.getInstance().getTime();
		if (request != null && request.headers != null && request.headers.containsKey("user-agent")) {
			requete.userAgent = request.headers.get("user-agent").value();
		}
		requete.value1 = valeurTicket;
		requete.value2 = totalAddition;
		requete.result = flash.get("success");
		requete.save();

		render("Application/ticket.html");
	}

	public static void calculateDivision(int nombrePart, double totalAddition) {
		validation.min(nombrePart, 0.1).message("Le nombre de part est obligatoire");
		validation.min(totalAddition, 0.1).message("Le total de l'addition est obligatoire");
		if (validation.hasErrors()) {
			render("Application/division.html");
		}

		double resteAPayer = totalAddition / nombrePart;

		flash.success("Les %s personnes doivent chacun %s €", nombrePart, new DecimalFormat("#0.00").format(resteAPayer));

		Requete requete = new Requete(Requete.TYPE_DIVISION);
		requete.dateRequest = Calendar.getInstance().getTime();
		if (request != null && request.headers != null && request.headers.containsKey("user-agent")) {
			requete.userAgent = request.headers.get("user-agent").value();
		}
		requete.value1 = nombrePart;
		requete.value2 = totalAddition;
		requete.result = flash.get("success");
		requete.save();

		render("Application/division.html");
	}

	public static void register(Long id) {
		if (id == null) {
			render();
		}
		User user = User.findById(id);
		render(user);
	}

	public static void saveRegister(@Valid @Required User user, @Required(message = "Requis") String confirm) {
		validation.equals(user.password, confirm).key("confirm").message("Le mot de passe et la confirmation sont différents...");
		if (validation.hasErrors()) {
			if (request.isAjax())
				error("Invalid value");
			render("@register", user);
		}
		user.password = Crypto.passwordHash(user.password);
		user.save();
		renderTemplate("Application/registered.html", user);
	}

	/* ********* API ******** */
	public static void calculateTicketXml(double valeurTicket, double totalAddition) {
		validation.min(valeurTicket, 0.1).message("La valeur du ticket est obligatoire");
		validation.min(totalAddition, 0.1).message("Le total de l'addition est obligatoire");
		if (validation.hasErrors()) {
			render("Application/ticket.xml");
		}

		int nombreTicket = new Double(totalAddition / valeurTicket).intValue();
		double resteAPayerValeur = totalAddition % valeurTicket;

		String resteAPayer = new DecimalFormat("#0.00").format(resteAPayerValeur);

		renderTemplate("Application/ticket.xml", valeurTicket, totalAddition, nombreTicket, resteAPayer);
	}

	public static void calculateDivisionXml(int nombrePart, double totalAddition) {
		validation.min(nombrePart, 0.1).message("Le nombre de part est obligatoire");
		validation.min(totalAddition, 0.1).message("Le total de l'addition est obligatoire");
		if (validation.hasErrors()) {
			render("Application/division.xml");
		}

		double resteAPayerValeur = totalAddition / nombrePart;

		String resteAPayer = new DecimalFormat("#0.00").format(resteAPayerValeur);

		render("Application/division.xml", nombrePart, totalAddition, resteAPayer);
	}
}