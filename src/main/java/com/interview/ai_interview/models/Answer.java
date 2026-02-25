package com.interview.ai_interview.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "answers",
    uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "question_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private InterviewParticipant participant;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private String audioPath;

    @Column(columnDefinition = "TEXT")
    private String transcript;

    private Float score;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}