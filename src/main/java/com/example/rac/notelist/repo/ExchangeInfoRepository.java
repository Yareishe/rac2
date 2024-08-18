package com.example.rac.notelist.repo;

import com.example.rac.notelist.entity.ExchangeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeInfoRepository extends JpaRepository<ExchangeInfo, Long> {
    List<ExchangeInfo> findByUserId(Long userId);
}
