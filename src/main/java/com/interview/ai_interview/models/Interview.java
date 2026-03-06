package com.interview.ai_interview.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String context;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String objective;

    @Column(name = "role_target", nullable = false, columnDefinition = "TEXT")
    private String roleTarget;

    @Column(name = "level_target", nullable = false, columnDefinition = "TEXT")
    private String levelTarget;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String technology;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewMode purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    @Column(nullable = false)
    private UUID createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}
