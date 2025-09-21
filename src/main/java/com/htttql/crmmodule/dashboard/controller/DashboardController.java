package com.htttql.crmmodule.dashboard.controller;

import com.htttql.crmmodule.common.dto.ApiResponse;
import com.htttql.crmmodule.dashboard.service.IDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Dashboard Controller - REST API cho dashboard analytics
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard Management", description = "API cho dashboard analytics và báo cáo")
public class DashboardController {

    private final IDashboardService dashboardService;

    /**
     * Lấy tổng quan dashboard
     */
    @GetMapping("/overview")
    @Operation(summary = "Lấy tổng quan dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardOverview() {
        log.info("Getting dashboard overview");
        Map<String, Object> overview = dashboardService.getDashboardOverview();
        return ResponseEntity.ok(ApiResponse.success(overview, "Dashboard overview retrieved successfully"));
    }

    /**
     * Lấy dữ liệu dashboard real-time
     */
    @GetMapping("/realtime")
    @Operation(summary = "Lấy dữ liệu dashboard real-time")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRealtimeDashboardData() {
        log.info("Getting real-time dashboard data");
        Map<String, Object> realtimeData = dashboardService.getRealtimeDashboardData();
        return ResponseEntity.ok(ApiResponse.success(realtimeData, "Real-time dashboard data retrieved successfully"));
    }

    /**
     * Lấy dữ liệu dashboard cho hôm nay
     */
    @GetMapping("/today")
    @Operation(summary = "Lấy dữ liệu dashboard cho hôm nay")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTodayDashboardData() {
        log.info("Getting today's dashboard data");
        Map<String, Object> todayData = dashboardService.getTodayDashboardData();
        return ResponseEntity.ok(ApiResponse.success(todayData, "Today's dashboard data retrieved successfully"));
    }

    /**
     * Lấy dữ liệu dashboard cho tuần này
     */
    @GetMapping("/this-week")
    @Operation(summary = "Lấy dữ liệu dashboard cho tuần này")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getThisWeekDashboardData() {
        log.info("Getting this week's dashboard data");
        Map<String, Object> weekData = dashboardService.getThisWeekDashboardData();
        return ResponseEntity.ok(ApiResponse.success(weekData, "This week's dashboard data retrieved successfully"));
    }

    /**
     * Lấy dữ liệu dashboard cho tháng này
     */
    @GetMapping("/this-month")
    @Operation(summary = "Lấy dữ liệu dashboard cho tháng này")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getThisMonthDashboardData() {
        log.info("Getting this month's dashboard data");
        Map<String, Object> monthData = dashboardService.getThisMonthDashboardData();
        return ResponseEntity.ok(ApiResponse.success(monthData, "This month's dashboard data retrieved successfully"));
    }

    /**
     * Lấy dữ liệu dashboard cho năm này
     */
    @GetMapping("/this-year")
    @Operation(summary = "Lấy dữ liệu dashboard cho năm này")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'RECEPTIONIST', 'TECHNICIAN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getThisYearDashboardData() {
        log.info("Getting this year's dashboard data");
        Map<String, Object> yearData = dashboardService.getThisYearDashboardData();
        return ResponseEntity.ok(ApiResponse.success(yearData, "This year's dashboard data retrieved successfully"));
    }

    /**
     * Lấy dữ liệu dashboard cho khoảng thời gian tùy chỉnh
     */
    @GetMapping("/period")
    @Operation(summary = "Lấy dữ liệu dashboard cho khoảng thời gian tùy chỉnh")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardDataForPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Getting dashboard data for period: {} to {}", startDate, endDate);
        Map<String, Object> periodData = dashboardService.getDashboardDataForPeriod(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(periodData, "Dashboard data for period retrieved successfully"));
    }

    /**
     * Lấy thống kê khách hàng theo tháng
     */
    @GetMapping("/customer-stats")
    @Operation(summary = "Lấy thống kê khách hàng theo tháng")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getCustomerStatsByMonth(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(12);
        log.info("Getting customer stats by month from: {}", actualStartDate);

        java.util.List<Map<String, Object>> stats = dashboardService.getCustomerStatsByMonth(actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(stats, "Customer stats by month retrieved successfully"));
    }

    /**
     * Lấy thống kê doanh thu theo tháng
     */
    @GetMapping("/revenue-stats")
    @Operation(summary = "Lấy thống kê doanh thu theo tháng")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getRevenueStatsByMonth(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(12);
        log.info("Getting revenue stats by month from: {}", actualStartDate);

        java.util.List<Map<String, Object>> stats = dashboardService.getRevenueStatsByMonth(actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(stats, "Revenue stats by month retrieved successfully"));
    }

    /**
     * Lấy top khách hàng
     */
    @GetMapping("/top-customers")
    @Operation(summary = "Lấy top khách hàng")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(6);
        log.info("Getting top customers (limit: {}, from: {})", limit, actualStartDate);

        java.util.List<Map<String, Object>> topCustomers = dashboardService.getTopCustomersByCaseCount(actualStartDate, limit);
        return ResponseEntity.ok(ApiResponse.success(topCustomers, "Top customers retrieved successfully"));
    }

    /**
     * Lấy top dịch vụ
     */
    @GetMapping("/top-services")
    @Operation(summary = "Lấy top dịch vụ")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getTopServices(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(6);
        log.info("Getting top services (limit: {}, from: {})", limit, actualStartDate);

        java.util.List<Map<String, Object>> topServices = dashboardService.getTopServicesByUsage(actualStartDate, limit);
        return ResponseEntity.ok(ApiResponse.success(topServices, "Top services retrieved successfully"));
    }

    /**
     * Lấy hiệu suất nhân viên
     */
    @GetMapping("/staff-performance")
    @Operation(summary = "Lấy hiệu suất nhân viên")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getStaffPerformance(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(6);
        log.info("Getting staff performance (limit: {}, from: {})", limit, actualStartDate);

        java.util.List<Map<String, Object>> performance = dashboardService.getStaffPerformance(actualStartDate, limit);
        return ResponseEntity.ok(ApiResponse.success(performance, "Staff performance retrieved successfully"));
    }

    // ===== RECEPTIONIST DASHBOARD ENDPOINTS =====

    /**
     * Lấy dữ liệu dashboard cho receptionist
     */
    @GetMapping("/receptionist")
    @Operation(summary = "Lấy dữ liệu dashboard cho receptionist")
    @PreAuthorize("hasAnyRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReceptionistDashboard() {
        log.info("Getting receptionist dashboard data");
        Map<String, Object> data = dashboardService.getReceptionistDashboardData();
        return ResponseEntity.ok(ApiResponse.success(data, "Receptionist dashboard data retrieved successfully"));
    }

    /**
     * Lấy tóm tắt lịch hẹn hôm nay cho receptionist
     */
    @GetMapping("/receptionist/appointments/summary")
    @Operation(summary = "Lấy tóm tắt lịch hẹn hôm nay cho receptionist")
    @PreAuthorize("hasAnyRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTodayAppointmentsSummary() {
        log.info("Getting today's appointments summary for receptionist");
        Map<String, Object> summary = dashboardService.getTodayAppointmentsSummary();
        return ResponseEntity.ok(ApiResponse.success(summary, "Today's appointments summary retrieved successfully"));
    }

    /**
     * Lấy chi tiết lịch hẹn hôm nay cho receptionist
     */
    @GetMapping("/receptionist/appointments/detail")
    @Operation(summary = "Lấy chi tiết lịch hẹn hôm nay cho receptionist")
    @PreAuthorize("hasAnyRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getTodayAppointmentsDetail() {
        log.info("Getting today's appointments detail for receptionist");
        java.util.List<Map<String, Object>> detail = dashboardService.getTodayAppointmentsDetail();
        return ResponseEntity.ok(ApiResponse.success(detail, "Today's appointments detail retrieved successfully"));
    }

    /**
     * Lấy tasks cần làm hôm nay cho receptionist
     */
    @GetMapping("/receptionist/tasks/today")
    @Operation(summary = "Lấy tasks cần làm hôm nay cho receptionist")
    @PreAuthorize("hasAnyRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getTodayTasksForReceptionist() {
        log.info("Getting today's tasks for receptionist");
        java.util.List<Map<String, Object>> tasks = dashboardService.getTodayTasksForReceptionist();
        return ResponseEntity.ok(ApiResponse.success(tasks, "Today's tasks retrieved successfully"));
    }

    /**
     * Lấy customer cases cần xử lý hôm nay cho receptionist
     */
    @GetMapping("/receptionist/cases/today")
    @Operation(summary = "Lấy customer cases cần xử lý hôm nay cho receptionist")
    @PreAuthorize("hasAnyRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getTodayCasesForReceptionist() {
        log.info("Getting today's cases for receptionist");
        java.util.List<Map<String, Object>> cases = dashboardService.getTodayCasesForReceptionist();
        return ResponseEntity.ok(ApiResponse.success(cases, "Today's cases retrieved successfully"));
    }

    /**
     * Lấy hoạt động gần đây cho receptionist
     */
    @GetMapping("/receptionist/activities/recent")
    @Operation(summary = "Lấy hoạt động gần đây cho receptionist")
    @PreAuthorize("hasAnyRole('RECEPTIONIST')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getRecentActivitiesForReceptionist() {
        log.info("Getting recent activities for receptionist");
        java.util.List<Map<String, Object>> activities = dashboardService.getRecentActivitiesForReceptionist();
        return ResponseEntity.ok(ApiResponse.success(activities, "Recent activities retrieved successfully"));
    }

    // ===== MANAGER DASHBOARD ENDPOINTS =====

    /**
     * Lấy dữ liệu dashboard cho manager
     */
    @GetMapping("/manager")
    @Operation(summary = "Lấy dữ liệu dashboard cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getManagerDashboard() {
        log.info("Getting manager dashboard data");
        Map<String, Object> data = dashboardService.getManagerDashboardData();
        return ResponseEntity.ok(ApiResponse.success(data, "Manager dashboard data retrieved successfully"));
    }

    /**
     * Lấy tổng quan chi tiết cho manager
     */
    @GetMapping("/manager/overview")
    @Operation(summary = "Lấy tổng quan chi tiết cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getManagerDashboardOverview() {
        log.info("Getting manager dashboard overview");
        Map<String, Object> overview = dashboardService.getManagerDashboardOverview();
        return ResponseEntity.ok(ApiResponse.success(overview, "Manager dashboard overview retrieved successfully"));
    }

    /**
     * Lấy analytics doanh thu cho manager
     */
    @GetMapping("/manager/revenue-analytics")
    @Operation(summary = "Lấy analytics doanh thu cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getRevenueAnalyticsForManager(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusDays(30);
        LocalDateTime actualEndDate = endDate != null ? endDate : LocalDateTime.now();

        log.info("Getting revenue analytics for manager: {} to {}", actualStartDate, actualEndDate);
        java.util.List<Map<String, Object>> analytics = dashboardService.getRevenueAnalyticsForManager(actualStartDate, actualEndDate);
        return ResponseEntity.ok(ApiResponse.success(analytics, "Revenue analytics retrieved successfully"));
    }

    /**
     * Lấy performance của nhân viên cho manager
     */
    @GetMapping("/manager/staff-performance")
    @Operation(summary = "Lấy performance của nhân viên cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getStaffPerformanceForManager(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusDays(30);
        log.info("Getting staff performance for manager from: {}", actualStartDate);

        java.util.List<Map<String, Object>> performance = dashboardService.getStaffPerformanceForManager(actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(performance, "Staff performance retrieved successfully"));
    }

    /**
     * Lấy analytics khách hàng cho manager
     */
    @GetMapping("/manager/customer-analytics")
    @Operation(summary = "Lấy analytics khách hàng cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getCustomerAnalyticsForManager(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusMonths(6);
        log.info("Getting customer analytics for manager from: {}", actualStartDate);

        java.util.List<Map<String, Object>> analytics = dashboardService.getCustomerAnalyticsForManager(actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(analytics, "Customer analytics retrieved successfully"));
    }

    /**
     * Lấy performance của dịch vụ cho manager
     */
    @GetMapping("/manager/service-performance")
    @Operation(summary = "Lấy performance của dịch vụ cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getServicePerformanceForManager(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusDays(30);
        log.info("Getting service performance for manager from: {}", actualStartDate);

        java.util.List<Map<String, Object>> performance = dashboardService.getServicePerformanceForManager(actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(performance, "Service performance retrieved successfully"));
    }

    /**
     * Lấy inventory alerts cho manager
     */
    @GetMapping("/manager/inventory-alerts")
    @Operation(summary = "Lấy inventory alerts cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<java.util.List<Map<String, Object>>>> getInventoryAlertsForManager() {
        log.info("Getting inventory alerts for manager");
        java.util.List<Map<String, Object>> alerts = dashboardService.getInventoryAlertsForManager();
        return ResponseEntity.ok(ApiResponse.success(alerts, "Inventory alerts retrieved successfully"));
    }

    /**
     * Lấy financial summary cho manager
     */
    @GetMapping("/manager/financial-summary")
    @Operation(summary = "Lấy financial summary cho manager")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFinancialSummaryForManager(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {

        LocalDateTime actualStartDate = startDate != null ? startDate : LocalDateTime.now().minusDays(30);
        log.info("Getting financial summary for manager from: {}", actualStartDate);

        Map<String, Object> summary = dashboardService.getFinancialSummaryForManager(actualStartDate);
        return ResponseEntity.ok(ApiResponse.success(summary, "Financial summary retrieved successfully"));
    }

    /**
     * Lấy real-time data cho manager dashboard
     */
    @GetMapping("/manager/realtime")
    @Operation(summary = "Lấy real-time data cho manager dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getManagerRealtimeDashboardData() {
        log.info("Getting manager real-time dashboard data");
        Map<String, Object> realtimeData = dashboardService.getManagerRealtimeDashboardData();
        return ResponseEntity.ok(ApiResponse.success(realtimeData, "Manager real-time dashboard data retrieved successfully"));
    }
}
