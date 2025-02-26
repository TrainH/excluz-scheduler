package com.example.excluzscheduler.domain.store.storeRevenue.service;

import com.example.excluzscheduler.common.entity.PointTransaction;
import com.example.excluzscheduler.common.entity.StoreRevenue;
import org.springframework.transaction.annotation.Transactional;
import com.example.excluzscheduler.domain.point.pointTransaction.enums.TransactionType;
import com.example.excluzscheduler.domain.point.pointTransaction.repository.PointTransactionRepository;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRevenue.repository.StoreRevenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRevenueService {
    private final StoreRevenueRepository storeRevenueRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional
    public void createRevenue(RevenuePeriod revenuePeriod, LocalDateTime startDate, LocalDateTime endDate) {

        List<TransactionType> transactionTypeList = List.of(TransactionType.PURCHASE, TransactionType.REFUND);

        List<PointTransaction> pointTransactionList = pointTransactionRepository
                .findAllByDateRangeAndTransactionType(startDate, endDate, transactionTypeList);

        Map<Integer, Integer> revenueByStoreId = pointTransactionList.stream()
                .filter(pt -> pt.getStore() != null)
                .collect(Collectors.groupingBy(
                        pt -> pt.getStore().getId(),
                        Collectors.summingInt(pt ->
                                pt.getTransactionType() == TransactionType.PURCHASE ? pt.getAmount() : -pt.getAmount()
                        )
                ));

        List<StoreRevenue> storeRevenueList = revenueByStoreId.entrySet().stream()
                .map(entry -> StoreRevenue.builder()
                        .storeId(entry.getKey())
                        .totalRevenue(entry.getValue().longValue())
                        .revenuePeriod(revenuePeriod)
                        .startDate(startDate.toLocalDate())
                        .endDate(endDate.toLocalDate())
                        .build()
                )
                .toList();

        storeRevenueRepository.saveAll(storeRevenueList);
    }
}
