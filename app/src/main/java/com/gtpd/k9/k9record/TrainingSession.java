package com.gtpd.k9.k9record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingSession {

    public Dog dog;
    public List<Explosive> explosives;

    public int activeExplosiveIndex = -1;

    private Date trainingStartDate;
    private int trainingRunTime;

    private JSONObject wind;
    private JSONObject temperature;
    private String humidity;
    private String weatherDesc;
    private String gpsLoc;

    public Timestamp startTime;
    public Timestamp endTime;

    /** TODO: confirm these items
     * > I'm making the assumption that this will be a string,
     * since it'll save having to convert it back later.
     * > Also assuming we want to log the explosive instance because
     * there may be multiple of the same explosive in different locations
     */
    private List<Tuple<Explosive, String>> individualExplosivesTimeToFind;
    private Map<Explosive, List<String>> notes;


    public TrainingSession() {
        notes = new HashMap<>();
        individualExplosivesTimeToFind = new ArrayList<>();
        startTime = null;
        endTime = null;
        temperature = new JSONObject();
        wind = new JSONObject();
    }

    public void addNotes(Tuple<Explosive, String> note){
        // Create the list if it didn't exist
        if(notes.get(note.getKey()) == null) {
            List<String> noteList = new ArrayList<>();
            noteList.add(note.getValue());
            notes.put(note.getKey(), noteList);
        } else {
            // Otherwise add to the existing list
            notes.get(note.getKey()).add(note.getValue());
        }
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
    public Dog getDog(){
        return this.dog;
    }

    /**
     *
     * @return
     */
    public JSONObject generateSessionPayload() throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("trainerP_ID", null);
        payload.put("handlerP_ID", null);
        payload.put("dogP_ID", dog.getName());
        payload.put("problemType", null);
        payload.put("setNumber", null);

        payload.put("gpsLocation", gpsLoc);
        payload.put("temp", temperature);
        payload.put("humidity", humidity);
        payload.put("wind", wind);
        payload.put("weatherDesc", weatherDesc);

        payload.put("explosives", getExplosivesJSON());
        payload.put("startTime", startTime); // TODO: Figure out the first
        payload.put("endTime", endTime);
        return payload;
    }

    /**
     *
     * unit :
     * low :
     * avg :
     * high :
     * NOTE: defaulting to average for temp
     */
    public void setTempPayload(String unit, double low, double avg, double high) throws JSONException {
//        temperature = new JSONObject();
        temperature.put("unit", unit);
        temperature.put("low", low);
        temperature.put("avg", avg);
        temperature.put("high", high);
    }


    /**
     * wind : {
        speedUnit:
        speed :
        chillUnit:
        chill :
        direction :
     }
     */
    public void setWindPayload(String unit, String wind_mph, String chill_unit,
                               String chill_temp, String wind_dir) throws JSONException {
//        wind = new JSONObject();
        wind.put("speedUnit", unit);
        wind.put("speed", wind_mph);
        wind.put("chillUnit", chill_unit);
        wind.put("chill", chill_temp);
        wind.put("direction", wind_dir);
    }

    public void setWeather(JSONObject response) {
        try {
            setTempPayload("F", response.getInt("temp_f"), response.getInt("temp_f"), response.getInt("temp_f"));
            setWindPayload("mph", response.getString("wind_mph"), "F", response.getString("windchill_f"), response.getString("wind_dir"));
            setHumidity(response.getString("relative_humidity"));
            setWeatherDesc(response.getString("weather"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWeatherDesc(String desc) {
        this.weatherDesc = desc;
    }

    /**
     *
     explosives : list of all explosives [{
         type:
         amount :
         Units:
         hidingLocation:
         depth:
         Height:
         placementTime
         startTime:
         endTime:
         result:
         Notes: [ <Strings> ]
     }]
     * @return
     */
    public JSONArray getExplosivesJSON() throws JSONException {
        JSONArray arr = new JSONArray();
        for(Explosive exp: explosives) {
            JSONObject obj = new JSONObject();
            obj.put("type", exp.name);
            obj.put("amount", exp.quantity);
            obj.put("Units", exp.unit.toString());
            obj.put("hidingLocation", exp.location);
            obj.put("depth", exp.depth);
            obj.put("Height", exp.height);
            obj.put("placementTime", exp.placementTime);
            obj.put("startTime", exp.startTime);
            obj.put("endTime", exp.endTime);
//            obj.put("result", exp.) // TODO: result is unclear...fix this shit
            obj.put("results", exp.getResultsArray());
            obj.put("Notes", notes.get(exp));

        }

        return arr;
    }

    public void setGPS(double lat, double longitude) {
        this.gpsLoc = "" + lat + ", " + longitude;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}


/** CLAYTON's PAYLOAD
 {
 trainerP_ID: # of trainer
 handlerP_ID: # of handler
 dogP_ID
 problemType :
 setNumber :
 date :
 temp:	{
     unit :
     low :
     avg :
     high :
 }
 humidity :
 wind : {
     speedUnit:
     speed :
     chillUnit:
     chill :
     direction :
 }
 weatherDesc : description of weather (Sunny/Rainy/ Cloudy)
 gpsLocation :
 noiseLevelUnit :
 noiseLevel :
 explosives : list of all explosives [{
     type:
     amount :
     Units:
     hidingLocation:
     depth:
     Height:
     placementTime
     startTime:
     endTime:
     result:
     Notes: [ <Strings> ]
 }]
 startTime:
 endTime:
 result: --> Can be removed
 notes: [ <Strings> ]
 }
 */