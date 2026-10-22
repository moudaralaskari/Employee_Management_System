package com.example.employee_system.Repository;

import com.example.employee_system.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findOneByUsername(String username);

    @Query("select count(u) > 0 from UserAccount u where u.employee.id = :employeeId and u.username = :username")
    public boolean isOwner(@Param("username") String username, @Param("employeeId") UUID employeeId);
}
