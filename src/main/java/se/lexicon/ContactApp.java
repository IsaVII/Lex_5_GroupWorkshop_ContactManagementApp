package se.lexicon;

import java.util.ArrayList;
import java.util.Comparator;

public class ContactApp {

    ArrayList<Person> contacts = new ArrayList<>();
    
    void main() {

        boolean continueCreating = true;
        
        while (continueCreating) {
            createNewContact();
            
           String input = IO.readln("Do you want to create a new contact (yes/no):");
           if (input.equals("no")) {
                continueCreating = false;
            }

        }

        sortListByFirstName();
        printAllContacts();
        deletePerson();
        printAllContacts();
    }
    
    private void createNewContact() {
        Person newPerson = new Person();
        
        newPerson.setFirstName(IO.readln("Enter first name: "));
        newPerson.setLastName(IO.readln("Enter last name: "));
        newPerson.setPhoneNumber(IO.readln("Enter phone number: "));
        newPerson.setEmail(IO.readln("Enter email: "));
        IO.println("Contact created successfully!");
        IO.println("--------------------\n");

        contacts.add(newPerson);
    }

    private void printAllContacts(){

        IO.println("\n---- CONTACTS ---- ");
        for (int i = 0; i < contacts.size(); i++) {
            IO.println((i + 1) + ". ");
            IO.println(contacts.get(i).toString());
            IO.print("--------------------\n");
        }
    }
    
    //Get a person
    private Person findPersonByFirstName(){
        String name = IO.readln("Enter the first name you want to search: ");
        return  findPersonByFirstName(name);
    }
    
    private Person findPersonByFirstName(String name)
    {
        for (Person person : contacts) {
            if (person.getFirstName().equalsIgnoreCase(name)) {
                return person;
            }
        }
        return null;
    }
    
    //delete a person 
    private void deletePerson(){
        IO.println("Delete a contact");
        Person person = findPersonByFirstName();
        
        if (person == null) {
            IO.println("Person not found.");
            return;
        }
        
        contacts.remove(person);
        IO.println("Contact deleted successfully!");
    }
     
    
    //edit a person
    private void editPerson(Person person) {
        if (person == null) {
            IO.println("Person not found.");
            return;
        }
        
        IO.println("\nEditing contact:\n" + person + "\n");
        
        String newFirstName = IO.readln("Enter new first name (leave blank to keep current): ");
        if (!newFirstName.isEmpty()) {
            person.setFirstName(newFirstName);
        }
        
        String newLastName = IO.readln("Enter new last name (leave blank to keep current): ");
        if (!newLastName.isEmpty()) {
            person.setLastName(newLastName);
        }
        
        String newPhoneNumber = IO.readln("Enter new phone number (leave blank to keep current): ");
        if (!newPhoneNumber.isEmpty()) {
            person.setPhoneNumber(newPhoneNumber);
        }
        
        String newEmail = IO.readln("Enter new email (leave blank to keep current): ");
        if (!newEmail.isEmpty()) {
            person.setEmail(newEmail);
        }
        
        IO.println("Contact updated successfully!");
    }

    //Sorting methods
    private void sortListByFirstName(){
        contacts.sort(Comparator.comparing(Person::getFirstName));
        
    }
    
    private void sortListByFirstNameReversed(){
        contacts.sort(Comparator.comparing(Person::getFirstName).reversed());
    }
    
    //alternative to comparator 
    private void comparatorSortListByFirstName(){
        contacts.sort( new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getFirstName().compareTo(p2.getFirstName());
            }
        });
    }
  
}
