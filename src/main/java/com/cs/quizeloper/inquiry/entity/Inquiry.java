package com.cs.quizeloper.inquiry.entity;

import com.cs.quizeloper.global.entity.BaseEntity;
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

}
