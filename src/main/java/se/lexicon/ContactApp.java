package se.lexicon;

import java.util.ArrayList;

public class ContactApp {

    ArrayList<Person> contacts = new ArrayList<>();
    
    void main() {
        
        createNewContact();
        printAllContacts();
        
        

    }
    
    private void createNewContact() {
        Person newPerson = new Person();
        
        newPerson.setFirstName(IO.readln("Enter first name: "));
        newPerson.setLastName(IO.readln("Enter last name: "));
        newPerson.setPhoneNumber(IO.readln("Enter phone number: "));
        newPerson.setEmail(IO.readln("Enter email: "));

        contacts.add(newPerson);
    }
    
    void printAllContacts(){

        IO.println("\n---- CONTACTS ---- ");
        for (int i = 0; i < contacts.size(); i++) {
            IO.println((i + 1) + ". ");
            IO.println(contacts.get(i).toString());
            IO.print("--------------------");
        }
    }
}
