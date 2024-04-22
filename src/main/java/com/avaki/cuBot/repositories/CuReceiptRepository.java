package com.avaki.cuBot.repositories;

import com.avaki.cuBot.models.CuReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuReceiptRepository extends JpaRepository<CuReceipt, Long> {

    Optional<CuReceipt> findTopByOrderByIdDesc();
}
