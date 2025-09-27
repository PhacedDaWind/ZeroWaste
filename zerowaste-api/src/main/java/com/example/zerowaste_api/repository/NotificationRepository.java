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


    @Modifying
    @Query("UPDATE Notification n SET ")
}

