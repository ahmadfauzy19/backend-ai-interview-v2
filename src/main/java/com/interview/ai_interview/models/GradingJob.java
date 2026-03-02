package com.interview.ai_interview.models;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;



@Entity
@Table(
    name = "grading_jobs",
    indexes = {
        @Index(name = "idx_grading_status", columnList = "status"),
        @Index(name = "idx_grading_participant", columnList = "participant_id")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradingJob {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "participant_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_grading_job_participant")
    )
    private InterviewParticipant participant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GradingStatusEnum status;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
