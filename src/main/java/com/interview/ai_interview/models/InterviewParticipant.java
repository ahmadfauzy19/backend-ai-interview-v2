package com.interview.ai_interview.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "interview_participants",
    uniqueConstraints = @UniqueConstraint(columnNames = {"interview_id", "candidate_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewParticipant {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    private Float totalScore;

    private String recommendation;

    @Column(columnDefinition = "TEXT")
    private String summaryReason;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;
}