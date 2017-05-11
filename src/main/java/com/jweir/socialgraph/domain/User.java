package com.jweir.socialgraph.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.jweir.socialgraph.serialization.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

@Document
public class User {

    public static final class Friends extends HashSet<User> {}

    public static final class Connections extends ArrayList<Integer> {
        public Connections() {
            super(Collections.nCopies(4, 0));
        }
    }

    @Id
    @JsonView(View.Summary.class)
    private String id;

    @JsonView(View.Summary.class)
    private String name;

    @Transient
    private Connections connections;

    @Transient
    @JsonView(View.Summary.class)
    private Friends friends;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }
}
