package com.example.exemserver.dto;

import com.example.exemserver.service.alert.Alert;
import lombok.Data;


@Data
public class AlertResponse {

    private int grade;
    private String location;
    private String date;
    private String time;
    private String message;

    public AlertResponse(Alert alert, String dateTime, String location, int before){
        this.grade = alert.getGrade();
        this.location = location;
        String[] dt = dateTime.split(" ");
        this.date = dt[0];
        this.time = dt[1];
        this.message = date + " " + time + "시 " + location + " '";

        if(before == 0){
            this.message += Alert.KOREAN_GRADE[grade - 1] + "'가 발령되었습니다.";
        }
        else{
            if(before > alert.getGrade()){
                this.message += Alert.KOREAN_GRADE[before - 1] + "'에서 '" +
                        Alert.KOREAN_GRADE[grade - 1] + "'로 상향 발령되었습니다.";
            }
            if(before < alert.getGrade()){
                this.message += Alert.KOREAN_GRADE[before - 1] + "'에서 '" +
                        Alert.KOREAN_GRADE[grade - 1] + "'로 하향 발령되었습니다.";
            }
        }

    }

}
