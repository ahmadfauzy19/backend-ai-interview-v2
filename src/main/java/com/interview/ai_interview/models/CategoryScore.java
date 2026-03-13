package com.interview.ai_interview.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "category_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryScore {

    @Id
    @GeneratedValue
    private UUID id;

    private String category;

    private Float score;
}