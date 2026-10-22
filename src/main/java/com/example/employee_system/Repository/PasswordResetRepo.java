package com.example.employee_system.Repository;

import com.example.employee_system.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepo extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findOneByToken(String token);
}
