package se.lexicon;

import java.util.ArrayList;
import java.util.Comparator;

public class Contacts {

    private ArrayList<Contact> contacts = new ArrayList<>();

    //add new contact and auto sort alphabetically by first name
    public void addNewContact(Contact contact) {
        contacts.add(contact);
        contacts.sort(Comparator.comparing(Contact::getFirstName));
    }

    public void createNewContact() {
        Contact newContact = new Contact();

        String firstName = IO.readln("Enter first name: ");
        while (firstName.isEmpty()) {
            firstName = IO.readln("First name can't be empty. Enter first name: ");
        }
        newContact.setFirstName(firstName);

        newContact.setLastName(IO.readln("Enter last name: "));
        newContact.setPhoneNumber(IO.readln("Enter phone number: "));
        newContact.setEmail(IO.readln("Enter email: "));
        IO.println("Contact created successfully!");
        IO.println("--------------------\n");

        addNewContact(newContact);
    }

    public void printAllContacts(){

        IO.println("\n---- CONTACTS ---- ");
        for (int i = 0; i < contacts.size(); i++) {
            IO.println((i + 1) + ". ");
            //getting the contact and printing it -> the contact toString is set in the Contact class
            IO.println(contacts.get(i).toString());
            IO.print("--------------------\n");
        }
    }

    //Find a person by first name, last name or email
    //case-insensitive search, return null if not found
    public Contact findPersonByFirstName(){
        String name = IO.readln("Enter the first name you want to search: ");
        return  findPersonByFirstName(name);
    }

    public Contact findPersonByLastName() {
        String name = IO.readln("Enter the last name you want to search: ");
        return  findPersonByLastName(name);
    }

    public Contact findPersonByEmail() {
        String email = IO.readln("Enter the email you want to search: ");
        return  findPersonByEmail(email);
    }

    public Contact findPersonByFirstName(String name)
    {
        for (Contact contact : contacts) {
            if (contact.getFirstName().equalsIgnoreCase(name)) {
                return contact;
            }
        }
        return null;
    }
    
    public Contact findPersonByLastName(String name)
    {
        for (Contact person : contacts) {
            if (person.getLastName().equalsIgnoreCase(name)) {
                return person;
            }
        }
        return null;
    }
    
    public Contact findPersonByEmail(String email)
    {
        for (Contact contact : contacts) {
            if (contact.getEmail().equalsIgnoreCase(email)) {
                return contact;
            }
        }
        return null;
    }

    //delete a contact 
    public void deleteContact(Contact contact) {
        if (contact == null) {
            IO.println("Contact not found.");
            return;
        }

        contacts.remove(contact);
        IO.println("Contact deleted successfully!");
    }


    //edit a contact
    public void editContact(Contact contact) {
        if (contact == null) {
            IO.println("Contact not found.");
            return;
        }

        IO.println("\nEditing contact:\n" + contact + "\n");

        String newFirstName = IO.readln("Enter new first name (leave blank to keep current): ");
        if (!newFirstName.isEmpty()) {
            contact.setFirstName(newFirstName);
        }

        String newLastName = IO.readln("Enter new last name (leave blank to keep current): ");
        if (!newLastName.isEmpty()) {
            contact.setLastName(newLastName);
        }

        String newPhoneNumber = IO.readln("Enter new phone number (leave blank to keep current): ");
        if (!newPhoneNumber.isEmpty()) {
            contact.setPhoneNumber(newPhoneNumber);
        }

        String newEmail = IO.readln("Enter new email (leave blank to keep current): ");
        if (!newEmail.isEmpty()) {
            contact.setEmail(newEmail);
        }

        IO.println("Contact updated successfully!");
    }

    //Sorting methods
    public void sortByFirstName(boolean reversed) {
        if (reversed) {
            contacts.sort(Comparator.comparing(Contact::getFirstName).reversed());
        } else {
            contacts.sort(Comparator.comparing(Contact::getFirstName));
        }
    }

    public void sortByLastName(boolean reversed) {
        if (reversed) {
            contacts.sort(Comparator.comparing(Contact::getLastName).reversed());
        } else {
            contacts.sort(Comparator.comparing(Contact::getLastName));
        }
    }
    
    public void sortByEmail(boolean reversed) {
        if (reversed) {
            contacts.sort(Comparator.comparing(Contact::getEmail).reversed());
        } else {    
            contacts.sort(Comparator.comparing(Contact::getEmail));
        }
    }

    //alternative to comparator 
    private void comparatorSortListByFirstName(){
        contacts.sort( new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getFirstName().compareTo(c2.getFirstName());
            }
        });
    }
    
    public ArrayList<Contact> getContacts() {
        return contacts;
    }


    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
