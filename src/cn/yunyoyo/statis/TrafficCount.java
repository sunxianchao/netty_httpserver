package cn.yunyoyo.statis;

import java.util.Date;

public class TrafficCount {
    private String src_ip;
    private String uri;
    private Date date;
    private long sent_bytes;
    private long received_bytes;
    private long speed;

    public TrafficCount(String src_ip, String uri, Date date, long sent_bytes, long received_bytes, long speed) {
        this.src_ip = src_ip;
        this.uri = uri;
        this.date = date;
        this.sent_bytes = sent_bytes;
        this.received_bytes = received_bytes;
        this.speed = speed;
    }

    public TrafficCount() {
    }

    public String getSrc_ip() {
        return src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getSent_bytes() {
        return sent_bytes;
    }

    public void setSent_bytes(long sent_bytes) {
        this.sent_bytes = sent_bytes;
    }

    public long getReceived_bytes() {
        return received_bytes;
    }

    public void setReceived_bytes(long received_bytes) {
        this.received_bytes = received_bytes;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return src_ip + " " + uri + " " + date + " " + sent_bytes + " " + received_bytes + " " + speed;
    }
}
