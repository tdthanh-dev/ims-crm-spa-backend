package com.htttql.crmmodule.lead.repository;

import com.htttql.crmmodule.lead.entity.Appointment;
import com.htttql.crmmodule.common.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Appointment entity - Simplified for basic reminders
 */
@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find appointments by customer ID
     */
    List<Appointment> findByCustomerId(Long customerId);

    /**
     * Find appointments by customer ID with pagination, ordered by appointment date time descending
     */
    Page<Appointment> findByCustomerIdOrderByAppointmentDateTimeDesc(Long customerId, Pageable pageable);

    /**
     * Find appointments by lead ID
     */
    List<Appointment> findByLeadId(Long leadId);

    /**
     * Find appointments by status
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Find appointments by appointment date
     */
    Page<Appointment> findByAppointmentDateTime(LocalDate date, Pageable pageable);

    /**
     * Find appointments between date range
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate")
    List<Appointment> findByAppointmentDateTimeBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find appointments for today
     */
    @Query("SELECT a FROM Appointment a WHERE DATE(a.appointmentDateTime) = :date")
    Page<Appointment> findByAppointmentDateToday(@Param("date") LocalDate date, Pageable pageable);

    /**
     * Count appointments by date
     */
    long countByAppointmentDateTime(LocalDate date);

    /**
     * Count appointments by date and status
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE DATE(a.appointmentDateTime) = :date AND a.status = :status")
    long countByDateAndStatus(@Param("date") LocalDate date, @Param("status") AppointmentStatus status);

    /**
     * Count appointments by date range
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate")
    long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Count appointments by status
     */
    long countByStatus(AppointmentStatus status);

    /**
     * Count appointments by customer ID
     */
    long countByCustomerId(Long customerId);

    /**
     * Count appointments by lead ID
     */
    long countByLeadId(Long leadId);

    /**
     * Count appointments by date range and status
     */
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate AND a.status = :status")
    long countByDateRangeAndStatus(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") AppointmentStatus status);

    /**
     * Find upcoming appointments (future dates)
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime >= :date ORDER BY a.appointmentDateTime")
    List<Appointment> findUpcomingAppointments(@Param("date") LocalDateTime date);

    /**
     * Find past appointments (past dates)
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime < :date ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findPastAppointments(@Param("date") LocalDateTime date);

    /**
     * Find today's appointments
     */
    @Query("SELECT a FROM Appointment a WHERE DATE(a.appointmentDateTime) = :date ORDER BY a.appointmentDateTime")
    List<Appointment> findTodayAppointments(@Param("date") LocalDate date);
}
