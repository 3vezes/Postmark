package com.ericrgon.nearbox.model;

import java.util.HashMap;

public class Todo extends HashMap<String,Long> {

    public static final Todo EMPTY = new Todo();

    private Todo(){}

    public Todo(long timeStamp) {
        this.put("due",timeStamp);
    }
}
