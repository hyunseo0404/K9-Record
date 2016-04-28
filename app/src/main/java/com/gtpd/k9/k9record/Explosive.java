package com.gtpd.k9.k9record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class Explosive {

    public String name;
    public double quantity;
    public Unit unit;
    public String location;
    public double height;
    public double depth;
    public Timestamp placementTime;
    public String container;
    public int unitResource;
    public Timestamp startTime;
    public Timestamp endTime;
    ArrayList<Timestamp> falsePositives;
    ArrayList<Timestamp> handlerErrors;
    ArrayList<Timestamp> misses;

    List<Tuple<String, Timestamp>> results;

    public Explosive(String name, double quantity, Unit unit, String location, double height, double depth, Timestamp placementTime, String container, int unitResource) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.height = height;
        this.depth = depth;
        this.placementTime = placementTime;
        this.container = container;
        this.unitResource = unitResource;
        this.falsePositives = new ArrayList<>();
        this.handlerErrors = new ArrayList<>();
        this.misses = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public Explosive(String name, int unitResource) {
        this(name, 0, null, null, 0, 0, null, null, unitResource);
    }

    public String getQuantityAsString() {
        if (unit == Unit.STICK) {
            return String.format("%d stick%c", (int) quantity, quantity > 1 ? 's' : '\0');
        } else {
            return String.format("%s %s", Double.toString(quantity), unit.name().toLowerCase());
        }
    }

    public enum Unit {
        KG, G, LB, OZ, IN, FT, STICK
    }

    public void setStartTime(Timestamp start) {
        startTime = start;
    }

    public void setEndTime(Timestamp end){
        endTime = end;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }

    public String getElapsedTime() {
        long diff = endTime.getTime() - startTime.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String hour_str = (hours < 10) ? "0" + hours: "" + hours;
        String minutes_str = (minutes < 10) ? "0" + minutes: "" + minutes;
        String seconds_str = (seconds < 10) ? "0" + seconds: "" + seconds;
        return "" + hour_str + ":" + minutes_str + ":" + seconds_str;
    }

    public void addFind(Timestamp time) {
        this.results.add(new Tuple<String, Timestamp>("find", time));
    }
    public void addFalsePositive(Timestamp time) {
        this.results.add(new Tuple<String, Timestamp>("falsePositive", time));
//        this.falsePositives.add(time);
    }

    public void addHandlerError(Timestamp time) {
//        this.handlerErrors.add(time);
        this.results.add(new Tuple<String, Timestamp>("handlerError", time));
    }

    public void addMiss(Timestamp time) {
//        this.misses.add(time);
        this.results.add(new Tuple<String, Timestamp>("miss", time));
    }

    public JSONArray getResultsArray() throws JSONException {
        JSONArray resultsArr = new JSONArray();
        for(Tuple result: results){
            JSONObject tempObj = new JSONObject();
            tempObj.put("result", result.getKey());
            tempObj.put("timeOccurred", result.getValue());
            resultsArr.put(tempObj);
        }

        return resultsArr;
    }

    public String[] getResultsStr() {
        String [] finalResults = new String[results.size()];
        for(int i = 0; i < results.size(); i++) {
            String resultStr = results.get(i).getKey();
            if(resultStr.equals("find")) {
                finalResults[i] = "+";
            } else if(resultStr.equals("miss")) {
                finalResults[i] = "-";
            } else if(resultStr.equals("handlerError")) {
                finalResults[i] = "HE";
            } else if(resultStr.equals("falsePositive")) {
                finalResults[i] = "F";
            }
        }
        return finalResults;
    }
}
