package com.ef.entity;

/**
 * Blocked ip address entity
 */
public class BlockedEntity {

    private Long id;
    private String ipAddress;
    private String reason;

    public BlockedEntity(String ipAddress, String reason) {
        this.ipAddress = ipAddress;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "BlockedEntity{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
