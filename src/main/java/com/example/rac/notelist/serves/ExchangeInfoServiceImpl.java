package com.example.rac.notelist.serves;

import com.example.rac.notelist.entity.ExchangeInfo;
import com.example.rac.notelist.repo.ExchangeInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExchangeInfoServiceImpl implements ExchangeInfoService {

    private final ExchangeInfoRepository exchangeInfoRepository;

    @Autowired
    public ExchangeInfoServiceImpl(ExchangeInfoRepository exchangeInfoRepository) {
        this.exchangeInfoRepository = exchangeInfoRepository;
    }

    @Override
    public List<ExchangeInfo> findByUserId(Long userId) {
        return exchangeInfoRepository.findByUserId(userId);
    }

    @Override
    public Optional<ExchangeInfo> getById(Long id) {
        return exchangeInfoRepository.findById(id);
    }

    @Override
    public ExchangeInfo add(ExchangeInfo exchangeInfo) {
        return exchangeInfoRepository.save(exchangeInfo);
    }

    @Override
    public void update(ExchangeInfo exchangeInfo) {
        exchangeInfoRepository.save(exchangeInfo);
    }

    @Override
    public void deleteById(Long id) {
        exchangeInfoRepository.deleteById(id);
    }
}