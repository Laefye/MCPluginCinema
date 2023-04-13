package com.github.laefye.videoapi;

public class VideoInfo {
    public String url;
    public int duration;
    public String title;

    @Override
    public String toString() {
        return "VideoInfo{" +
                "url='" + url + '\'' +
                ", duration=" + duration +
                ", title='" + title + '\'' +
                '}';
    }

    public VideoInfo(String url, int duration, String title) {
        this.url = url;
        this.duration = duration;
        this.title = title;
    }

    public VideoInfo() {
        url = "";
        title = "";
        duration = 0;
    }
}
