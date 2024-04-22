package com.avaki.cuBot.models;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private byte number;

    @Column(name = "cleaning_date")
    private LocalDateTime cleaningDate;

    @OneToMany(mappedBy = "apartment")
    private List<Meter> meters;

    @OneToMany(mappedBy = "apartment")
    private List<User> users;

    @OneToOne(mappedBy = "apartment")
    private WiFiCredentials wiFiCredentials;

    @Override
    public String toString() {
        return "Apartment{" +
                "number=" + number +
                '}';
    }

    public List<User> getUsers() {
        return users;
    }

    public WiFiCredentials getWiFiCredentials() {
        return wiFiCredentials;
    }

    public LocalDateTime getCleaningDate() {
        return cleaningDate;
    }

    public void setCleaningDate(LocalDateTime cleaningDate) {
        this.cleaningDate = cleaningDate;
    }
}