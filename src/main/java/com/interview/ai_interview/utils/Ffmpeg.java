package com.interview.ai_interview.utils;

public class Ffmpeg {
    public static void extractAudio(String videoFilePath, String audioFilePath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg","-y", "-i", videoFilePath, "-q:a", "0", "-map", "a", audioFilePath  
        );
        pb.inheritIO();
        Process process = pb.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void cutAudio(
        String inputAudio,
        String outputAudio
    ) throws Exception {

        // String startStr = String.valueOf(start);
        // String durationStr = String.valueOf(end - start);

        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg", "-y",
            "-i", inputAudio,
            "-af", "afftdn",
            "-acodec", "pcm_s16le",
            outputAudio
        );

        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();
    }

}

