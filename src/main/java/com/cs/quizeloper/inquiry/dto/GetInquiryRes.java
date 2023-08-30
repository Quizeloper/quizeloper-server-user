package com.cs.quizeloper.inquiry.dto;

import com.cs.quizeloper.inquiry.entity.Inquiry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInquiryRes {
    private String type;
    private String title;
    private String content;
    private String link;
    private String answer;
    private Boolean byUser;

    public static GetInquiryRes toDto(Inquiry inquiry, Boolean byUser){
        return GetInquiryRes.builder()
                .type(inquiry.getType().getValue())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .link(inquiry.getReferenceLink())
                .answer(inquiry.getAnswer())
                .byUser(byUser)
                .build();
    }
}
