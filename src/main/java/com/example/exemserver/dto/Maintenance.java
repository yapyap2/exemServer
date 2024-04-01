package com.example.exemserver.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private LocalDateTime dateTime;
    private String sensorType;

    public Maintenance(String[] line, String type){
        this.location = line[1];
        this.dateTime = LocalDateTime.parse(line[0] + ":00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.sensorType = type;
    }

    public Maintenance() {

    }
}
