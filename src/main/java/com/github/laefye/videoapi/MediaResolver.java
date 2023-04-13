package com.github.laefye.videoapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;

public class MediaResolver {
    private String ffprobe = "";
    public ServicesRouter servicesRouter = new ServicesRouter();

    public MediaResolver(String ffprobe) {
        this.ffprobe = ffprobe;
    }

    private VideoInfo getDuration(VideoInfo info) {
        if (info == null) {
            return null;
        }
        try {
            Process p = Runtime.getRuntime().exec(new String[]{
                    ffprobe,
                    "-v",
                    "quiet",
                    "-print_format",
                    "json",
                    "-show_format",
                    info.url,

            });
            JsonObject format = null;
            try(BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                format = JsonParser.parseReader(input).getAsJsonObject();
                if (format.has("format")) {
                    format = format.get("format").getAsJsonObject();
                } else {
                    format = null;
                }
            }
            if (format == null) {
                return null;
            }
            info.duration = format.has("duration") ? (int) format.get("duration").getAsDouble() : -1;
            return info;
        } catch (IOException e) {
            return null;
        }
    }

    public VideoInfo getVideoFromDirect(URL url) {
        return getDuration(new VideoInfo(url.toString(), 0, ""));
    }

    public void getVideoFromDirect(URL url, Consumer<VideoInfo> callback) {
        new Thread(() -> callback.accept(getVideoFromDirect(url))).start();
    }
}
