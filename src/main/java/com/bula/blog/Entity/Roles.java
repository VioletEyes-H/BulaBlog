package com.bula.blog.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tb_roles")
public class Roles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String auth;
    @ManyToMany(mappedBy = "rolesSet", fetch = FetchType.EAGER)
    private Set<Users> usersSet;

    public Roles() {
    }

    public Set<Users> getUsersSet() {
        return usersSet;
    }

    public void setUsersSet(Set<Users> usersSet) {
        this.usersSet = usersSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
