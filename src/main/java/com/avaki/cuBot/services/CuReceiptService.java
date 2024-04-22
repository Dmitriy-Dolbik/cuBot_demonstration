package com.avaki.cuBot.services;

import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.CuReceipt;
import com.avaki.cuBot.repositories.CuReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuReceiptService {

    @Autowired
    private CuReceiptRepository cuReceiptRepository;

    public CuReceipt findLast() {
        return cuReceiptRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new NotFoundException("The last row of cu receipt was not found in db"));
    }
}
