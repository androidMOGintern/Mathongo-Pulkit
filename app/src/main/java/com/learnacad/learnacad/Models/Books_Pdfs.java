package com.learnacad.learnacad.Models;

import com.orm.dsl.Table;

/**
 * Created by sahil on 29/12/17.
 */

@Table
public class Books_Pdfs {

    private Long id;
    String storedInDB;

    public Books_Pdfs() {
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
