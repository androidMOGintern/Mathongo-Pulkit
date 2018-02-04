package com.learnacad.learnacad.Models;

import com.orm.dsl.Table;

import java.util.HashMap;

/**
 * Created by Sahil Malhotra on 17-07-2017.
 */
@Table
public class Student {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String mobileNum;
    int studentId;
    private String usedReferalCode = "NONE";

    public String getUsedReferalCode() {
        return usedReferalCode;
    }

    public void setUsedReferalCode(String usedReferalCode) {
        this.usedReferalCode = usedReferalCode;
    }

    private String myReferalCode;

    public String getMyReferalCode() {
        return myReferalCode;
    }

    public void setMyReferalCode(String myReferalCode) {
        this.myReferalCode = myReferalCode;
    }

    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
    }

    public int getStudentId() {

        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public HashMap<Integer,Boolean> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void initializeEnrolledMap(){

        this.enrolledCourses = new HashMap<>();
    }

    HashMap<Integer,Boolean> enrolledCourses;

    public Student(){


    }

    public Student(String name,String email,String password,String mobileNum,String classChoosen,String pincode){


        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNum = mobileNum;
        this.classChoosen = classChoosen;
        this.pincode = pincode;
        enrolledCourses = new HashMap<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getClassChoosen() {
        return classChoosen;
    }

    public void setClassChoosen(String classChoosen) {
        this.classChoosen = classChoosen;
    }

    private String pincode;
    private String classChoosen;

}
