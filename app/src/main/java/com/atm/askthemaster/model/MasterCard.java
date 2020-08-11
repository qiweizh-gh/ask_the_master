package com.atm.askthemaster.model;

public class MasterCard {
    private String masterId;
    private String category;
    private String description;
    //save space for further actions.
    private String askerId;
    private String qTitle;
    private String masterUserId;

    public MasterCard() {
    }
    private MasterCard(MasterCard.MasterCardBuilder builder) {
        this.masterId = builder.masterId;
        this.category = builder.category;
        this.description = builder.description;
        this.qTitle = builder.qTitle;
        this.masterUserId = builder.masterUserId;
    }
    //getters:
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public String getMasterId() {
        return masterId;
    }
    public String getAskerIdId() {
        return askerId;
    }
    public String getqTitle() { return qTitle; }
    public String getMasterUserId() { return masterUserId; }

    public static class MasterCardBuilder {
        private String masterId;
        private String category;
        private String description;
        private String qTitle;
        private String masterUserId;

        public MasterCard build() {
            return new MasterCard(this);
        }
        public MasterCardBuilder setMasterId(String masterId) {
            this.masterId = masterId;
            return this;
        }
        public MasterCardBuilder setCategory(String category) {
            this.category = category;
            return this;
        }
        public MasterCardBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        public MasterCardBuilder setAsker(String masterId) {
            this.masterId = masterId;
            return this;
        }
        public MasterCardBuilder setQTitle(String qTitle) {
            this.qTitle = qTitle;
            return this;
        }
        public MasterCardBuilder setMasterUserId(String userId) {
            this.masterUserId = userId;
            return this;
        }
    }
}