package controllers;

import models.*;

public class Login extends Secure {
	public static void preLogin() throws Throwable {
		flash.clear();
		Secure.login();
	}
}