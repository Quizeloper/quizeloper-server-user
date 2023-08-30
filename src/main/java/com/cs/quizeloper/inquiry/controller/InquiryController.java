package com.cs.quizeloper.inquiry.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.resolver.Account;
import com.cs.quizeloper.global.resolver.UserInfo;
import com.cs.quizeloper.inquiry.dto.PostInquiryReq;
import com.cs.quizeloper.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inquires")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    // 문의하기
    @PostMapping("")
    public BaseResponse<BaseResponseStatus> postInquiry(@Account UserInfo userInfo, @RequestBody @Valid PostInquiryReq inquiryReq){
        inquiryService.postInquiry(userInfo.getId(), inquiryReq);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
