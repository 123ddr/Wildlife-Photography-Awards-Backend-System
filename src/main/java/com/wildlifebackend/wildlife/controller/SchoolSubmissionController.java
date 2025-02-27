package com.wildlifebackend.wildlife.controller;


import com.wildlifebackend.wildlife.dto.response.StudentSubmissionDTO;
import com.wildlifebackend.wildlife.entitiy.SchoolSubmission;
import com.wildlifebackend.wildlife.service.SchoolSubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/schoolsubmission")
public class SchoolSubmissionController {

   private final SchoolSubmissionService schoolSubmissionService;

   public SchoolSubmissionController(SchoolSubmissionService schoolSubmissionService) {
       this.schoolSubmissionService = schoolSubmissionService;
   }

   @PostMapping("/createSchoolSubmission")
    public ResponseEntity<SchoolSubmission> submitSchoolEntry(@ModelAttribute StudentSubmissionDTO studentSubmissionDTO){
       SchoolSubmission schoolSubmission=schoolSubmissionService.saveSchoolSubmission(studentSubmissionDTO);
       return ResponseEntity.ok(schoolSubmission);
   }
}
