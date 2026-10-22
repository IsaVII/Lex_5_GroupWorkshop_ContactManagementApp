package se.lexicon;


public class ContactApp {

    Contacts contacts = new Contacts();

    void main() {

        //some dummy contacts to test the app
        contacts.addNewContact(new Contact(1, "John", "Doe", "1234567890", ""));
        contacts.addNewContact(new Contact(2, "Jane", "Smith", "0987654321", ""));
        contacts.addNewContact(new Contact(3, "Alice", "Johnson", "5555555555", ""));

        IO.println("Welcome to the Contact Management App!");

        runApp();
    }

    private void runApp() {

        while (true) {

            //show main menu and getting the user-input for the choices
            int choice = showMainMenu();
            switch (choice) {

                //print all contacts
                case 1:
                    contacts.printAllContacts();
                    break;

                // create new contacts
                case 2:
                    createContacts();
                    break;

                // find a contact and show menu for the found contact
                case 3:
                    findContactMenu();
                    break;

                case 4:
                    showSortMenu();
                    break;

                // exit the app -> break out of the loop
                case 5:
                    return;
                default:
                    IO.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }


    private int showMainMenu() {
        IO.println("==================");
        IO.println("*** Main Menu ***");
        IO.println("==================");
        IO.println("What do you want to do?");
        IO.println("\t1. Print all contacts");
        IO.println("\t2. Create new contacts");
        IO.println("\t3. Find a contact by first name");
        IO.println("\t4. Sort contacts");
        IO.println("\t5. Exit");
        return getMenuNumberFromUserInRange("Enter your choice:", 1, 5);
    }

    private void createContacts() {
        //Creating contacts
        boolean continueCreating = true;

        while (continueCreating) {
            contacts.createNewContact();

            String input = getYesNoChoiceFromUser("Do you want to create another contact?");
            if (input.equals("no")) {
                continueCreating = false;
            }
        }
    }

    private void findContactMenu() {
        //find contact
        Contact contact = null;

        while (contact == null) {
            IO.println("Find contact by ...");
            IO.println("\t1. First name");
            IO.println("\t2. Last name");
            IO.println("\t3. Email");
            IO.println("\t4. Return to main menu");

            int choice = getMenuNumberFromUserInRange("Enter your choice:", 1, 4);
            IO.println();

            switch (choice) {
                case 1:
                    contact = contacts.findPersonByFirstName();
                    break;
                case 2:
                    contact = contacts.findPersonByLastName();
                    break;
                case 3:
                    contact = contacts.findPersonByEmail();
                    break;
                case 4:
                    return; // return to main menu
                default:
                    IO.println("Invalid choice. Please try again.");
                    break;
            }
        }

        showContactMenu(contact);
    }

    private void showContactMenu(Contact contact) {
        boolean continueContactMenu = true;
        while (continueContactMenu) {
            IO.println("*** Contact found ***  \n" + contact.toString());
            IO.println("--------------------");
            IO.println("What do you want to do?");
            IO.println("\t1. Edit contact");
            IO.println("\t2. Delete contact");
            IO.println("\t3. Return to main menu");

            int choice = getMenuNumberFromUserInRange("Enter your choice:", 1, 3);

            switch (choice) {
                //edit contact details
                case 1:
                    contacts.editContact(contact);
                    IO.println("----------------\n");
                    break;

                //delete contact  
                case 2:
                    contacts.deleteContact(contact);
                    continueContactMenu = false; // exit the contact menu after deletion
                    IO.println("----------------\n");
                    break;

                //do nothing and return to main menu
                case 3:
                    IO.println();
                    return;
                default:
                    IO.println("Invalid choice. Please try again.");
                    break;
            }

        }
    }


    //Optional 1 
    //Sorting
    private void showSortMenu() {

        IO.println("Sort after ...");
        IO.println("\t1. First name A-Z");
        IO.println("\t2. First name Z-A");
        IO.println("\t3. Last name A-Z");
        IO.println("\t4. Last name Z-A");
        IO.println("\t5. Return to main menu");

        int choice = getMenuNumberFromUserInRange("Enter your choice:", 1, 5);
        switch (choice) {
            case 1:
                contacts.sortByFirstName(false);
                break;
            case 2:
                contacts.sortByFirstName(true); 
                break;
            case 3:
                contacts.sortByLastName(false);
                break;
            case 4:
                contacts.sortByLastName(true);
                break;
                
                //Back to main menu
            case 5:
                return;
        }
        
        contacts.printAllContacts();
    }


    // **** USER INPUTS ****
    public static String getYesNoChoiceFromUser(String message) {
        String choice = "";
        boolean validInput = false;

        while (!validInput) {
            String userInput = IO.readln(message + " (yes/no): ");

            if (userInput.equalsIgnoreCase("yes") || userInput.equalsIgnoreCase("no")) {
                choice = userInput.toLowerCase();
                validInput = true;
            } else {
                IO.println("Invalid input. Please enter 'yes' or 'no'.");
            }

        }
        return choice;
    }

    //getting a number from the user from min to max, includes validation checks so that the program continues only 
    //when a valid number is being entered by the user
    public static int getMenuNumberFromUserInRange(String message, int minIncl, int maxIncl) {
        int menuNumber = 0;
        boolean validInput = false;

        while (!validInput) {
            String userInput = IO.readln(message + " (" + minIncl + "-" + maxIncl + "): ");

            try {
                menuNumber = Integer.parseInt(userInput);
                validInput = menuNumber >= minIncl && menuNumber <= maxIncl;

            } catch (NumberFormatException e) {
            }

            if (!validInput) {
                IO.println("Invalid choice. Please enter a number between " + minIncl + " and " + maxIncl + ".");
            }
        }
        return menuNumber;
    }

}
