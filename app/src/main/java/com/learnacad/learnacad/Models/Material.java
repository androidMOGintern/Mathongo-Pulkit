package com.learnacad.learnacad.Models;

import java.io.Serializable;

/**
 * Created by Sahil Malhotra on 10-11-2017.
 */


public class Material implements Serializable{

    private Long id;

    String name;
    int minicourseId;

    public Material(String name, int minicourseId) {
        this.name = name;
        this.minicourseId = minicourseId;
        this.category_Level_II = "_";
        this.category_Level_I = "_";
    }

    public Material() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinicourseId() {
        return minicourseId;
    }

    String category_Level_I;
    String category_Level_II;

    public String getCategory_Level_I() {
        return category_Level_I;
    }

    public void setCategory_Level_I(String category_Level_I) {
        this.category_Level_I = category_Level_I;
    }

    public String getCategory_Level_II() {
        return category_Level_II;
    }

    public void setCategory_Level_II(String category_Level_II) {
        this.category_Level_II = category_Level_II;
    }

    public void setMinicourseId(int minicourseId) {
        this.minicourseId = minicourseId;
    }
}
