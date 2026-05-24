package com.autoinsight.autoinsight_api.service;

import com.autoinsight.autoinsight_api.dto.SearchHistoryResponseDTO;
import com.autoinsight.autoinsight_api.model.SearchHistory;
import com.autoinsight.autoinsight_api.repository.SearchHistoryRepository;
import com.autoinsight.autoinsight_api.security.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final CryptoUtils cryptoUtils;

    public void save(String brand, String model, String version) {
        SearchHistory history = SearchHistory.builder()
                .brand(cryptoUtils.encrypt(brand))
                .model(cryptoUtils.encrypt(model))
                .version(cryptoUtils.encrypt(version))
                .build();
        searchHistoryRepository.save(history);
    }

    public List<SearchHistoryResponseDTO> findRecent() {
        return searchHistoryRepository.findTop10ByOrderBySearchedAtDesc()
                .stream()
                .map(h -> SearchHistoryResponseDTO.builder()
                        .id(h.getId())
                        .brand(cryptoUtils.decrypt(h.getBrand()))
                        .model(cryptoUtils.decrypt(h.getModel()))
                        .version(cryptoUtils.decrypt(h.getVersion()))
                        .searchedAt(h.getSearchedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        searchHistoryRepository.deleteAll();
    }
}