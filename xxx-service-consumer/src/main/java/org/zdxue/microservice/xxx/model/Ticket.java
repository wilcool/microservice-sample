package org.zdxue.microservice.xxx.model;

import java.io.Serializable;

/**
 * @author xuezd
 */
public class Ticket implements Serializable {
    private static final long serialVersionUID = 3019565335319029490L;

    private int id;
    private int userId;
    private String code;

    private User owner;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

}
