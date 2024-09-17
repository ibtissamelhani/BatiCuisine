import database.DataBaseConnection;
import ui.Menu;

import java.sql.Connection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        final String RESET = "\u001B[0m";
        final String YELLOW = "\u001B[33m";
        System.out.println(YELLOW+"                    __________         __  .__          _________       .__       .__               ");
        System.out.println("                    \\______   \\_____ _/  |_|__|         \\_   ___ \\ __ __|__| _____|__| ____   ____  ");
        System.out.println("                     |    |  _/\\__  \\\\   __\\  |  ______ /    \\  \\/|  |  \\  |/  ___/  |/    \\_/ __ \\ ");
        System.out.println("                     |    |   \\ / __ \\|  | |  | /_____/ \\     \\___|  |  /  |\\___ \\|  |   |  \\  ___/ ");
        System.out.println("                     |______  /(____  /__| |__|          \\______  /____/|__/____  >__|___|  /\\___  >");
        System.out.println("                            \\/      \\/                          \\/              \\/        \\/     \\/ ");

        System.out.println("\n\t\t\tWelcome to BatiCuisine, your professional solution for accurate kitchen construction cost estimation!"+RESET);
        menu.start();
    }

}