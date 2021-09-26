package com.payoneer.jobmanagement.drivers.http;

import com.payoneer.jobmanagement.drivers.http.dto.JobDTO;
import com.payoneer.jobmanagement.entities.Job;
import com.payoneer.jobmanagement.services.api.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    public JobDTO save(@RequestBody JobDTO jobDTO) {
        Job job = jobService.create(jobDTO.toEntity());
        return JobDTO.of(job);
    }

    @GetMapping("/{id}")
    public JobDTO getOne(@PathVariable("id") @Positive Long id) {
        Job job = jobService.get(id);
        return JobDTO.of(job);
    }
}
