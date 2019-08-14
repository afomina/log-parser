package com.ef.entity;

import java.util.Date;

/**
 * Parsed access log entity
 */
public class LogEntity {

    private Long id;
    private Date date;
    private String ipAddress;
    private String request;
    private Integer status;
    private String userAgent;

    public LogEntity(Date date, String ipAddress, String request, Integer status, String userAgent) {
        this.date = date;
        this.ipAddress = ipAddress;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "date=" + date +
                ", ipAddress='" + ipAddress + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }

}
