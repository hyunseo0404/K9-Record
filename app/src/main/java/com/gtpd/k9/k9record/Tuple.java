package com.gtpd.k9.k9record;

/**
 * Created by Clayton on 3/12/2016.
 */
public class Tuple <K, V> {

    private K key;
    private V value;

    public Tuple(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
