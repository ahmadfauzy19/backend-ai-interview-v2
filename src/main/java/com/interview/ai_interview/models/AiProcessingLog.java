package com.interview.ai_interview.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_processing_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiProcessingLog {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    private String sttStatus;

    private String scoringStatus;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime processedAt;
}