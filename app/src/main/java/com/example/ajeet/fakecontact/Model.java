package com.example.ajeet.fakecontact;

import java.util.Comparator;

class Model {
    String first_name,last_name,number,name,OTP;
    long time;
    public Model(String first_name,String last_name,String number,String ab){
        this.first_name=first_name;
        this.last_name=last_name;
        this.number=number;

    }
    public Model(String name, long time, String OTP){
        this.time=time;
        this.OTP=OTP;
        this.name=name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getOTP() {
        return OTP;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

// list sorting........
    public static Comparator<Model> stTime = new Comparator<Model>() {

        public int compare(Model m1, Model m2) {

            long time1 = m1.getTime();
            long time2 = m2.getTime();

            /*For descending order*/
            return (int) (time2-time1);


        }};
}
