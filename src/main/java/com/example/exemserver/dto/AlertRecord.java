package com.example.exemserver.dto;

import jakarta.persistence.*;
import com.example.exemserver.service.alert.Alert;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class AlertRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String toxicType;
    private String level;
    private int grade;
    private String location;
    private LocalDateTime dateTime;
    private String status;
    private String sessionId;

    public AlertRecord(Alert alert, String strDate, String location, int before, String sessionId){
        this.toxicType = alert.getToxic().name();
        this.level = alert.getAlertLevel().name();
        this.grade = alert.getGrade();
        this.dateTime = LocalDateTime.parse(strDate + ":00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.location = location;
        this.sessionId = sessionId;

        if(strDate.equals("2023-03-23 22") && location.equals("노원구")){
            System.out.println("stop!");
        }

        if(before != 0){
            if(before < grade) this.status = "하향 발령";
            else if(before > grade) this.status = "상향 발령";
            else this.status = "유지";
        } else this.status = "최초 발령";
    }

    public AlertRecord() {

    }
}
