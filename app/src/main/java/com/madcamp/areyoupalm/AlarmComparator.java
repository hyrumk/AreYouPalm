package com.madcamp.areyoupalm;

import java.util.Comparator;

public class AlarmComparator implements Comparator<Alarm> {
    @Override
    public int compare(Alarm o1, Alarm o2) {
        if (o1.hour > o2.hour)
            return 1;
        else if (o1.hour < o2.hour)
            return -1;
        else {
            if (o1.minute > o2.minute)
                return 1;
            else if(o1.minute < o2.minute)
                return -1;
            else
                return 0;
        }
    }
}
