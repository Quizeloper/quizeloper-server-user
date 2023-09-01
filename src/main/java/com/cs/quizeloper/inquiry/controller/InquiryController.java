package com.cs.quizeloper.inquiry.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.resolver.Account;
import com.cs.quizeloper.global.resolver.UserInfo;
import com.cs.quizeloper.inquiry.dto.GetInquiryRes;
import com.cs.quizeloper.inquiry.dto.GetShortInquiryRes;
import com.cs.quizeloper.inquiry.dto.PostInquiryReq;
import com.cs.quizeloper.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    // 문의하기 상세조회
    @GetMapping("/{inquiryId}")
    public BaseResponse<GetInquiryRes> getInquiry(@Account UserInfo userInfo, @PathVariable(name = "inquiryId") Long inquiryId){
        return new BaseResponse<>(inquiryService.getInquiry(userInfo.getId(), inquiryId));
    }

    // 문의하기 리스트 조회
    @GetMapping("")
    public BaseResponse<Page<GetShortInquiryRes>> getInquiries(Pageable pageable){
        return new BaseResponse<>(inquiryService.getInquiries(pageable));
    }
}
