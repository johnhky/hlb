package com.hlb.haolaoban.bean.device;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heky on 2017/11/24.
 */

public class BloodPressureBean {
    /* [{"date":"11/20","value":"111"},{"date":"11/22","svalue":"116","ss":555},{"date":"11/24","value":"124"}]*/
    private String date;
    /**
     * items : {"1":"109"}
     */

    private ItemsBean items;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ItemsBean getItems() {
        return items;
    }

    public void setItems(ItemsBean items) {
        this.items = items;
    }

    public static class ItemsBean {

        @SerializedName("1")
        private int hypertension;

        @SerializedName("2")
        private int hypotension;

        @SerializedName("3")
        private float heart_rate;

        @SerializedName("4")
        private float blood_sugar;

        @SerializedName("5")
        private float weight;

        @SerializedName("6")
        private float bmi;

        @SerializedName("7")
        private float body_fat;

        @SerializedName("8")
        private float body_moisture;

        @SerializedName("9")
        private float basal_metabolic_rate;

        @SerializedName("10")
        private float subcutaneous_fat_rate;

        @SerializedName("11")
        private float visceral_adipose_grade;

        @SerializedName("12")
        private float skeletal_muscle_rate;

        @SerializedName("13")
        private float bone_mass;

        @SerializedName("14")
        private float protein;

        @SerializedName("15")
        private float body_age;

        @SerializedName("16")
        private float lbm;

        @SerializedName("17")
        private float muscle_mass;

        @SerializedName("18")
        private float face;

        @SerializedName("19")
        private float temperature;

        @SerializedName("99")
        private float other;

        public int getHypertension() {
            return hypertension;
        }

        public void setHypertension(int hypertension) {
            this.hypertension = hypertension;
        }

        public int getHypotension() {
            return hypotension;
        }

        public void setHypotension(int hypotension) {
            this.hypotension = hypotension;
        }

        public float getHeart_rate() {
            return heart_rate;
        }

        public void setHeart_rate(float heart_rate) {
            this.heart_rate = heart_rate;
        }

        public float getBlood_sugar() {
            return blood_sugar;
        }

        public void setBlood_sugar(float blood_sugar) {
            this.blood_sugar = blood_sugar;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public float getBmi() {
            return bmi;
        }

        public void setBmi(float bmi) {
            this.bmi = bmi;
        }

        public float getBody_fat() {
            return body_fat;
        }

        public void setBody_fat(float body_fat) {
            this.body_fat = body_fat;
        }

        public float getBody_moisture() {
            return body_moisture;
        }

        public void setBody_moisture(float body_moisture) {
            this.body_moisture = body_moisture;
        }

        public float getBasal_metabolic_rate() {
            return basal_metabolic_rate;
        }

        public void setBasal_metabolic_rate(float basal_metabolic_rate) {
            this.basal_metabolic_rate = basal_metabolic_rate;
        }

        public float getSubcutaneous_fat_rate() {
            return subcutaneous_fat_rate;
        }

        public void setSubcutaneous_fat_rate(float subcutaneous_fat_rate) {
            this.subcutaneous_fat_rate = subcutaneous_fat_rate;
        }

        public float getVisceral_adipose_grade() {
            return visceral_adipose_grade;
        }

        public void setVisceral_adipose_grade(float visceral_adipose_grade) {
            this.visceral_adipose_grade = visceral_adipose_grade;
        }

        public float getSkeletal_muscle_rate() {
            return skeletal_muscle_rate;
        }

        public void setSkeletal_muscle_rate(float skeletal_muscle_rate) {
            this.skeletal_muscle_rate = skeletal_muscle_rate;
        }

        public float getBone_mass() {
            return bone_mass;
        }

        public void setBone_mass(float bone_mass) {
            this.bone_mass = bone_mass;
        }

        public float getProtein() {
            return protein;
        }

        public void setProtein(float protein) {
            this.protein = protein;
        }

        public float getBody_age() {
            return body_age;
        }

        public void setBody_age(float body_age) {
            this.body_age = body_age;
        }

        public float getLbm() {
            return lbm;
        }

        public void setLbm(float lbm) {
            this.lbm = lbm;
        }

        public float getMuscle_mass() {
            return muscle_mass;
        }

        public void setMuscle_mass(float muscle_mass) {
            this.muscle_mass = muscle_mass;
        }

        public float getFace() {
            return face;
        }

        public void setFace(float face) {
            this.face = face;
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public float getOther() {
            return other;
        }

        public void setOther(float other) {
            this.other = other;
        }
    }
}
