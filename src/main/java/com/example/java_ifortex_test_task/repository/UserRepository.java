package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = "SELECT u.* FROM users u " +
                    "JOIN (" +
                    "    SELECT s.user_id, MAX(s.started_at_utc) AS latest_start_time " +
                    "    FROM sessions s " +
                    "    WHERE s.device_type = :#{#deviceType.code} " +
                    "    GROUP BY s.user_id" +
                    ") AS latest_mobile_sessions ON u.id = latest_mobile_sessions.user_id " +
                    "ORDER BY latest_mobile_sessions.latest_start_time DESC",
            nativeQuery = true
    )
    List<User> getUsersWithAtLeastOneMobileSession(@Param("deviceType") DeviceType deviceType);

    @Query(
            value = "SELECT u.* FROM users u JOIN sessions s ON u.id = s.user_id " +
                    "GROUP BY u.id ORDER BY COUNT(s.id) DESC LIMIT 1",
            nativeQuery = true
    )
    User getUserWithMostSessions();
}
