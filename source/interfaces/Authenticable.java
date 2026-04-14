package source.interfaces;

import source.models.User;

public interface Authenticable {

    User login(String email, String password);
    boolean signup(String email, String password, String name, String role);
    void logout();
    boolean validateCredentials(String email, String password);
} 
