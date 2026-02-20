package com.arun.event.utils;

import com.arun.event.db.model.Dept;
import com.arun.event.db.model.Session;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final int CREATE = 1;
    public static final int UPDATE = 2;

    public static List<Dept> getDept() {
        List<Dept> list = new ArrayList<Dept>();
        list.add(new Dept(0, "I-BSC (CS)"));
        list.add(new Dept(1, "II-BSC (CS)"));
        list.add(new Dept(2, "III-BSC (CS)"));

        list.add(new Dept(3, "I-BSC (CT)"));
        list.add(new Dept(4, "II-BSC (CT)"));
        list.add(new Dept(5, "III-BSC (CT)"));

        list.add(new Dept(6, "I-BSC (IT)"));
        list.add(new Dept(7, "II-BSC (IT)"));
        list.add(new Dept(8, "III-BSC (IT)"));

        list.add(new Dept(9, "I-BCA"));
        list.add(new Dept(10, "II-BCA"));
        list.add(new Dept(11, "III-BCA"));


        list.add(new Dept(12, "I-MSC (CS)"));
        list.add(new Dept(13, "II-MSC (CS)"));
        list.add(new Dept(14, "I-MSC (CT)"));
        list.add(new Dept(15, "II-MSC (CT)"));
        list.add(new Dept(16, "I-MSC (IT)"));
        list.add(new Dept(17, "II-MSC (IT)"));
        list.add(new Dept(18, "I-MCA"));
        list.add(new Dept(19, "II-MCA"));
        list.add(new Dept(20, "M.Phil"));
        list.add(new Dept(21, "Others"));
        return list;
    }

    public static List<Session> getSession() {
        List<Session> list = new ArrayList<Session>();
        list.add(new Session(0, "Fore Noon"));
        list.add(new Session(1, "After Noon"));
        list.add(new Session(2, "FullDay"));
        return list;
    }
    public static List<String> getMonths() {
        List<String> months = new ArrayList<>();
        months.add("Jan");
        months.add("Feb");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");
        return months;
    }

}
