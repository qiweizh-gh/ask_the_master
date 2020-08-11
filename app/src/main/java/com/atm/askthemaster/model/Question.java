package com.atm.askthemaster.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Question implements Serializable {
    private String questionId;
    private String questionKey;
    private String title;
    private String content;
    private List<Category> categories;
    private double duration;
    private Date createTime;
    private Date endTime;

    // Since user, asker, and master are using the same userId,
    // so recommendMasterList and confirmedMasterList contains a list of userId
    // and selectedMasterId is also a userId
    private List<String> recommendMasterList;
    private List<String> confirmedMasterList;
    private String selectedMasterId;

    private boolean isAnswered;
    private String status;
    private String askerId;
    private String previousQuestionId;

    public Question() {
    }

    public Question(QuestionBuilder builder) {
        this.questionId = builder.questionId;
        this.title = builder.title;
        this.content = builder.content;
        this.categories = builder.categories;
        this.duration = builder.duration;
        this.createTime = builder.createTime;
        this.endTime = builder.endTime;
        this.recommendMasterList = builder.recommendMasterList;
        this.confirmedMasterList = builder.confirmedMasterList;
        this.selectedMasterId = builder.selectedMasterId;
        this.isAnswered = builder.isAnswered;
        this.status = builder.status;
        this.askerId = builder.askerId;
        this.previousQuestionId = builder.previousQuestionId;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public double getDuration() {
        return duration;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<String> getRecommendMasterList() {
        return recommendMasterList;
    }

    public List<String> getConfirmedMasterList() {
        return confirmedMasterList;
    }

    public String getSelectedMasterId() {
        return selectedMasterId;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public String getStatus() {
        return status;
    }

    public String getAskerId() {
        return askerId;
    }

    public String getPreviousQuestionId() {
        return previousQuestionId;
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("recommendMasterList", getRecommendMasterList());
        map.put("confirmedMasterList", getConfirmedMasterList());
        map.put("selectedMasterId", getSelectedMasterId());
        map.put("isAnswered", isAnswered());
        map.put("status", getStatus());
        return map;
    }

    public static class QuestionBuilder {
        private String questionId;
        private String title;
        private String content;
        private List<Category> categories;
        private double duration;
        private Date createTime;
        private Date endTime;
        private List<String> recommendMasterList;
        private List<String> confirmedMasterList;
        private String selectedMasterId;
        private boolean isAnswered;
        private String status;
        private String askerId;
        private String previousQuestionId;

        public Question build() {
            return new Question(this);
        }


        public QuestionBuilder setQuestionId(String questionId) {
            this.questionId = questionId;
            return this;
        }

        public QuestionBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public QuestionBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public QuestionBuilder setCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public QuestionBuilder setDuration(double duration) {
            this.duration = duration;
            return this;
        }

        public QuestionBuilder setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public QuestionBuilder setEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }

        public QuestionBuilder setRecommendMasterList(List<String> recommendMasterList) {
            this.recommendMasterList = recommendMasterList;
            return this;
        }

        public QuestionBuilder setConfirmedMasterList(List<String> confirmedMasterList) {
            this.confirmedMasterList = confirmedMasterList;
            return this;
        }

        public QuestionBuilder setSelectedMasterId(String selectedMasterId) {
            this.selectedMasterId = selectedMasterId;
            return this;
        }

        public QuestionBuilder setAnswered(boolean answered) {
            isAnswered = answered;
            return this;
        }

        public QuestionBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public QuestionBuilder setAskerId(String askerId) {
            this.askerId = askerId;
            return this;
        }

        public QuestionBuilder setPreviousQuestionId(String previousQuestionId) {
            this.previousQuestionId = previousQuestionId;
            return this;
        }
    }
}