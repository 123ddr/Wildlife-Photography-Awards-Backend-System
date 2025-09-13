package com.wildlifebackend.wildlife.service;


import com.wildlifebackend.wildlife.repository.OpenSubmissionRepository;
import com.wildlifebackend.wildlife.repository.SchoolSubmissionRepositry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubmissionSummaryService {

    private final OpenSubmissionRepository openSubmissionRepository;
    private final SchoolSubmissionRepositry schoolSubmissionRepository;

    public long getTotalSubmissions() {
        long openCount = openSubmissionRepository.count();
        long schoolCount = schoolSubmissionRepository.count();
        return openCount + schoolCount;
    }

    private static final List<String> CATEGORIES = List.of("LANDSCAPE", "PORTRAIT", "WILDLIFE", "STREET", "MACRO");

    // Open categories
    public Map<String, Long> getOpenCategoryTotals() {
        Map<String, Long> openCategoryCounts = new HashMap<>();
        for (String category : CATEGORIES) {
            openCategoryCounts.put(category, openSubmissionRepository.countByEntryCategory(category));
        }
        return openCategoryCounts;
    }

    // School categories
    public Map<String, Long> getSchoolCategoryTotals() {
        Map<String, Long> schoolCategoryCounts = new HashMap<>();
        for (String category : CATEGORIES) {
            schoolCategoryCounts.put(category, schoolSubmissionRepository.countByEntryCategory(category));
        }
        return schoolCategoryCounts;
    }
}
