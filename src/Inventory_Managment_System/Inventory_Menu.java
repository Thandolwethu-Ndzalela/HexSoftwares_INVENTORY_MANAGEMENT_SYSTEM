package Inventory_Managment_System;

import java.util.AbstractMap.SimpleEntry;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

/**
 * A menu system for managing products and transactions in an inventory.
 * The menu displays a list of choices, allows the user to select an option,
 * and executes the corresponding action for the selected choice.
 */
public class Inventory_Menu implements Runnable {
    private String system_title;  // Title of the system
    private java.util.List<SimpleEntry<String, Runnable>> choices;  // List of choices and corresponding actions
    private Connection con;

    /**
     * Constructs an Inventory_Menu with a specified system title and database connection.
     *
     * @param system_title The title of the system for the menu.
     * @param con The database connection to interact with the database.
     */
    public Inventory_Menu(String system_title, Connection con) {
        this.system_title = system_title;
        this.con = con;
        choices = new java.util.ArrayList<>();
    }

    /**
     * Adds a new choice to the menu with its associated action.
     *
     * @param text The description of the choice.
     * @param action The action to be executed when the choice is selected.
     */
    public void add_new_choice(String text, Runnable action) {
        choices.add(new SimpleEntry<>(text, action));  // Store the choice and corresponding action
    }

    /**
     * Runs the menu system, displaying choices and handling user input in a loop.
     */
    @Override
    public void run() {
        int choice = -1;
        do {
            displayChoices();  // Show the menu choices
            choice = getChoice();  // Get user input for the choice
            processChoice(choice);  // Process the chosen action
        } while (choice != choices.size() + 1);  // Exit when 'Exit' option is selected
    }

    /**
     * Displays the available choices in the menu, including an exit option.
     */
    private void displayChoices() {
        System.out.println(system_title);  // Display the system title

        // Loop through and print each available choice
        for (int i = 0; i < choices.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, choices.get(i).getKey());
        }

        // Display the exit option
        System.out.printf("%d) Exit\n", choices.size() + 1);
    }

    /**
     * Prompts the user for a choice and ensures valid input.
     *
     * @return The valid choice selected by the user.
     */
    private int getChoice() {
        int choice = 0;
        Scanner in = new Scanner(System.in);

        do {
            try {
                System.out.print(">");  // Prompt for user input
                choice = in.nextInt();  // Read the choice
                in.nextLine();  // Consume any remaining input on the line
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                in.nextLine();  // Consume the invalid input to avoid an infinite loop
            }
        } while ((choice <= 0) || (choice > (choices.size() + 1)));  // Ensure the choice is valid

        return choice;  // Return the valid choice
    }

    /**
     * Executes the action associated with the selected choice.
     *
     * @param choice The index of the chosen action.
     */
    private void processChoice(int choice) {
        // If the choice is valid (within the range of available actions), execute the action
        if (choice <= choices.size()) {
            choices.get(choice - 1).getValue().run();
        } else {
            System.out.println("Exiting menu...");
        }
    }
}
