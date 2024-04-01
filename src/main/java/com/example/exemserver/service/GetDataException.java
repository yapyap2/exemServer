package com.example.exemserver.service;

public class GetDataException extends RuntimeException{
    private String info;

    public GetDataException(String msg, String info){
        super(msg);
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
