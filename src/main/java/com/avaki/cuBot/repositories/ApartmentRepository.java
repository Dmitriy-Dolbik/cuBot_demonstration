package com.avaki.cuBot.repositories;

import com.avaki.cuBot.models.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Byte> {

    @Query(value = "SELECT DISTINCT ap FROM Apartment ap LEFT JOIN FETCH ap.users")
    List<Apartment> findAllWithUsers();
}