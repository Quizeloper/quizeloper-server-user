package com.cs.quizeloper.inquiry.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.inquiry.entity.Inquiry;
import com.cs.quizeloper.inquiry.entity.InquiryStatus;
import com.cs.quizeloper.inquiry.entity.InquiryType;
import com.cs.quizeloper.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Optional<Inquiry> findByIdAndStatus(Long inquiryId, BaseStatus status);
    Page<Inquiry> findByOrderByCreatedDateDesc(Pageable pageable);
    Integer countByUserAndStatus(User user, BaseStatus status);
    Integer countByUserAndStatusAndInquiryStatus(User user, BaseStatus status, InquiryStatus inquiryStatus);
}
