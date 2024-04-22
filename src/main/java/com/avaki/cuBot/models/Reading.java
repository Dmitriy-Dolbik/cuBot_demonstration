package com.avaki.cuBot.models;

import com.avaki.cuBot.models.enums.ReadingType;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "readings")
@NoArgsConstructor
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "reading_type")
    @Enumerated(EnumType.STRING)
    private ReadingType readingType;

    @ManyToOne
    @JoinColumn(name = "meter_id", referencedColumnName = "id", nullable = false)
    private Meter meter;

    public Reading(BigDecimal value, Meter meter, ReadingType readingType) {
        this.value = value;
        this.meter = meter;
        this.readingType = readingType;
    }

    @PrePersist
    private void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public BigDecimal getValue() {
        if (ReadingType.ELECTRICITY.equals(readingType)) {
            value = value.setScale(1, RoundingMode.HALF_UP);//у показаний электричества только один знак после запятой,
            // но т.к. он в общей таблице показаний, то присутствует три знака, два из которых нули.
        }
        return value;
    }

    public Meter getMeter() {
        return meter;
    }

    public ReadingType getReadingType() {
        return readingType;
    }

    @Override
    public String toString() {
        return "Reading{" +
                "value=" + value +
                ", meter=" + meter +
                '}';
    }
}
