package se.lexicon;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveLoadContacts {

    final static String FILE_PATH = "contacts.csv";

    public static void saveContacts(ArrayList<Contact> contacts) {
        //Save contacts as a CSV file

        //Create CSV text
        StringBuilder sb = new StringBuilder();
        for (Contact contact : contacts) {
            sb.append(contact.getId()).append(",");
            sb.append(contact.getFirstName()).append(",");
            sb.append(contact.getLastName()).append(",");
            sb.append(contact.getPhoneNumber()).append(",");
            sb.append(contact.getEmail()).append("\n");
        }
        
        //Write CSV text to file
        try {
            FileWriter myWriter = new FileWriter(FILE_PATH);
            myWriter.write(sb.toString());
            myWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    
    public static ArrayList<Contact> loadContacts() {
        //Load contacts from CSV file
        ArrayList<Contact> contacts = new ArrayList<>();

        try (FileReader fileReader = new FileReader(FILE_PATH)) {
            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
            String[] lines = sb.toString().split("\n");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Contact contact = new Contact();
                    contact.setId(Integer.parseInt(parts[0]));
                    contact.setFirstName(parts[1]);
                    contact.setLastName(parts[2]);
                    contact.setPhoneNumber(parts[3]);
                    contact.setEmail(parts[4]);
                    contacts.add(contact);
                }
            }

            fileReader.close();
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return contacts;
    }
}
