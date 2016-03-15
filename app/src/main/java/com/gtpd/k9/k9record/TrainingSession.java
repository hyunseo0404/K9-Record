package com.gtpd.k9.k9record;

import java.util.Date;
import java.util.List;

public class TrainingSession {

    public Dog dog;
    public List<Explosive> explosives;

    private Date trainingStartDate;
    private int trainingRunTime;
    private int temperature;
    private Tuple<String, Integer> individualDrugsTimeToFind;
    private Tuple<String, String> notes;

    public TrainingSession(Dog dog) {
        this.dog = dog;
    }

//    public TrainingSession(Date date, int totalRuntime, int temperature, Tuple<String, Integer> drugTimes, Tuple<String, String> individualNotes){
//        this.trainingStartDate = date;
//        this.trainingRunTime = totalRuntime;
//        this.temperature = temperature;
//        this.individualDrugsTimeToFind = drugTimes;
//        this.notes = individualNotes;
//    }
}
