package com.wordteacher.utils;

public enum Colors {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    BLUE("\u001B[34m"),
    GREEN("\u001B[32m"),
    CYAN("\u001B[36m");

    public final String typeOfColor;

    Colors(String typeOfColor) {
        this.typeOfColor = typeOfColor;
    }

}
// typeOfColors
//    ANSI_RESET = "\u001B[0m";
//    ANSI_RED = "\u001B[31m";
//    ANSI_BLUE = "\u001B[34m";
//    ANSI_GREEN = "\u001B[32m";

//    ANSI_BLACK = "\u001B[30m";
//    ANSI_YELLOW = "\u001B[33m";
//    ANSI_PURPLE = "\u001B[35m";
//    ANSI_CYAN = "\u001B[36m";
//    ANSI_WHITE = "\u001B[37m";