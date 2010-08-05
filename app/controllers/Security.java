package controllers;

import models.*;

public class Security extends Secure.Security {

    public static boolean authentify(String username, String password) {
        return User.connect(username, password) != null;
    }
    
    public static boolean check(String profile) {
        if("admin".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().isAdmin;
        }
        return false;
    }
    
    public static void onDisconnected() {
        Application.index();
    }
    
    public static void onAuthenticated() {
        Profile.index();
    }   
}