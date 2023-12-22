package com.chatapp;

import com.chatapp.presentation.views.MainApplication;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            Application.launch(MainApplication.class, args);
            // Each instance of MessagingApp will have a different user
            // You can set more properties or simulate different users within each instance
        });

        Thread threadB = new Thread(() -> {
            Application.launch(MainApplication.class, args);
        });

        threadA.start();
        threadB.start();
    }
}