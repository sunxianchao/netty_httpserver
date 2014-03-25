package cn.yunyoyo.statis;

import java.util.Date;

public class IPRequests {
    private Long counter;
    private Date date;

    public IPRequests(Long counter, Date date) {
        this.counter = counter;
        this.date = date;
    }

    public IPRequests() {
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
