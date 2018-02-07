package com.learnacad.learnacad.Models;

import com.orm.dsl.Table;

/**
 * Created by Sahil Malhotra on 12-07-2017.
 */
@Table
public class SessionManager {

    public Long getId() {
        return id;
    }

    private Long id;

    String token;

    public SessionManager(){

    }

    public SessionManager(String token){

        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
