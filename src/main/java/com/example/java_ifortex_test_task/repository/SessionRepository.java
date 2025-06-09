package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(
            value = "SELECT s.id, s.user_id, s.started_at_utc, s.ended_at_utc, " +
                    "CASE s.device_type " +
                    "  WHEN 1 THEN 0 " +
                    "  WHEN 2 THEN 1 " +
                    "END as device_type " +
                    "FROM sessions s WHERE s.device_type = :#{#deviceType.code} ORDER BY s.started_at_utc ASC LIMIT 1",
            nativeQuery = true
    )
    Session getFirstDesktopSession(@Param("deviceType") DeviceType deviceType);

    @Query(
            value = "SELECT s.id, s.user_id, s.started_at_utc, s.ended_at_utc, " +
                    "CASE s.device_type " +
                    "  WHEN 1 THEN 0 " +
                    "  WHEN 2 THEN 1 " +
                    "END as device_type " +
                    "FROM sessions s JOIN users u ON s.user_id = u.id " +
                    "WHERE NOT u.deleted AND s.ended_at_utc < ?1 ORDER BY s.ended_at_utc DESC",
            nativeQuery = true
    )
    List<Session> getSessionsFromActiveUsersEndedBefore2025(LocalDateTime endDate);
}