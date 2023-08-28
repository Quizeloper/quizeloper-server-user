package com.cs.quizeloper.user.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmailAndStatus(String email, BaseStatus status);
    Boolean existsByNicknameAndStatus(String email, BaseStatus status);
}
