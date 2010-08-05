package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Requete extends Model {
	public static final String TYPE_TICKETRESTO = "ticketResto";
	public static final String TYPE_DIVISION = "division";

	@Required
	public String type;
	
	@Required
	public Date dateRequest;

	public String userAgent;

	@Required
	public double value1;

	@Required
	public double value2;

	@Required
	public String result;

	public Requete(String type) {
		this.type = type;
	}

	public String toString() {
		return type + " - " + value1 + " | " + value2 + " = " + result;
	}
}