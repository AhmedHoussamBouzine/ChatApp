package com.chatapp.business;

public class DefaultServices implements IServices {

    private static DefaultServices instance = null;


    private DefaultServices() {

    }

    public static DefaultServices getInstance(){

        if(instance == null)
            instance = new DefaultServices();

        return instance;

    }

}
