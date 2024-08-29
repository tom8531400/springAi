package com.example.springai01chat.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用戶債務表
 */
@Data
public class UserDebtVo {
    /**
     * 流水號
     */
    private Integer id;

    /**
     * 債務人編號
     */
    private String userId;

    /**
     * 年齡
     */
    private Integer age;

    /**
     * 總債務金額
     */
    private BigDecimal totalDebtAmount;

    /**
     * 每月總還款總額
     */
    private BigDecimal monthlyRepaymentAmount;

    /**
     * 貸款總數
     */
    private Integer totalLoans;

    /**
     * 目前手頭總資產
     */
    private BigDecimal totalAssets;

    /**
     * 每月薪資
     */
    private BigDecimal monthlySalary;

    /**
     * 信用評分
     */
    private Integer creditScore;

    /**
     * 添加日期
     */
    private LocalDate addedDate;

    /**
     * 修改日期
     */
    private LocalDateTime modifiedDate;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 備註
     */
    private String notes;
}
