package com.zzzkvidi4.commands;

import com.zzzkvidi4.exceptions.NotInitializedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class CommandList {
    private ArrayList<Command> commandList = new ArrayList<>();

    public void printCommandTitles(String header){
        Iterator<Command> commandIterator = commandList.iterator();
        System.out.println(header);
        int i = 1;
        while (commandIterator.hasNext()){
            Command cmd = commandIterator.next();
            if (cmd.isEnabled()) {
                System.out.println(i + ". " + cmd.getTitle());
                ++i;
            }
        }
    }

    public int actualSize() {
        int size = 0;
        for(Command cmd: commandList){
            if (cmd.isEnabled()){
                ++size;
            }
        }
        return size;
    }

    public void executeCommand(int index) throws NotInitializedException, SQLException {
        getCommand(index).execute();
    }

    public Command getCommand(int index){
        int realIndex = -1;
        Iterator<Command> iterator = commandList.iterator();
        while ((index >= 0) && (iterator.hasNext())) {
            if (iterator.next().isEnabled()){
                --index;
            }
            realIndex++;
        }
        return commandList.get(realIndex);
    }

    public void addCommand(Command cmd){
        commandList.add(cmd);
    }

    public void clear() {
        commandList.clear();
    }
}
