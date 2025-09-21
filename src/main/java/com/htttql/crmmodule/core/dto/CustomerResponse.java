package com.htttql.crmmodule.core.dto;

import com.htttql.crmmodule.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Customer Response DTO - Simplified for basic customer information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long customerId;
    private String fullName;
    private String phone;
    private String email;
    private LocalDate dob;
    private Gender gender;
    private String displayAddress;
    private String tierCode;
    private String tierName;
    private Integer totalPoints;
    private BigDecimal totalSpent;
    private String notes;
    private Boolean isVip;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Computed fields for display
    public String getDisplayName() {
        return fullName != null ? fullName : "Unknown";
    }

    public String getDisplayPhone() {
        return phone != null ? phone : "N/A";
    }

    public String getDisplayEmail() {
        return email != null ? email : "N/A";
    }

    public String getDisplayGender() {
        return gender != null ? gender.name() : "N/A";
    }

    public String getDisplayTier() {
        return tierName != null ? tierName : "N/A";
    }

    public String getDisplayAddress() {
        return displayAddress != null ? displayAddress : "N/A";
    }
}