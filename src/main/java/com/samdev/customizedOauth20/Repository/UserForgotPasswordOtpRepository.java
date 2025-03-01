package com.samdev.customizedOauth20.Repository;

import com.samdev.customizedOauth20.Entity.UserForgotPasswordOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserForgotPasswordOtpRepository extends JpaRepository<UserForgotPasswordOtp, Long> {
    @Query("SELECT u FROM UserForgotPasswordOtp u WHERE u.username = :username ORDER BY u.id DESC")
    Optional<UserForgotPasswordOtp> findByUsername(String username);

    Optional<UserForgotPasswordOtp> findTopByUsernameOrderByIdDesc(String username);
}
