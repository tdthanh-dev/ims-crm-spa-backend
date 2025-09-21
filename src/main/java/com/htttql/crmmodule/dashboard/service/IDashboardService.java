package com.htttql.crmmodule.dashboard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Service Interface
 */
public interface IDashboardService {

    /**
     * Lấy tổng quan dashboard
     */
    Map<String, Object> getDashboardOverview();

    /**
     * Lấy thống kê khách hàng theo tháng
     */
    List<Map<String, Object>> getCustomerStatsByMonth(LocalDateTime startDate);

    /**
     * Lấy thống kê doanh thu theo tháng
     */
    List<Map<String, Object>> getRevenueStatsByMonth(LocalDateTime startDate);

    /**
     * Lấy thống kê appointment theo trạng thái
     */
    List<Map<String, Object>> getAppointmentStatsByStatus(LocalDateTime startDate);

    /**
     * Lấy thống kê case theo trạng thái
     */
    List<Map<String, Object>> getCaseStatsByStatus(LocalDateTime startDate);

    /**
     * Lấy top khách hàng có nhiều case nhất
     */
    List<Map<String, Object>> getTopCustomersByCaseCount(LocalDateTime startDate, int limit);

    /**
     * Lấy top dịch vụ được sử dụng nhiều nhất
     */
    List<Map<String, Object>> getTopServicesByUsage(LocalDateTime startDate, int limit);

    /**
     * Lấy thống kê hiệu suất nhân viên
     */
    List<Map<String, Object>> getStaffPerformance(LocalDateTime startDate, int limit);

    /**
     * Lấy thống kê audit logs theo action
     */
    List<Map<String, Object>> getAuditStatsByAction(LocalDateTime startDate);

    /**
     * Lấy thống kê task theo trạng thái
     */
    List<Map<String, Object>> getTaskStatsByStatus(LocalDateTime startDate);

    /**
     * Lấy thống kê payment methods
     */
    List<Map<String, Object>> getPaymentStatsByMethod(LocalDateTime startDate);

    /**
     * Lấy dữ liệu dashboard real-time
     */
    Map<String, Object> getRealtimeDashboardData();

    /**
     * Lấy dashboard data cho một khoảng thời gian
     */
    Map<String, Object> getDashboardDataForPeriod(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Lấy dashboard data cho hôm nay
     */
    Map<String, Object> getTodayDashboardData();

    /**
     * Lấy dashboard data cho tuần này
     */
    Map<String, Object> getThisWeekDashboardData();

    /**
     * Lấy dashboard data cho tháng này
     */
    Map<String, Object> getThisMonthDashboardData();

    /**
     * Lấy dashboard data cho năm này
     */
    Map<String, Object> getThisYearDashboardData();

    // ===== RECEPTIONIST DASHBOARD METHODS =====

    /**
     * Lấy dữ liệu dashboard cho receptionist
     */
    Map<String, Object> getReceptionistDashboardData();

    /**
     * Lấy tóm tắt lịch hẹn hôm nay cho receptionist
     */
    Map<String, Object> getTodayAppointmentsSummary();

    /**
     * Lấy chi tiết lịch hẹn hôm nay cho receptionist
     */
    List<Map<String, Object>> getTodayAppointmentsDetail();

    /**
     * Lấy tasks cần làm hôm nay cho receptionist
     */
    List<Map<String, Object>> getTodayTasksForReceptionist();

    /**
     * Lấy customer cases cần xử lý hôm nay cho receptionist
     */
    List<Map<String, Object>> getTodayCasesForReceptionist();

    /**
     * Lấy hoạt động gần đây cho receptionist
     */
    List<Map<String, Object>> getRecentActivitiesForReceptionist();

    // ===== MANAGER DASHBOARD METHODS =====

    /**
     * Lấy dữ liệu dashboard cho manager
     */
    Map<String, Object> getManagerDashboardData();

    /**
     * Lấy tổng quan chi tiết cho manager
     */
    Map<String, Object> getManagerDashboardOverview();

    /**
     * Lấy analytics doanh thu cho manager
     */
    List<Map<String, Object>> getRevenueAnalyticsForManager(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Lấy performance của nhân viên cho manager
     */
    List<Map<String, Object>> getStaffPerformanceForManager(LocalDateTime startDate);

    /**
     * Lấy analytics khách hàng cho manager
     */
    List<Map<String, Object>> getCustomerAnalyticsForManager(LocalDateTime startDate);

    /**
     * Lấy performance của dịch vụ cho manager
     */
    List<Map<String, Object>> getServicePerformanceForManager(LocalDateTime startDate);

    /**
     * Lấy inventory alerts cho manager
     */
    List<Map<String, Object>> getInventoryAlertsForManager();

    /**
     * Lấy financial summary cho manager
     */
    Map<String, Object> getFinancialSummaryForManager(LocalDateTime startDate);

    /**
     * Lấy real-time data cho manager dashboard
     */
    Map<String, Object> getManagerRealtimeDashboardData();
}
