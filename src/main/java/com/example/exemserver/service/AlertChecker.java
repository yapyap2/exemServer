package com.example.exemserver.service;

import com.example.exemserver.service.alert.Alert;
import com.example.exemserver.service.alert.AlertLevel;
import com.example.exemserver.service.alert.Toxic;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AlertChecker {

    private final HashMap<Toxic, int[]> declarationStandard;
    public AlertChecker(){ // 새로운 유해물질 enum, 발령 기준을 여기에 추가
        declarationStandard = new HashMap<>();
        declarationStandard.put(Toxic.PM10, new int[] {150, 300});
        declarationStandard.put(Toxic.PM25, new int[] {75, 150});
    }

    public Alert getAlert(Toxic toxic, int before, int now){

        int attention = declarationStandard.get(toxic)[0];
        int warning = declarationStandard.get(toxic)[1];

        if(before >= attention && now >= attention){
            if(before >= warning && now >= warning){
                return new Alert(toxic, AlertLevel.WARNING);
            }
            return new Alert(toxic, AlertLevel.ATTENTION);
        }

        return null;
    }


    public HashMap<Toxic, int[]> getDeclarationStandard() {
        return declarationStandard;
    }
}
