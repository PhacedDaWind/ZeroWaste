package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.entity.Notification;
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
    @Query("SELECT u FROM Notification u WHERE u.usersId=:userId ORDER BY u.createdAt DESC")
    List<Notification> findByUserId(@Param("userId") BigInteger userId);

    //fetch all notification for a user that is unread
    @Query("SELECT u FROM Notification u WHERE u.usersId=:usersId AND u.markAsRead=false ORDER BY u.createdAt DESC")
    List<Notification> findByUnread(@Param("usersId") BigInteger usersId);

    //mark specific unread notification as Read?
    @Modifying
    @Query("UPDATE Notification u SET u.markAsRead=True WHERE u.id=:notificationId AND u.usersId=:usersId AND u.markAsRead=false")
    void changeToRead(@Param("notificationId") BigInteger id, @Param("usersId") BigInteger usersId);

    //Delete specific notification
    @Modifying
    @Query("DELETE FROM Notification u WHERE u.id=:notificationId")
    void deleteByNotificationId(@Param("notificationId") BigInteger id);

    //Delete whole notification for a specific user
    @Modifying
    @Query("DELETE FROM Notification u WHERE u.usersId=:usersId ")
    void deleteWholeNotifications(@Param("usersId") BigInteger usersId);
}

