package com.zzzkvidi4.commands;

public abstract class Command {
    public Command(String title){
        this.title = title;
    }

    private String title;

    public final String getTitle() {
        return title;
    }

    public abstract void execute();

    public boolean isEnabled() {
        return true;
    }
}
