package com.avaki.cuBot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cu_receipt")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "electricity_cost")
    private BigDecimal electricityCost;

    @Column(name = "cold_water_rate")
    private BigDecimal coldWaterRate;

    @Column(name = "hot_water_rate")
    private BigDecimal hotWaterRate;

    @Column(name = "disposal_water_rate")
    private BigDecimal disposalWaterRate;

    @Column(name = "cold_water_cost")
    private BigDecimal coldWaterCost;

    @Column(name = "hot_water_cost")
    private BigDecimal hotWaterCost;

    @Column(name = "water_disposal_cost")
    private BigDecimal waterDisposalCost;

    @Column(name = "electricity_day_rate")
    private BigDecimal electricityDayRate;

    @Column(name = "internet_payment")
    private BigDecimal internetPayment;

    public static CuReceiptBuilder builder() {
        return new CuReceiptBuilder();
    }

    public static class CuReceiptBuilder {
        private Long id;
        private BigDecimal totalCost;
        private BigDecimal electricityCost;
        private BigDecimal coldWaterRate;
        private BigDecimal hotWaterRate;
        private BigDecimal disposalWaterRate;
        private BigDecimal coldWaterCost;
        private BigDecimal hotWaterCost;
        private BigDecimal waterDisposalCost;
        private BigDecimal electricityDayRate;
        private BigDecimal internetPayment;

        CuReceiptBuilder() {
        }

        public CuReceiptBuilder id(@NotNull Long id) {
            this.id = Objects.requireNonNull(id, "id is marked not null but is null");
            return this;
        }

        public CuReceiptBuilder totalCost(@NotNull BigDecimal totalCost) {
            this.totalCost = Objects.requireNonNull(totalCost, "total cost is marked not-null but is null");
            return this;
        }

        public CuReceiptBuilder electricityCost(@NotNull BigDecimal electricityCost) {
            this.electricityCost = Objects.requireNonNull(electricityCost, "electricityCost is marked not-null but is null");
            return this;
        }

        public CuReceiptBuilder coldWaterRate(@NotNull BigDecimal coldWaterRate) {
            this.coldWaterRate = Objects.requireNonNull(coldWaterRate);
            return this;
        }

        public CuReceiptBuilder hotWaterRate(@NotNull BigDecimal hotWaterRate) {
            this.hotWaterRate = Objects.requireNonNull(hotWaterRate);
            return this;
        }

        public CuReceiptBuilder disposalWaterRate(@NotNull BigDecimal disposalWaterRate) {
            this.disposalWaterRate = Objects.requireNonNull(disposalWaterRate);
            return this;
        }

        public CuReceiptBuilder coldWaterCost(@NotNull BigDecimal coldWaterCost) {
            this.coldWaterCost = Objects.requireNonNull(coldWaterCost);
            return this;
        }

        public CuReceiptBuilder hotWaterCost(@NotNull BigDecimal hotWaterCost) {
            this.hotWaterCost = Objects.requireNonNull(hotWaterCost);
            return this;
        }

        public CuReceiptBuilder waterDisposalCost(@NotNull BigDecimal waterDisposalCost) {
            this.waterDisposalCost = Objects.requireNonNull(waterDisposalCost);
            return this;
        }

        public CuReceiptBuilder electricityDayRate(@NotNull BigDecimal electricityDayRate) {
            this.electricityDayRate = Objects.requireNonNull(electricityDayRate);
            return this;
        }

        public CuReceiptBuilder internetPayment(@NotNull BigDecimal internetPayment) {
            this.internetPayment = Objects.requireNonNull(internetPayment);
            return this;
        }

        public CuReceipt build() {
            return new CuReceipt(id, totalCost, electricityCost, coldWaterRate, hotWaterRate, disposalWaterRate, coldWaterCost, hotWaterCost, waterDisposalCost, electricityDayRate, internetPayment);
        }
    }
}
