package com.dfsoft.myiptvplayer;

import com.dfsoft.myiptvplayer.views.IPTVEpgData;

import java.util.ArrayList;
import java.util.List;

public class IPTVEPG {
    public int code = 0;
    public String msg = "";
    public String name = "";
    public int tvid = 0;
    public String date = "";

    public List<IPTVEpgData> data = new ArrayList<>();

    public Boolean isEmpty() {
        return code != 200;
    }

}
