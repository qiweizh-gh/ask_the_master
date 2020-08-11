package com.atm.askthemaster.model;

import java.util.Date;
import java.util.List;

public class User {
    // User fields
    private String userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Date timestamp;
    private boolean isMaster;
    private boolean isAsker;
    private String askerId;
    private String masterId;

    // Asker fields
    private double askerRating;
    private List<String> askerQuestions;

    // Master fields
    private List<String> masterSpecialties;
    private double masterRating;
    private List<String> masterQuestions;
    private int pickedCount;
    private double consultingFee;
    private String linkedInURL;
    private String introduction;
    private boolean masterIsWorking;

    public User() {
    }

    private User(UserBuilder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.password = builder.password;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.timestamp = builder.timestamp;
        this.isMaster = builder.isMaster;
        this.isAsker = builder.isAsker;
        this.askerId = builder.askerId;
        this.masterId = builder.masterId;

        this.askerRating = builder.askerRating;
        this.askerQuestions = builder.askerQuestions;

        this.masterSpecialties = builder.masterSpecialties;
        this.masterRating = builder.masterRating;
        this.masterQuestions = builder.masterQuestions;
        this.pickedCount = builder.pickedCount;
        this.consultingFee = builder.consultingFee;
        this.linkedInURL = builder.linkedInURL;
        this.introduction = builder.introduction;
        this.masterIsWorking = builder.masterIsWorking;
    }

    // User getter
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public boolean isAsker() {
        return isAsker;
    }

    public String getAskerId() {
        return askerId;
    }

    public String getMasterId() {
        return masterId;
    }

    // Asker getter
    public double getAskerRating() {
        return askerRating;
    }

    public List<String> getAskerQuestions() {
        return askerQuestions;
    }

    // Master getter
    public List<String> getMasterSpecialties() {
        return masterSpecialties;
    }

    public double getMasterRating() {
        return masterRating;
    }

    public List<String> getMasterQuestions() {
        return masterQuestions;
    }

    public int getPickedCount() {
        return pickedCount;
    }

    public double getConsultingFee() {
        return consultingFee;
    }

    public String getLinkedInURL() {
        return linkedInURL;
    }

    public String getIntroduction() {
        return introduction;
    }

    public boolean isMasterIsWorking() {
        return masterIsWorking;
    }

    public static class UserBuilder {
        // User getter
        private String userId;
        private String userName;
        private String password;
        private String firstName;
        private String lastName;
        private Date timestamp;
        private boolean isMaster;
        private boolean isAsker;
        private String askerId;
        private String masterId;

        // Asker fields
        private double askerRating;
        private List<String> askerQuestions;

        // Master fields
        private List<String> masterSpecialties;
        private double masterRating;
        private List<String> masterQuestions;
        private int pickedCount;
        private double consultingFee;
        private String linkedInURL;
        private String introduction;
        private boolean masterIsWorking;

        // User setter
        public UserBuilder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public UserBuilder setMaster(boolean master) {
            this.isMaster = master;
            return this;
        }

        public UserBuilder setAsker(boolean asker) {
            this.isAsker = asker;
            return this;
        }

        public UserBuilder setAskerId(String askerId) {
            this.askerId = askerId;
            return this;
        }

        public UserBuilder setMasterId(String masterId) {
            this.masterId = masterId;
            return this;
        }

        // Asker setter
        public UserBuilder setAskerRating(double askerRating) {
            this.askerRating = askerRating;
            return this;
        }

        public UserBuilder setAskerQuestions(List<String> askerQuestions) {
            this.askerQuestions = askerQuestions;
            return this;
        }

        public User build() {
            return new User(this);
        }

        // Master setter
        public UserBuilder setMasterSpecialties(List<String> masterSpecialties) {
            this.masterSpecialties = masterSpecialties;
            return this;
        }

        public UserBuilder setMasterRating(double masterRating) {
            this.masterRating = masterRating;
            return this;
        }

        public UserBuilder setMasterQuestions(List<String> masterQuestions) {
            this.masterQuestions = masterQuestions;
            return this;
        }

        public UserBuilder setPickedCount(int pickedCount) {
            this.pickedCount = pickedCount;
            return this;
        }

        public UserBuilder setConsultingFee(double consultingFee) {
            this.consultingFee = consultingFee;
            return this;
        }

        public UserBuilder setLinkedInURL(String linkedInURL) {
            this.linkedInURL = linkedInURL;
            return this;
        }

        public UserBuilder setIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public UserBuilder setMasterIsWorking(boolean working) {
            this.masterIsWorking = working;
            return this;
        }
    }
}