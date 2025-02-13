package com.example.notice.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notice.Model.NoticeModel;
import com.example.notice.Repository.NoticeRepository;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<NoticeModel> getAllNotices() {
        return noticeRepository.findAll();
    }

    public NoticeModel createNotice(NoticeModel noticeModel) {
        return noticeRepository.save(noticeModel);
    }
}
