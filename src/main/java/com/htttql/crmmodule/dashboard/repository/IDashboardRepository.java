package com.htttql.crmmodule.dashboard.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Dashboard Repository - chứa các query native cho dashboard analytics
 * Không kế thừa từ JpaRepository để tránh conflict với multiple inheritance
 */
@Repository
public interface IDashboardRepository {

    // ===== Dashboard Analytics Queries =====

    /**
     * Tổng quan dashboard - đếm số lượng entities
     */
    @Query("SELECT " +
            "COUNT(DISTINCT c) as totalCustomers, " +
            "COUNT(DISTINCT s) as totalStaff, " +
            "COUNT(DISTINCT cs) as totalCases, " +
            "COUNT(DISTINCT a) as totalAppointments, " +
            "COUNT(DISTINCT i) as totalInvoices, " +
            "COUNT(DISTINCT p) as totalPayments " +
            "FROM Customer c, StaffUser s, CustomerCase cs, Appointment a, Invoice i, Payment p")
    List<Object[]> getDashboardOverview();

    /**
     * Thống kê khách hàng theo tháng
     */
    @Query("SELECT " +
            "DATE_FORMAT(c.createdAt, '%Y-%m') as month, " +
            "COUNT(c) as count " +
            "FROM Customer c " +
            "WHERE c.createdAt >= :startDate " +
            "GROUP BY DATE_FORMAT(c.createdAt, '%Y-%m') " +
            "ORDER BY month")
    List<Object[]> getCustomerStatsByMonth(@Param("startDate") LocalDateTime startDate);

    /**
     * Thống kê doanh thu theo tháng
     */
    @Query("SELECT " +
            "DATE_FORMAT(i.createdAt, '%Y-%m') as month, " +
            "SUM(i.totalAmount) as revenue, " +
            "COUNT(i) as invoiceCount " +
            "FROM Invoice i " +
            "WHERE i.createdAt >= :startDate " +
            "GROUP BY DATE_FORMAT(i.createdAt, '%Y-%m') " +
            "ORDER BY month")
    List<Object[]> getRevenueStatsByMonth(@Param("startDate") LocalDateTime startDate);

    /**
     * Thống kê appointment theo trạng thái
     */
    @Query("SELECT " +
            "a.status as status, " +
            "COUNT(a) as count " +
            "FROM Appointment a " +
            "WHERE a.appointmentDateTime >= :startDate " +
            "GROUP BY a.status")
    List<Object[]> getAppointmentStatsByStatus(@Param("startDate") LocalDateTime startDate);

    /**
     * Thống kê case theo trạng thái
     */
    @Query("SELECT " +
            "cs.status as status, " +
            "COUNT(cs) as count " +
            "FROM CustomerCase cs " +
            "WHERE cs.createdAt >= :startDate " +
            "GROUP BY cs.status")
    List<Object[]> getCaseStatsByStatus(@Param("startDate") LocalDateTime startDate);

    /**
     * Thống kê top khách hàng có nhiều case nhất
     */
    @Query("SELECT " +
            "c.fullName as customerName, " +
            "COUNT(cs) as caseCount, " +
            "SUM(cs.totalAmount) as totalValue " +
            "FROM Customer c " +
            "LEFT JOIN c.cases cs " +
            "WHERE cs.createdAt >= :startDate " +
            "GROUP BY c.customerId, c.fullName " +
            "ORDER BY caseCount DESC " +
            "LIMIT :limit")
    List<Object[]> getTopCustomersByCaseCount(
        @Param("startDate") LocalDateTime startDate,
        @Param("limit") int limit
    );

    /**
     * Thống kê top dịch vụ được sử dụng nhiều nhất
     */
    @Query("SELECT " +
            "s.name as serviceName, " +
            "COUNT(cs) as usageCount, " +
            "SUM(cs.totalAmount) as totalRevenue " +
            "FROM SpaService s " +
            "LEFT JOIN s.customerCases cs " +
            "WHERE cs.createdAt >= :startDate " +
            "GROUP BY s.serviceId, s.name " +
            "ORDER BY usageCount DESC " +
            "LIMIT :limit")
    List<Object[]> getTopServicesByUsage(
        @Param("startDate") LocalDateTime startDate,
        @Param("limit") int limit
    );

    /**
     * Thống kê staff performance
     */
    @Query("SELECT " +
            "s.fullName as staffName, " +
            "COUNT(cs) as caseCount, " +
            "COUNT(a) as appointmentCount, " +
            "SUM(cs.totalAmount) as totalValue " +
            "FROM StaffUser s " +
            "LEFT JOIN s.customerCases cs " +
            "LEFT JOIN s.appointments a " +
            "WHERE cs.createdAt >= :startDate " +
            "GROUP BY s.staffId, s.fullName " +
            "ORDER BY totalValue DESC " +
            "LIMIT :limit")
    List<Object[]> getStaffPerformance(
        @Param("startDate") LocalDateTime startDate,
        @Param("limit") int limit
    );

    /**
     * Thống kê audit logs theo action
     */
    @Query("SELECT " +
            "al.action as action, " +
            "COUNT(al) as count " +
            "FROM AuditLog al " +
            "WHERE al.createdAt >= :startDate " +
            "GROUP BY al.action " +
            "ORDER BY count DESC")
    List<Object[]> getAuditStatsByAction(@Param("startDate") LocalDateTime startDate);

    /**
     * Thống kê task theo trạng thái
     */
    @Query("SELECT " +
            "t.status as status, " +
            "COUNT(t) as count " +
            "FROM Task t " +
            "WHERE t.createdAt >= :startDate " +
            "GROUP BY t.status")
    List<Object[]> getTaskStatsByStatus(@Param("startDate") LocalDateTime startDate);

    /**
     * Thống kê payment methods
     */
    @Query("SELECT " +
            "p.paymentMethod as method, " +
            "COUNT(p) as count, " +
            "SUM(p.amount) as totalAmount " +
            "FROM Payment p " +
            "WHERE p.createdAt >= :startDate " +
            "GROUP BY p.paymentMethod")
    List<Object[]> getPaymentStatsByMethod(@Param("startDate") LocalDateTime startDate);

    /**
     * Dashboard real-time data
     */
    @Query("SELECT " +
            "COUNT(DISTINCT CASE WHEN c.createdAt >= :today THEN c END) as newCustomersToday, " +
            "COUNT(DISTINCT CASE WHEN a.appointmentDateTime >= :today AND a.appointmentDateTime < :tomorrow THEN a END) as appointmentsToday, " +
            "COUNT(DISTINCT CASE WHEN i.createdAt >= :today THEN i END) as invoicesToday, " +
            "SUM(CASE WHEN p.createdAt >= :today THEN p.amount ELSE 0 END) as revenueToday " +
            "FROM Customer c, Appointment a, Invoice i, Payment p")
    List<Object[]> getRealtimeDashboardData(
        @Param("today") LocalDateTime today,
        @Param("tomorrow") LocalDateTime tomorrow
    );

    // ===== RECEPTIONIST DASHBOARD QUERIES =====

    /**
     * Lịch hẹn hôm nay cho receptionist
     */
    @Query("SELECT " +
            "COUNT(DISTINCT a) as totalAppointments, " +
            "COUNT(DISTINCT CASE WHEN a.status = 'SCHEDULED' THEN a END) as scheduledAppointments, " +
            "COUNT(DISTINCT CASE WHEN a.status = 'CONFIRMED' THEN a END) as confirmedAppointments, " +
            "COUNT(DISTINCT CASE WHEN a.status = 'IN_PROGRESS' THEN a END) as inProgressAppointments, " +
            "COUNT(DISTINCT CASE WHEN a.status = 'DONE' THEN a END) as completedAppointments " +
            "FROM Appointment a " +
            "WHERE DATE(a.appointmentDateTime) = :today")
    List<Object[]> getTodayAppointmentsSummary(@Param("today") LocalDate today);

    /**
     * Khách hàng có lịch hẹn hôm nay
     */
    @Query("SELECT " +
            "c.fullName as customerName, " +
            "c.phone as customerPhone, " +
            "a.appointmentDateTime as appointmentTime, " +
            "a.status as appointmentStatus, " +
            "s.name as serviceName, " +
            "a.note as appointmentNote " +
            "FROM Appointment a " +
            "JOIN a.customer c " +
            "LEFT JOIN a.primaryService s " +
            "WHERE DATE(a.appointmentDateTime) = :today " +
            "ORDER BY a.appointmentDateTime")
    List<Object[]> getTodayAppointmentsDetail(@Param("today") LocalDate today);

    /**
     * Tasks cần làm hôm nay cho receptionist
     */
    @Query("SELECT " +
            "t.title as taskTitle, " +
            "t.description as taskDescription, " +
            "t.status as taskStatus, " +
            "t.priority as taskPriority, " +
            "t.dueDate as dueDate, " +
            "c.fullName as assignedTo " +
            "FROM Task t " +
            "LEFT JOIN t.assignedTo c " +
            "WHERE t.dueDate = :today OR t.status = 'IN_PROGRESS' " +
            "ORDER BY t.priority DESC, t.dueDate ASC")
    List<Object[]> getTodayTasksForReceptionist(@Param("today") LocalDate today);

    /**
     * Customer cases cần xử lý hôm nay
     */
    @Query("SELECT " +
            "c.fullName as customerName, " +
            "cs.serviceName as serviceName, " +
            "cs.status as caseStatus, " +
            "cs.createdAt as createdAt, " +
            "cs.totalAmount as totalAmount " +
            "FROM CustomerCase cs " +
            "JOIN cs.customer c " +
            "WHERE DATE(cs.createdAt) = :today " +
            "ORDER BY cs.createdAt DESC")
    List<Object[]> getTodayCasesForReceptionist(@Param("today") LocalDate today);

    /**
     * Hoạt động gần đây cho receptionist
     */
    @Query("SELECT " +
            "al.action as action, " +
            "al.entityType as entityType, " +
            "al.details as details, " +
            "al.createdAt as createdAt, " +
            "s.fullName as performedBy " +
            "FROM AuditLog al " +
            "LEFT JOIN al.performedBy s " +
            "WHERE al.createdAt >= :startTime " +
            "ORDER BY al.createdAt DESC " +
            "LIMIT 20")
    List<Object[]> getRecentActivitiesForReceptionist(@Param("startTime") LocalDateTime startTime);

    // ===== MANAGER DASHBOARD QUERIES =====

    /**
     * Manager dashboard - tổng quan chi tiết
     */
    @Query("SELECT " +
            "COUNT(DISTINCT c) as totalCustomers, " +
            "COUNT(DISTINCT s) as totalStaff, " +
            "COUNT(DISTINCT cs) as totalActiveCases, " +
            "COUNT(DISTINCT a) as totalAppointments, " +
            "COUNT(DISTINCT i) as totalInvoices, " +
            "COUNT(DISTINCT p) as totalPayments, " +
            "COUNT(DISTINCT l) as totalLeads, " +
            "COUNT(DISTINCT t) as totalTasks, " +
            "COUNT(DISTINCT al) as totalAuditLogs " +
            "FROM Customer c, StaffUser s, CustomerCase cs, Appointment a, Invoice i, Payment p, Lead l, Task t, AuditLog al")
    List<Object[]> getManagerDashboardOverview();

    /**
     * Revenue analytics cho manager
     */
    @Query("SELECT " +
            "DATE_FORMAT(i.createdAt, '%Y-%m-%d') as date, " +
            "COUNT(i) as invoiceCount, " +
            "SUM(i.totalAmount) as dailyRevenue, " +
            "COUNT(DISTINCT i.customer.customerId) as uniqueCustomers, " +
            "SUM(CASE WHEN i.status = 'PAID' THEN i.totalAmount ELSE 0 END) as paidRevenue, " +
            "SUM(CASE WHEN i.status = 'PENDING' THEN i.totalAmount ELSE 0 END) as pendingRevenue " +
            "FROM Invoice i " +
            "WHERE i.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(i.createdAt, '%Y-%m-%d') " +
            "ORDER BY date")
    List<Object[]> getRevenueAnalyticsForManager(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Staff performance cho manager
     */
    @Query("SELECT " +
            "s.fullName as staffName, " +
            "COUNT(DISTINCT a) as appointmentsHandled, " +
            "COUNT(DISTINCT cs) as casesHandled, " +
            "COUNT(DISTINCT i) as invoicesCreated, " +
            "SUM(i.totalAmount) as totalRevenue, " +
            "AVG(CASE WHEN a.status = 'DONE' THEN 1 ELSE 0 END) as completionRate " +
            "FROM StaffUser s " +
            "LEFT JOIN s.appointments a " +
            "LEFT JOIN s.customerCases cs " +
            "LEFT JOIN s.invoices i " +
            "WHERE a.appointmentDateTime >= :startDate OR cs.createdAt >= :startDate OR i.createdAt >= :startDate " +
            "GROUP BY s.staffId, s.fullName " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getStaffPerformanceForManager(@Param("startDate") LocalDateTime startDate);

    /**
     * Customer analytics cho manager
     */
    @Query("SELECT " +
            "DATE_FORMAT(c.createdAt, '%Y-%m') as month, " +
            "COUNT(c) as newCustomers, " +
            "COUNT(DISTINCT cs.customer.customerId) as customersWithCases, " +
            "COUNT(DISTINCT i.customer.customerId) as customersWithInvoices, " +
            "SUM(i.totalAmount) as totalSpent, " +
            "AVG(CASE WHEN cs.status = 'COMPLETED' THEN 1 ELSE 0 END) as completionRate " +
            "FROM Customer c " +
            "LEFT JOIN c.cases cs " +
            "LEFT JOIN c.invoices i " +
            "WHERE c.createdAt >= :startDate " +
            "GROUP BY DATE_FORMAT(c.createdAt, '%Y-%m') " +
            "ORDER BY month")
    List<Object[]> getCustomerAnalyticsForManager(@Param("startDate") LocalDateTime startDate);

    /**
     * Service performance cho manager
     */
    @Query("SELECT " +
            "s.name as serviceName, " +
            "COUNT(cs) as usageCount, " +
            "SUM(cs.totalAmount) as totalRevenue, " +
            "AVG(cs.totalAmount) as averageRevenue, " +
            "COUNT(CASE WHEN cs.status = 'COMPLETED' THEN 1 END) as completedCases, " +
            "COUNT(CASE WHEN cs.status = 'IN_PROGRESS' THEN 1 END) as inProgressCases " +
            "FROM SpaService s " +
            "LEFT JOIN s.customerCases cs " +
            "WHERE cs.createdAt >= :startDate " +
            "GROUP BY s.serviceId, s.name " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getServicePerformanceForManager(@Param("startDate") LocalDateTime startDate);

    /**
     * Inventory alerts cho manager
     */
    @Query("SELECT " +
            "s.name as serviceName, " +
            "COUNT(cs) as currentUsage, " +
            "s.maxCapacity as maxCapacity, " +
            "(s.maxCapacity - COUNT(cs)) as remainingCapacity " +
            "FROM SpaService s " +
            "LEFT JOIN s.customerCases cs ON cs.status IN ('IN_PROGRESS', 'SCHEDULED') " +
            "GROUP BY s.serviceId, s.name, s.maxCapacity " +
            "HAVING remainingCapacity <= 5")
    List<Object[]> getInventoryAlertsForManager();

    /**
     * Financial summary cho manager
     */
    @Query("SELECT " +
            "SUM(CASE WHEN i.status = 'PAID' THEN i.totalAmount ELSE 0 END) as totalPaid, " +
            "SUM(CASE WHEN i.status = 'PENDING' THEN i.totalAmount ELSE 0 END) as totalPending, " +
            "SUM(CASE WHEN i.status = 'OVERDUE' THEN i.totalAmount ELSE 0 END) as totalOverdue, " +
            "COUNT(DISTINCT CASE WHEN i.status = 'PAID' THEN i.customer.customerId END) as paidCustomers, " +
            "COUNT(DISTINCT CASE WHEN i.status = 'PENDING' THEN i.customer.customerId END) as pendingCustomers " +
            "FROM Invoice i " +
            "WHERE i.createdAt >= :startDate")
    List<Object[]> getFinancialSummaryForManager(@Param("startDate") LocalDateTime startDate);

    /**
     * Manager real-time dashboard data
     */
    @Query("SELECT " +
            "COUNT(DISTINCT CASE WHEN c.createdAt >= :today THEN c END) as newCustomersToday, " +
            "COUNT(DISTINCT CASE WHEN a.appointmentDateTime >= :today AND a.appointmentDateTime < :tomorrow THEN a END) as appointmentsToday, " +
            "COUNT(DISTINCT CASE WHEN i.createdAt >= :today THEN i END) as invoicesToday, " +
            "COUNT(DISTINCT CASE WHEN p.createdAt >= :today THEN p END) as paymentsToday, " +
            "SUM(CASE WHEN p.createdAt >= :today THEN p.amount ELSE 0 END) as revenueToday, " +
            "COUNT(DISTINCT CASE WHEN t.dueDate = :today THEN t END) as tasksDueToday, " +
            "COUNT(DISTINCT CASE WHEN cs.createdAt >= :today THEN cs END) as newCasesToday " +
            "FROM Customer c, Appointment a, Invoice i, Payment p, Task t, CustomerCase cs")
    List<Object[]> getManagerRealtimeDashboardData(
        @Param("today") LocalDateTime today,
        @Param("tomorrow") LocalDateTime tomorrow
    );
}
