package com.example.springai01chat.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DebtVo {
    /**
     * 流水號
     */
    private Integer id;

    /**
     * 債務人姓名
     */
    private String debtorName;

    /**
     * 債務金額
     */
    private BigDecimal amount;

    /**
     * 債務種類
     */
    private String type;

    /**
     * 貨幣單位
     */
    private String currency;

    /**
     * 利息金額
     */
    private BigDecimal interestAmount;

    /**
     * 利息利率
     */
    private BigDecimal interestRate;

    /**
     * 債務起始時間
     */
    private LocalDate startDate;

    /**
     * 債務結束時間
     */
    private LocalDate endDate;

    /**
     * 添加時間
     */
    private LocalDateTime createdAt;

    /**
     * 更改時間
     */
    private LocalDateTime updatedAt;

    /**
     * 更改人
     */
    private String updatedBy;

    /**
     * 債務狀態
     */
    private String status;

    /**
     * 償還頻率
     */
    private String paymentFrequency;

    /**
     * 下一次償還日期
     */
    private LocalDate nextPaymentDate;

    /**
     * 附註
     */
    private String notes;

}
