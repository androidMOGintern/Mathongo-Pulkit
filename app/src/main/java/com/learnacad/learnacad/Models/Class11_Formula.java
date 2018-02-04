package com.learnacad.learnacad.Models;

import com.orm.dsl.Table;

/**
 * Created by sahil on 26/12/17.
 */

@Table
public class Class11_Formula {

    private Long id;
    String storedInDB;

    public Class11_Formula() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoredInDB() {
        return storedInDB;
    }

    public void setStoredInDB(String storedInDB) {
        this.storedInDB = storedInDB;
    }
}
