package com.zzzkvidi4.commands;

import java.util.List;

/**
 * Created by Роман on 26.09.2017.
 */
public class ExitCommand extends Command {
    public ExitCommand(String title) {
        super(title);
        isInitialized = true;
    }

    @Override
    public void initialize(List<Object> args) {
    }


    @Override
    public void execute() {

    }
}
