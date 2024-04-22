package com.avaki.cuBot.businessLogic;

import com.avaki.cuBot.exceptions.NotFoundException;
import com.avaki.cuBot.models.CuReceipt;
import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.services.ApartmentService;
import com.avaki.cuBot.services.CuReceiptService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Component
public class CuPaymentCalculator {

    @Autowired
    private CuReceiptService cuReceiptService;

    @Autowired
    private ApartmentService apartmentService;

    @Setter
    @Value("${additional_tenant_payment}")
    private BigDecimal additionalTenantPayment;

    private Set<Pair<Reading, BigDecimal>> newAndPreviousColdWaterReadingsSet;

    public synchronized BigDecimal calculate(@NotNull Set<Pair<Reading, BigDecimal>> newAndPreviousColdWaterReadingsSet) {
        this.newAndPreviousColdWaterReadingsSet = newAndPreviousColdWaterReadingsSet;

        BigDecimal newColdWaterReading = getNewReading(ReadingType.COLD);
        BigDecimal newHotWaterReading = getNewReading(ReadingType.HOT);
        BigDecimal newElectricityReading = getNewReading(ReadingType.ELECTRICITY);

        BigDecimal previousColdWaterReading = getPreviousReading(ReadingType.COLD);
        BigDecimal previousHotWaterReading = getPreviousReading(ReadingType.HOT);
        BigDecimal previousElectricityReading = getPreviousReading(ReadingType.ELECTRICITY);

        CuReceipt cuReceipt = cuReceiptService.findLast();

        BigDecimal tenantColdWaterConsumption = newColdWaterReading.subtract(previousColdWaterReading);
        BigDecimal tenantColdWaterPayment = tenantColdWaterConsumption.multiply(cuReceipt.getColdWaterRate());

        BigDecimal tenantHotWaterConsumption = newHotWaterReading.subtract(previousHotWaterReading);
        BigDecimal tenantHotWaterPayment = tenantHotWaterConsumption.multiply(cuReceipt.getHotWaterRate());

        BigDecimal tenantWaterDisposalConsumption = tenantColdWaterConsumption.add(tenantHotWaterConsumption);
        BigDecimal tenantWaterDisposalPayment = tenantWaterDisposalConsumption.multiply(cuReceipt.getDisposalWaterRate());

        BigDecimal tenantElectricityConsumption = newElectricityReading.subtract(previousElectricityReading);
        BigDecimal tenantElectricityPayment = tenantElectricityConsumption.multiply(cuReceipt.getElectricityDayRate());

        BigDecimal commonTenantsPayment = cuReceipt.getTotalCost()
                .subtract(cuReceipt.getElectricityCost())
                .subtract(cuReceipt.getColdWaterCost())
                .subtract(cuReceipt.getHotWaterCost())
                .subtract(cuReceipt.getWaterDisposalCost());

        BigDecimal countOfApartment = BigDecimal.valueOf(apartmentService.getApartmentCount());
        BigDecimal tenantPartOfCommonTenantsPayment = commonTenantsPayment.divide(countOfApartment, RoundingMode.CEILING);
        BigDecimal finalTenantPartOfCommonPayment = tenantPartOfCommonTenantsPayment.add(additionalTenantPayment);

        BigDecimal tenantPartOfInternetPayment = cuReceipt.getInternetPayment().divide(countOfApartment, RoundingMode.CEILING);

        return finalTenantPartOfCommonPayment
                .add(tenantColdWaterPayment)
                .add(tenantHotWaterPayment)
                .add(tenantWaterDisposalPayment)
                .add(tenantElectricityPayment)
                .add(tenantPartOfInternetPayment)
                .setScale(0, RoundingMode.CEILING);
    }

    private BigDecimal getNewReading(@NotNull ReadingType readingType) {
        return newAndPreviousColdWaterReadingsSet.stream()
                .filter(pair -> readingType.equals(pair.getFirst().getReadingType()))
                .map(pair -> pair.getFirst().getValue())
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("New reading with reading type %s can't be found while calculate tenantPayment", readingType)));
    }

    private BigDecimal getPreviousReading(@NotNull ReadingType readingType) {
        return newAndPreviousColdWaterReadingsSet.stream()
                .filter(pair -> readingType.equals(pair.getFirst().getReadingType()))
                .map(Pair::getSecond)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Previous reading with reading type %s can't be found while calculate tenantPayment", readingType)));
    }
}
