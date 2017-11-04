package com.example.android.heychat.Model;

import java.util.HashMap;

/**
 * Created by CHALLIDO on 7/6/2017.
 */

public class MemberName {
    private HashMap<String,Boolean> map;


    public MemberName(HashMap<String, Boolean> map) {
        this.map = map;
    }

    public HashMap<String, Boolean> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Boolean> map) {
        this.map = map;
    }
}
