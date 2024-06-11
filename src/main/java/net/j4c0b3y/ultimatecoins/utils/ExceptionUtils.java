package net.j4c0b3y.ultimatecoins.utils;

import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

@UtilityClass
public class ExceptionUtils {
    public String getStackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer, true);
        throwable.printStackTrace(printer);
        return writer.getBuffer().toString();
    }
}