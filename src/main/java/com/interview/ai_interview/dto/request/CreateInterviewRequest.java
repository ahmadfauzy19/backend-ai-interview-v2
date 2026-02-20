package com.interview.ai_interview.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.interview.ai_interview.models.InterviewMode;
import java.util.UUID;

@Data
public class CreateInterviewRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String objective;

    @NotNull
    private InterviewMode mode;

    @NotNull
    private UUID createdBy;

    @NotBlank
    private String role_target;

    @NotBlank
    private String level_target;
}
