package com.example.rac.notelist.serves;

import com.example.rac.notelist.entity.ExchangeInfo;
import com.example.rac.notelist.entity.User;

import java.util.List;
import java.util.Optional;

public interface ExchangeInfoService {
    List<ExchangeInfo> findByUserId(Long userId);
    Optional<ExchangeInfo> getById(Long id);
    ExchangeInfo add(ExchangeInfo exchangeInfo);
    void update(ExchangeInfo exchangeInfo);
    void deleteById(Long id);
}