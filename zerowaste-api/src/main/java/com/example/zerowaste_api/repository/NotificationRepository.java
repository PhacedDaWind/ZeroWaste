package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.entity.Notification;
import com.example.zerowaste_api.enums.NotificationType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    //fetch all notification for a user sorted by most recent
    @Query("SELECT u FROM Notification u WHERE u.usersId=:userId AND (:notifType IS NULL OR u.notifType = :notifType) ORDER BY u.createdAt DESC")
    List<Notification> findByUserIdAndNotifType(@Param("userId") Long userId, @Param("notifType")NotificationType notifType);

    //fetch all notification for a user that is unread
    @Query("SELECT u FROM Notification u WHERE u.usersId=:userId AND u.markAsRead=false ORDER BY u.createdAt DESC")
    List<Notification> findByUnread(@Param("userId") Long userId);

    //Delete whole notification for a specific user
    @Transactional
    @Modifying
    @Query("DELETE FROM Notification u WHERE u.usersId=:usersId ")
    void deleteWholeNotifications(@Param("usersId") Long usersId);
}

