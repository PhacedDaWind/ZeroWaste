package com.example.zerowaste_api.repository;

import com.example.zerowaste_api.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    //fetch all notification for user sorted by most recent
    @Query("SELECT n FROM Notification n WHERE n.usersId=:userId ORDER BY n.createdAT DESC")
    List<Notification> findByUserId(@Param("userId") BigInteger userId);

    @Query("")
}
