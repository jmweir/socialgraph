package com.jweir.socialgraph.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Connection {

    @Id
    private String id;

    @DBRef
    private User first;

    @DBRef
    private User second;

    public Connection() {
        this(null, null);
    }

    public Connection(User first, User second) {
        setFirst(first);
        setSecond(second);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFirst() {
        return first;
    }

    public void setFirst(User first) {
        this.first = first;
    }

    public User getSecond() {
        return second;
    }

    public void setSecond(User second) {
        this.second = second;
    }

}
