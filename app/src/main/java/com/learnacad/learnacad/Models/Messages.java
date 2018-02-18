package com.learnacad.learnacad.Models;


import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

/**
 * Created by pulkit-mac on 25/01/18.
 */
@Table
public class Messages {
    private Long id;
    private String title;
    private String message;
    private Boolean seen;
    private String img_url;
    private String intent;
    private String process_id;
    Integer minicourse_id;
    String material_name;
    String category_level_II;
    String category_level_I;

    public Messages(String title, String message, Boolean seen) {
        this.title = title;
        this.message = message;
        this.seen = seen;
    }

    public Messages(String title, String message, Boolean seen, String img_url) {
        this.title = title;
        this.message = message;
        this.seen = seen;
        this.img_url = img_url;
    }



    public Messages() {

    }

    public Messages(String title, String message, Boolean seen, String img_url, String intent, String process_id, Integer minicourse_id, String material_name, String category_level_II, String category_level_I) {
        this.title = title;
        this.message = message;
        this.seen = seen;
        this.img_url = img_url;
        this.intent = intent;
        this.process_id = process_id;
        this.minicourse_id = minicourse_id;
        this.material_name = material_name;
        this.category_level_II = category_level_II;
        this.category_level_I = category_level_I;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getProcess_id() {
        return process_id;
    }

    public void setProcess_id(String process_id) {
        this.process_id = process_id;
    }

    public Integer getMinicourse_id() {
        return minicourse_id;
    }

    public void setMinicourse_id(Integer minicourse_id) {
        this.minicourse_id = minicourse_id;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getCategory_level_II() {
        return category_level_II;
    }

    public void setCategory_level_II(String category_level_II) {
        this.category_level_II = category_level_II;
    }

    public String getCategory_level_I() {
        return category_level_I;
    }

    public void setCategory_level_I(String category_level_I) {
        this.category_level_I = category_level_I;
    }
}
