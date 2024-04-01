package com.example.exemserver.service.alert;

import lombok.Data;

@Data
public class Alert {
    private Toxic toxic;
    private AlertLevel alertLevel;
    public static final String[] KOREAN_GRADE = new String[] {"초미세먼지 경보", "미세먼지 경보", "초미세먼지 주의보", "미세먼지 주의보"};

    public Alert(Toxic toxic, AlertLevel alertLevel) {
        this.toxic =toxic;
        this.alertLevel = alertLevel;
    }

    public int getGrade(){
        int grade = 0;

        for(int i = 0; i < Toxic.values().length; i++){
            if(this.toxic == Toxic.values()[i]){
                grade += 2 - i;
            }
        }

        if(alertLevel == AlertLevel.ATTENTION) grade += 2;

        return grade;
    }
    @Override
    public String toString(){
        return toxic.name() + " " + alertLevel.name() + " " + getGrade() + "단계 발령";
    }
}
