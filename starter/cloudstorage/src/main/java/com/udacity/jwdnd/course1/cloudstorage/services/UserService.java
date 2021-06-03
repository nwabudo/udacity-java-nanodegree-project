package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    
    boolean isUsernameAvailable(String username);

    int createUser(User user);

    User getSignedInUser(Authentication authentication);
}
