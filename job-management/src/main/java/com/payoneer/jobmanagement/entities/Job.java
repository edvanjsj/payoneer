package com.payoneer.jobmanagement.entities;

import lombok.Data;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @NotNull
    private LocalDateTime queuedAt;

    @NotNull
    private LocalDateTime scheduledTo;

    @NotNull
    @Positive
    private Integer priority = 1;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String message;


    // Job's configuration options
    @ElementCollection
    @CollectionTable(name = "job_params",
            joinColumns = {@JoinColumn(name = "job_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "param_name")
    @Column(name = "param_value")
    private Map<String, String> params;
}
