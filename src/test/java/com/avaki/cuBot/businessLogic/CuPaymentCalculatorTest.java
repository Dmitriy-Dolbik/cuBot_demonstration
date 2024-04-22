package com.avaki.cuBot.businessLogic;

import com.avaki.cuBot.models.CuReceipt;
import com.avaki.cuBot.models.Meter;
import com.avaki.cuBot.models.Reading;
import com.avaki.cuBot.models.enums.ReadingType;
import com.avaki.cuBot.services.ApartmentService;
import com.avaki.cuBot.services.CuReceiptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuPaymentCalculatorTest {
    private static final long COUNT_OF_APARTMENT = 4L;
    private static final BigDecimal COLD_WATER_RATE = BigDecimal.valueOf(36.54);
    private static final BigDecimal DISPOSAL_WATER_RATE = BigDecimal.valueOf(36.54);
    private static final BigDecimal HOT_WATER_RATE = BigDecimal.valueOf(126.68);
    private static final BigDecimal ELECTRICITY_DAY_RATE = BigDecimal.valueOf(6.51);
    private static final BigDecimal INTERNET_PAYMENT = BigDecimal.valueOf(600);

    @Mock
    private CuReceiptService cuReceiptService;

    @Mock
    private ApartmentService apartmentService;

    @InjectMocks
    private CuPaymentCalculator cuPaymentCalculator = new CuPaymentCalculator();

    @BeforeEach
    public void beforeEachTest() {
        cuPaymentCalculator.setAdditionalTenantPayment(BigDecimal.valueOf(400));
    }

    @ParameterizedTest
    @MethodSource("getCuReceiptData")
    public void calculateTest_shouldReturnCorrectValue1(BigDecimal totalReceiptCost, BigDecimal totalElectricityCost,
                                                        BigDecimal newColdWaterReading, BigDecimal newHotWaterReading, BigDecimal newElectricityReading,
                                                        BigDecimal previousColdWaterReading, BigDecimal previousHotWaterReading, BigDecimal previousElectricityReading,
                                                        BigDecimal totalColdWaterCost, BigDecimal totalHotWaterCost, BigDecimal totalWaterDisposalCost,
                                                        BigDecimal expectedTenantPayment) {
        //Given
        Pair<Reading, BigDecimal> newAndPreviousColdWaterReadings = Pair.of(new Reading(newColdWaterReading, new Meter(), ReadingType.COLD), previousColdWaterReading);
        Pair<Reading, BigDecimal> newAndPreviousHotWaterReadings = Pair.of(new Reading(newHotWaterReading, new Meter(), ReadingType.HOT), previousHotWaterReading);
        Pair<Reading, BigDecimal> newAndPreviousElectricityReadings = Pair.of(new Reading(newElectricityReading, new Meter(), ReadingType.ELECTRICITY),
                previousElectricityReading);

        CuReceipt cuReceipt = createCuReceipt(totalColdWaterCost, totalHotWaterCost, totalWaterDisposalCost, totalReceiptCost, totalElectricityCost);

        when(apartmentService.getApartmentCount()).thenReturn(COUNT_OF_APARTMENT);
        when(cuReceiptService.findLast()).thenReturn(cuReceipt);

        //When
        BigDecimal actualTenantPayment = cuPaymentCalculator.calculate(new HashSet<>(Arrays.asList(newAndPreviousColdWaterReadings, newAndPreviousHotWaterReadings,
                newAndPreviousElectricityReadings)));

        //Then
        Assertions.assertEquals(expectedTenantPayment, actualTenantPayment);
    }

    public static Stream<Arguments> getCuReceiptData() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(9389.95), BigDecimal.valueOf(1374.62),
                        BigDecimal.valueOf(220.869), BigDecimal.valueOf(230.065), BigDecimal.valueOf(1772.2),
                        BigDecimal.valueOf(219.128), BigDecimal.valueOf(229.505), BigDecimal.valueOf(1743.7),
                        BigDecimal.valueOf(438.48), BigDecimal.valueOf(1393.48), BigDecimal.valueOf(840.42),
                        BigDecimal.valueOf(2290)),
                Arguments.of(BigDecimal.valueOf(8857.77), BigDecimal.valueOf(1009.96),
                        BigDecimal.valueOf(95.030), BigDecimal.valueOf(87.446), BigDecimal.valueOf(4180.5),
                        BigDecimal.valueOf(92.658), BigDecimal.valueOf(80.062), BigDecimal.valueOf(4117.8),
                        BigDecimal.valueOf(214.64), BigDecimal.valueOf(891.32), BigDecimal.valueOf(471.74),
                        BigDecimal.valueOf(3905)));
    }

    private CuReceipt createCuReceipt(BigDecimal totalColdWaterCost, BigDecimal totalHotWaterCost, BigDecimal totalWaterDisposalCost, BigDecimal totalReceiptCost,
                                      BigDecimal totalElectricityCost) {
        return CuReceipt.builder()
                .totalCost(totalReceiptCost)
                .electricityCost(totalElectricityCost)
                .coldWaterRate(COLD_WATER_RATE)
                .hotWaterRate(HOT_WATER_RATE)
                .disposalWaterRate(DISPOSAL_WATER_RATE)
                .electricityDayRate(ELECTRICITY_DAY_RATE)
                .coldWaterCost(totalColdWaterCost)
                .hotWaterCost(totalHotWaterCost)
                .waterDisposalCost(totalWaterDisposalCost)
                .internetPayment(INTERNET_PAYMENT)
                .build();
    }
}