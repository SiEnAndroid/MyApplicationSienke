package com.example.administrator.thinker_soft.meter_code.sk.observer;


/**
 *
 * 被观察者接口
 */

public interface SubjectListener {
    void add(ObserverListener observerListener);
    void notifyObserver(String content);
    void remove(ObserverListener observerListener);
}