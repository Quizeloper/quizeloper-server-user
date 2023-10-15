package com.cs.quizeloper.inquiry.service;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.inquiry.repository.InquiryRepository;
import com.cs.quizeloper.inquiry.dto.GetInquiryRes;
import com.cs.quizeloper.inquiry.dto.GetShortInquiryRes;
import com.cs.quizeloper.inquiry.dto.PostInquiryReq;
import com.cs.quizeloper.inquiry.entity.Inquiry;
import com.cs.quizeloper.inquiry.entity.InquiryStatus;
import com.cs.quizeloper.user.repository.UserRepository;
import com.cs.quizeloper.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cs.quizeloper.global.exception.BaseResponseStatus.INQUIRY_ALREADY_REPLIED;
import static com.cs.quizeloper.global.exception.BaseResponseStatus.INQUIRY_USER_NOT_MATCHED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    // 문의하기 생성
    @Transactional
    public void postInquiry(Long userId, PostInquiryReq inquiryReq) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        inquiryRepository.save(Inquiry.toEntity(inquiryReq, user));
    }

    // 문의하기 상세 조회
    public GetInquiryRes getInquiry(Long userId, Long inquiryId) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Inquiry inquiry = inquiryRepository.findByIdAndStatus(inquiryId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.INQUIRY_NOT_FOUND));
        return GetInquiryRes.toDto(inquiry, inquiry.getUser().equals(user));
    }

    public Page<GetShortInquiryRes> getInquiries(Pageable pageable) {
        return inquiryRepository.findByOrderByCreatedDateDesc(pageable)
                .map(GetShortInquiryRes::toDto);
    }

    @Transactional
    public void patchInquiry(Long userId, Long inquiryId, PostInquiryReq inquiryReq) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Inquiry inquiry = inquiryRepository.findByIdAndStatus(inquiryId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.INQUIRY_NOT_FOUND));
        // 사용자가 생성한 문의사항이 아닌경우
        if (!inquiry.getUser().equals(user)) throw new BaseException(INQUIRY_USER_NOT_MATCHED);
        // 이미 답변 완료가 된 경우
        if(inquiry.getInquiryStatus().equals(InquiryStatus.REPLIED)) throw new BaseException(INQUIRY_ALREADY_REPLIED);
        inquiry.toPatchEntity(inquiryReq);
        inquiryRepository.save(inquiry);
    }

    @Transactional
    public void deleteInquiry(Long userId, Long inquiryId) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Inquiry inquiry = inquiryRepository.findByIdAndStatus(inquiryId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.INQUIRY_NOT_FOUND));
        // 사용자가 생성한 문의사항이 아닌경우
        if (!inquiry.getUser().equals(user)) throw new BaseException(INQUIRY_USER_NOT_MATCHED);
        inquiryRepository.delete(inquiry);
    }
}
