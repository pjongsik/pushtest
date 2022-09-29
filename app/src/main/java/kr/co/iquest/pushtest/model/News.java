package kr.co.iquest.pushtest.model;

public class News {

    public News(String title, String url, String from, String time) {
        this.title = title;
        this.url = url;
        this.from = from;
        this.time = time;
    }

    public String title;
    public String url;
    public String from;
    public String time;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%s : %s [%s] - %s", title, from, time, url);
    }


}
