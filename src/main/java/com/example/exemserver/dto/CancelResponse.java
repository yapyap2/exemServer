package com.example.exemserver.dto;

import com.example.exemserver.service.alert.Alert;
import lombok.Data;

@Data
public class CancelResponse {

    private String location;
    private String date;
    private String time;
    private String message;

    public CancelResponse(String[] line, int before){
        this.location = line[1];
        String[] dt = line[0].split(" ");
        this.date = dt[0];
        this.time = dt[1];
        this.message = date + " " + time + "시 " + line[1] + " '" +
                Alert.KOREAN_GRADE[before - 1] + "'가 해제되었습니다.";
    }

}
