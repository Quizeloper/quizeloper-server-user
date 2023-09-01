package com.cs.quizeloper.inquiry.dto;

import com.cs.quizeloper.inquiry.entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetShortInquiryRes {
    private String inquiryStatus;
    private String type;
    private String title;

    public static GetShortInquiryRes toDto(Inquiry inquiry){
        return GetShortInquiryRes.builder()
                .inquiryStatus(inquiry.getInquiryStatus().getValue())
                .type(inquiry.getType().getValue())
                .title(inquiry.getTitle())
                .build();
    }
}
