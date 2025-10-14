package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.dto.UserDetailsTuple;
import com.example.zerowaste_api.entity.Users;
import jakarta.validation.constraints.Min;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.username = :username")
    Optional<Users> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Optional<Object> findByEmail(String email);

    @Query(value = "SELECT u.id as id, " +
            "u.username as username, " +
            "u.email as email, " +
            "u.household_size as householdSize, " +
            "u.two_factor_auth_enabled as twoFactorAuthEnabled, " +
            "u.status as status, " +
            "COUNT(f.id) as totalItems, " +
            "SUM(CASE WHEN f.convert_to_donation = true THEN 1 ELSE 0 END) as donationsMade " +
            "FROM users u " +
            "LEFT JOIN food_item f ON f.users_id = u.id " +
            "WHERE u.id = :id " +
            "GROUP BY u.id", nativeQuery = true)
    UserDetailsTuple findUserDetailsTuple(@Param("id") Long id);

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Optional<Users> findUserByEmail(String email);
}