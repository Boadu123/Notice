package com.example.notice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.notice.Model.NoticeModel;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeModel, Long> {
    
}
