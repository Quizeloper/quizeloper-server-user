package com.cs.quizeloper.inquiry.entity;

import com.cs.quizeloper.global.entity.BaseEntity;
import com.cs.quizeloper.inquiry.dto.PostInquiryReq;
import com.cs.quizeloper.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String answer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryType type;

    @Column
    private String referenceLink;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Builder
    public Inquiry(String title, String content, String answer, InquiryType type, String referenceLink, InquiryStatus inquiryStatus, User user) {
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.type = type;
        this.referenceLink = referenceLink;
        this.inquiryStatus = inquiryStatus;
        this.user = user;
    }

    public static Inquiry toEntity(PostInquiryReq req, User user){
        return Inquiry.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .referenceLink(req.getLink())
                .type(InquiryType.getTypeByName(req.getType()))
                .inquiryStatus(InquiryStatus.WAITING)
                .user(user)
                .build();
    }
}
