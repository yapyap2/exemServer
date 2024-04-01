package com.example.exemserver.service;


import com.example.exemserver.dto.AlertRecord;
import com.example.exemserver.dto.Maintenance;
import com.example.exemserver.repositoty.AlertRepository;
import com.example.exemserver.repositoty.MaintenanceRepository;
import com.example.exemserver.service.alert.Alert;
import com.example.exemserver.service.alert.Toxic;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final ResourceLoader resourceLoader;
    private final AlertChecker alertChecker;
    private final SocketService socketService;
    private final AlertRepository alertRepository;
    private final MaintenanceRepository maintenanceRepository;

    private final List<String[]> data = new ArrayList<>();

    public void startAlert(WebSocketSession session) {
        HashMap<Integer, HashMap<Toxic, Integer>> observatories = new HashMap<>();
        HashMap<Integer, Integer> gradeTemp = new HashMap<>();

        for(String[] line : data){
            Integer obsCode = Integer.parseInt(line[2]);

            // 지역별 관측값 초기화
            if(!observatories.containsKey(obsCode)){
                HashMap<Toxic, Integer> map = new HashMap<>();
                for(Toxic toxic : Toxic.values()){
                    map.put(toxic, 0);
                }
                observatories.put(obsCode, map);
                gradeTemp.put(obsCode, 0);
            }
            HashMap<Toxic, Integer> currentObs = observatories.get(obsCode);

            //점검중인 센서 확인 및 점검 기록 저장
            checkMaintenance(line);
            if(line[0].equals("2023-03-05 03") && line[2].equals("111311")){
                System.out.println(" ");
            }
            Alert alert = null;
            for(int i = 0; i<Toxic.values().length; i++){
                Toxic toxic = Toxic.values()[i];
                int value = Integer.parseInt(line[3 + i]);
                Alert a = alertChecker.getAlert(toxic, currentObs.get(toxic), value);
                if(alert == null) alert = a;
                else if(a != null){
                    if(alert.getGrade() > a.getGrade()) alert = a;
                }
            }

            if(alert != null){ // 발령
                Toxic toxic = alert.getToxic();
                System.out.println(line[0] + " " + line[1]);
                System.out.println("Alert occurred " + toxic.name());
                System.out.println(alert + "\n");

                if(gradeTemp.get(obsCode) != alert.getGrade()){
                    socketService.sendAlert(session, alert, line, gradeTemp.get(obsCode));
                }
                saveAlert(alert, line, gradeTemp.get(obsCode), session.getId());
                gradeTemp.replace(obsCode, alert.getGrade());
            } else{ // 발령 취소
                if(gradeTemp.get(obsCode) != 0) {
                    socketService.sendCancelMessage(session,line, gradeTemp.get(obsCode));
                    gradeTemp.replace(obsCode, 0);
                }
            }

            // 기록 보관
            for(int i = 0; i<Toxic.values().length; i++){
                currentObs.replace(Toxic.values()[i], Integer.parseInt(line[3 + i]));
            }

        }

        socketService.closeSession(session);

    }


    private void saveAlert(Alert alert, String[] line, int beforeGrade, String sessionId){
        AlertRecord record = new AlertRecord(alert, line[0], line[1], beforeGrade, sessionId);
        alertRepository.save(record);
    }

    private void checkMaintenance(String[] line){

        for(int i = 0; i<Toxic.values().length; i++){
            int value = Integer.parseInt(line[3 + i]);

            if(value == 0){
                Maintenance maintenance = new Maintenance(line, Toxic.values()[i].name());
                maintenanceRepository.save(maintenance);
            }
        }

    }


    public List<String[]> getData(){
        return data;
    }


    @PostConstruct
    public void init() {
        List<String[]> lines = new ArrayList<>();

        try {
            InputStream inputStream = resourceLoader.getResource("classpath:/static/fineDustUnsorted.csv").getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            br.readLine();
            String line;
            while((line = br.readLine()) != null){
                String[] split = line.split(",",-1);

                lines.add(split);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(String[] line : lines){
            for(int i = 0; i<Toxic.values().length; i++){
                if(line[3 + i].equals("")){
                    line[3 + i] = "0"; //점검중인 경우 값에 0을 담음
                }
            }
            data.add(line);
        }

        data.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
    }
}
