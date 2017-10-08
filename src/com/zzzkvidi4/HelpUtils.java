package com.zzzkvidi4;
import com.zzzkvidi4.casters.Caster;
import com.zzzkvidi4.casters.IntegerCaster;
import com.zzzkvidi4.commands.CommandList;
import com.zzzkvidi4.exceptions.AbortOperationException;
import com.zzzkvidi4.validators.BasicValidator;
import com.zzzkvidi4.validators.IntegerBetweenBoundariesValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelpUtils {
    public static User rowToUser(ResultSet rs) throws  SQLException{
        User user = new User();
        user.setId(rs.getInt("id_user"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        return user;
    }

    public static <T> T getValueCLI(String title, String abortString, ArrayList<BasicValidator<T>> validators, T initialValue, Caster<T> caster) throws AbortOperationException{
        String buf;
        boolean isInputCorrect = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        T value = initialValue;
        while (!isInputCorrect){
            System.out.print(title);
            try {
                buf = reader.readLine();
                if (buf.equals(abortString)){
                    throw new AbortOperationException("Отмена операции...");
                }
                value = caster.cast(buf);
                isInputCorrect = true;
                isInputCorrect = CheckValidatorsCLI(validators, value);
            }
            catch(IOException e){
                System.out.println("Внимание, произошла ошибка ввода!");
            }
            catch(NumberFormatException e){
                System.out.println("Внимание, вы ввели не число!");
            }
        }
        return value;
    }

    private static <T> boolean CheckValidatorsCLI(ArrayList<BasicValidator<T>> validators, T value) {
        boolean isInputCorrect = true;
        for(BasicValidator<T> validator : validators) {
            boolean tmpFlag = validator.validate(value);
            if (!tmpFlag) {
                System.out.println(validator.message());
            }
            isInputCorrect = isInputCorrect && tmpFlag;
        }
        return isInputCorrect;
    }

    public static <T> T getValueCLIWithoutAbort(String title, ArrayList<BasicValidator<T>> validators, T initialValue, Caster<T> caster) {
        String buf;
        boolean isInputCorrect = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        T value = initialValue;
        while (!isInputCorrect){
            System.out.print(title);
            try {
                buf = reader.readLine();
                value = caster.cast(buf);
                isInputCorrect = CheckValidatorsCLI(validators, value);
            }
            catch(IOException e){
                System.out.println("Внимание, произошла ошибка ввода!");
            }
            catch(NumberFormatException e){
                System.out.println("Внимание, вы ввели не число!");
            }
        }
        return value;
    }


    public static void runCommandList(String commonTitle, CommandList cmdList) {
        int cmdNumber;
        int actualSize;
        do {
            cmdList.printCommandTitles(commonTitle);
            actualSize = cmdList.actualSize();
            ArrayList<BasicValidator<Integer>> validators = new ArrayList<>();
            validators.add(new IntegerBetweenBoundariesValidator("Число должно быть между 1 и " + actualSize + "!", 1, actualSize));
            cmdNumber = HelpUtils.getValueCLIWithoutAbort("--> ", validators, -1, new IntegerCaster());
            cmdList.executeCommand(cmdNumber - 1);
        } while (cmdNumber != actualSize);
    }
}
