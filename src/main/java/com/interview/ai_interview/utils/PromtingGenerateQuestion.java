package com.interview.ai_interview.utils;

import com.interview.ai_interview.dto.request.GenerateQuestionRequest;
import org.springframework.stereotype.Component;

@Component
public class PromtingGenerateQuestion {

    private static final String SYSTEM_PROMPT =
            "Anda adalah AI profesional pembuat pertanyaan interview teknis. " +
            "Selalu keluarkan response dalam format JSON valid tanpa markdown.";

    public String buildPrompt(GenerateQuestionRequest body) {

        return """
                Buat %d pertanyaan interview berdasarkan detail berikut:
                
                Nama Interview: %s
                Role Target: %s
                Level Target: %s
                Technology: %s
                Tujuan Interview: %s
                
                Konteks Tambahan:
                %s
                
                Aturan:
                - Pertanyaan maksimal 30 kata
                - Fokus utama pada teknis dan problem solving
                - Sedikit soft skill (maksimal 20%% dari total pertanyaan)
                - Gunakan bahasa profesional
                
                Output WAJIB dalam format JSON valid seperti ini:
                
                {
                  "description": "string",
                  "questions": [
                    { "question": "string" }
                  ]
                }
                
                Jangan tambahkan markdown.
                Jangan tambahkan komentar.
                """.formatted(
                body.getNumber(),
                body.getName(),
                body.getRoleTarget(),
                body.getLevelTarget(),
                body.getTechnology(),
                body.getObjective(),
                body.getContext()
        );
    }

    public String getSystemPrompt() {
        return SYSTEM_PROMPT;
    }
}