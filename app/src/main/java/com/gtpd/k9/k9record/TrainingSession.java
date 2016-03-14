package com.gtpd.k9.k9record;

import java.util.Date;

/**
 * Created by Clayton on 3/12/2016.
 */
public class TrainingSession {

    private Date trainingStartDate;
    private int trainingRunTime;
    private int temperature;
    private Tuple<String, Integer> individualDrugsTimeToFind;
    private Tuple<String, String> notes;

    public TrainingSession(Date date, int totalRuntime, int temperature, Tuple<String, Integer> drugTimes, Tuple<String, String> individualNotes){
        this.trainingStartDate = date;
        this.trainingRunTime = totalRuntime;
        this.temperature = temperature;
        this.individualDrugsTimeToFind = drugTimes;
        this.notes = individualNotes;
    }
}
