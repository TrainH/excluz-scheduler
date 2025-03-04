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
    public void createRevenue(RevenuePeriod revenuePeriod, LocalDateTime startDatetime, LocalDateTime endDatetime) {
        log.info("Create {} revenue", revenuePeriod.name());

        // 구매 / 환불
        List<TransactionType> transactionTypeList = List.of(TransactionType.PURCHASE, TransactionType.REFUND);

        // 포인트 거래내역 불러오기
        List<PointTransaction> pointTransactionList = pointTransactionRepository
                .findAllByDateRangeAndTransactionType(startDatetime, endDatetime, transactionTypeList);

        // 스토어 아이디별로 구매는 +, 환불은 - 해서 합산
        Map<Integer, Integer> revenueByStoreId = pointTransactionList.stream()
                .filter(pt -> pt.getStore() != null)
                .collect(Collectors.groupingBy(
                        pt -> pt.getStore().getId(),
                        Collectors.summingInt(pt ->
                                pt.getTransactionType() == TransactionType.PURCHASE ? pt.getAmount() : -pt.getAmount()
                        )
                ));

        // 리스트에 시간정보 넣기
        List<StoreRevenue> storeRevenueList = revenueByStoreId.entrySet().stream()
                .map(entry -> StoreRevenue.builder()
                        .storeId(entry.getKey())
                        .totalRevenue(entry.getValue().longValue())
                        .revenuePeriod(revenuePeriod)
                        .startDatetime(startDatetime)
                        .endDatetime(endDatetime)
                        .build()
                )
                .toList();

        storeRevenueRepository.saveAll(storeRevenueList);

        log.info("Finish {} revenue", revenuePeriod.name());
    }
}
