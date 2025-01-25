package com.example.notice.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notice.Model.UserModel;
import com.example.notice.Repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<UserModel> getAllUsers(){
        return userRepository.findAll();
    }

    public void registerUser(UserModel user){
        userRepository.save(user);
    }
}
