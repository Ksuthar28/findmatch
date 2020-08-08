package com.saadi.findmatch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kailash Suthar.
 */

public class MyMatchModel {
    private static final long serialVersionUID = -7966543301779447258L;

    @SerializedName("gender")
    private String gender;

    @SerializedName("name")
    private NameModel nameModel;

    @SerializedName("location")
    private LocationModel locationModel;

    @SerializedName("email")
    private String email;

    @SerializedName("dob")
    private DOBModel dobModel;

    @SerializedName("phone")
    private String phone;

    @SerializedName("picture")
    private PictureModel pictureModel;

    private String status = "0";

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public NameModel getNameModel() {
        return nameModel;
    }

    public void setNameModel(NameModel nameModel) {
        this.nameModel = nameModel;
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DOBModel getDobModel() {
        return dobModel;
    }

    public void setDobModel(DOBModel dobModel) {
        this.dobModel = dobModel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PictureModel getPictureModel() {
        return pictureModel;
    }

    public void setPictureModel(PictureModel pictureModel) {
        this.pictureModel = pictureModel;
    }

    public static class NameModel {

        @SerializedName("title")
        private String title;

        @SerializedName("first")
        private String first;

        @SerializedName("last")
        private String last;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }
    }

    public static class LocationModel {

        @SerializedName("city")
        private String city;

        @SerializedName("state")
        private String state;

        @SerializedName("country")
        private String country;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class DOBModel {

        @SerializedName("age")
        private String age;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    public static class PictureModel {

        @SerializedName("medium")
        private String medium;

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
