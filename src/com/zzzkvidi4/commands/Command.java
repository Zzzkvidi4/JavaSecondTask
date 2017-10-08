package com.zzzkvidi4.commands;

import com.zzzkvidi4.exceptions.NotInitializedException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

    public abstract void initialize(List<Object> args);

    public abstract void execute() throws NotInitializedException, SQLException, IOException;

    public boolean isEnabled() {
        return true;
    }
}
