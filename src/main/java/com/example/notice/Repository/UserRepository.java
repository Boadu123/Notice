package com.example.notice.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notice.Model.UserModel;

@Repository
public interface UserRepository extends  JpaRepository<UserModel, Long>{
    Optional<UserModel> findByEmail(String email);
}
