package com.avaki.cuBot.services;

import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.Apartment;
import com.avaki.cuBot.repositories.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    public long getApartmentCount() {
        return apartmentRepository.count();
    }

    public Apartment findById(byte apartmentId) {
        return apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Apartment with id %s cannot be found in DB", apartmentId)));
    }

    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    public List<Apartment> findAllWithUsers() {
        return apartmentRepository.findAllWithUsers();
    }

    public void save(Apartment apartment) {
        apartmentRepository.save(apartment);
    }
}
