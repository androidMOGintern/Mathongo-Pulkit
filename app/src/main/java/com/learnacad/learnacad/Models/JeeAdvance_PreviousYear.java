package com.learnacad.learnacad.Models;

import com.orm.dsl.Table;

/**
 * Created by sahil on 26/12/17.
 */
@Table
public class JeeAdvance_PreviousYear {

    private Long id;
    String storedInDB;

    public JeeAdvance_PreviousYear() {
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