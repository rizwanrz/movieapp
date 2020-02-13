package com.example.popularmovies.rizwan;

import java.io.Serializable;

public class EachVideo implements Serializable {

    private String videoName;
    private String videoKey;
    private String videoSite;
    private String videoId;
    private int size;

    public EachVideo(String videoId, String videoName, String videoKey, String videoSite, int size){
        this.videoId = videoId;
        this.size = size;
        this.videoKey = videoKey;
        this.videoName = videoName;
        this.videoSite = videoSite;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
