package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import play.libs.Crypto;

@Entity
public class User extends Model {
	@Email
	@Required(message = "Requis")
	public String email;

	@Required(message = "Requis")
	public String password;

	@Required(message = "Requis")
	public String fullname;

	public Double defaultTicketValue;

	public boolean isAdmin;

	public User(String email, String password, String fullname) {
		this.email = email;
		this.password = password;
		this.fullname = fullname;
	}

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, Crypto.passwordHash(password)).first();
	}

	public void resetDefaultTicketValue() {
		defaultTicketValue = null;
	}

	public String toString() {
		return email;
	}
}