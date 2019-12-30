package com.spring_stream.server_song;

public class CustResponse {

    String mess;

    public CustResponse(String mess) {
        this.mess = mess;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
