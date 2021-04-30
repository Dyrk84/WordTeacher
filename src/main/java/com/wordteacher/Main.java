package com.wordteacher;


import com.wordteacher.tools.Menu;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Menu menu = new Menu();
        menu.checkForFile();
    }
}