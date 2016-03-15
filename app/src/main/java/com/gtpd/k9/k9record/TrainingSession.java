package com.gtpd.k9.k9record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingSession {

    public Dog dog;
    public List<Explosive> explosives;

    private Date trainingStartDate;
    private int trainingRunTime;
    private int temperature;

    /** TODO: confirm these items
     * > I'm making the assumption that this will be a string,
     * since it'll save having to convert it back later.
     * > Also assuming we want to log the explosive instance because
     * there may be multiple of the same explosive in different locations
     */
    private List<Tuple<Explosive, String>> individualExplosivesTimeToFind;
    private List<Tuple<Explosive, String>> notes;

    public TrainingSession(Dog dog) {
        this.dog = dog;
        notes = new ArrayList<>();
        individualExplosivesTimeToFind = new ArrayList<>();
    }

    public void addNotes(Tuple<Explosive, String> note){
        notes.add(note);
    }

    public void logTime(Tuple<Explosive, String> timeLoggedForExplosive){
        individualExplosivesTimeToFind.add(timeLoggedForExplosive);
    }

//    public TrainingSession(Date date, int totalRuntime, int temperature, Tuple<String, Integer> drugTimes, Tuple<String, String> individualNotes){
//        this.trainingStartDate = date;
//        this.trainingRunTime = totalRuntime;
//        this.temperature = temperature;
//        this.individualDrugsTimeToFind = drugTimes;
//        this.notes = individualNotes;
//    }
}
