package com.cs.quizeloper.inquiry.service;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.inquiry.Repository.InquiryRepository;
import com.cs.quizeloper.inquiry.dto.GetInquiryRes;
import com.cs.quizeloper.inquiry.dto.PostInquiryReq;
import com.cs.quizeloper.inquiry.entity.Inquiry;
import com.cs.quizeloper.user.Repository.UserRepository;
import com.cs.quizeloper.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    // 문의하기 생성
    public void postInquiry(Long userId, PostInquiryReq inquiryReq) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        inquiryRepository.save(Inquiry.toDto(inquiryReq, user));
    }

    // 문의하기 상세 조회
    public GetInquiryRes getInquiry(Long userId, Long inquiryId) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Inquiry inquiry = inquiryRepository.findByIdAndStatus(inquiryId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.INQUIRY_NOT_FOUND));
        return GetInquiryRes.toDto(inquiry, inquiry.getUser().equals(user));
    }
}
