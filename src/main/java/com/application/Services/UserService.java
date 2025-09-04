package com.application.Services;

import java.util.Map;

import com.application.Entities.User;

public interface UserService {
   
    Map<String, String> registerUser(String name, String email, String rawPassword);
    Map<String, String> loginUser(String email, String rawPassword);
    User getUserByEmail(String email);

}
