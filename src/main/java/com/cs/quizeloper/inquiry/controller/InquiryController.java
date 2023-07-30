package com.cs.quizeloper.inquiry.controller;

import com.cs.quizeloper.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inquires")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;
}
