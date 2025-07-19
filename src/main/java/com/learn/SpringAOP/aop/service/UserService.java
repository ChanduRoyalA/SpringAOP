package com.learn.SpringAOP.aop.service;


import com.learn.SpringAOP.aop.model.User;
import com.learn.SpringAOP.aop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@Service
public class UserService  {


    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // object to encrypt the text

    public User getUser(Long id){
        User user =  userRepo.findById(id).get();
        return user;
    }

    public User addUser(User user){
        if(user.getUserPassword()!=null && user.getUserName()!=null){
            // encrypt the password
            user.setUserPassword(encoder.encode(user.getUserPassword()));
            if(user.getUserRole()!=null){
                userRepo.save(user);
            }
            else{
                user.setUserRole("USER");
                userRepo.save(user);
            }
        }
        else{
            System.out.println("User and Password are Required"+user.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User and Password are Required");
        }

        return user;
    }

    public Collection<User> getAllUsers() {
        List<User> allUsers = userRepo.findAll();
        return allUsers;
    }
}
