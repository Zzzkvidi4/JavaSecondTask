package com.zzzkvidi4.commands;

import com.zzzkvidi4.exceptions.NotInitializedException;

import java.sql.SQLException;

public abstract class Command {
    public Command(String title){
        this.title = title;
        isInitialized = false;
    }

    protected boolean isInitialized;
    private String title;

    public final String getTitle() {
        return title;
    }

    public abstract void execute() throws NotInitializedException, SQLException;

    public boolean isEnabled() {
        return true;
    }
}
