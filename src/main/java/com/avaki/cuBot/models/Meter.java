package com.avaki.cuBot.models;

import com.avaki.cuBot.models.enums.ReadingType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table (name = "meters")
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "number", updatable = false)
    private long number;

    @Column(name = "reading_type")
    @Enumerated(EnumType.STRING)
    private ReadingType readingType;

    @ManyToOne
    @JoinColumn(name = "apartment_id", referencedColumnName = "id")
    private Apartment apartment;

    @OneToMany(mappedBy = "meter")
    private List<Reading> readings;

    @Override
    public String toString() {
        return "Meter{" +
                "id=" + id +
                ", number=" + number +
                ", apartment=" + apartment +
                '}';
    }

    public long getNumber() {
        return number;
    }
}
