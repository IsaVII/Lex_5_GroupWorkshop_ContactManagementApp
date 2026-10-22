package se.lexicon;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Contacts")
class ContactsTest {

    private Contacts contacts;
    private InputStream originalSystemIn;

    @BeforeEach
    void setUp() {
        contacts = new Contacts();
        originalSystemIn = System.in;
    }

    @AfterEach
    void tearDown() {
        // Restore System.in and clear java.lang.IO's cached reader so it
        // doesn't keep pointing at a test's ByteArrayInputStream afterwards.
        System.setIn(originalSystemIn);
        resetIoReader();
    }

    private Contact contact( String firstName, String lastName, String phone, String email) {
       Contact contact = new Contact();
       contact.setFirstName(firstName);
       contact.setLastName(lastName);
       contact.setPhoneNumber(phone);
       contact.setEmail(email);
       return contact;
    }

    /**
     * Feeds the given lines (one per IO.readln() prompt, in order) to java.lang.IO
     * by swapping System.in and clearing IO's cached BufferedReader, since that
     * reader is created once and then cached for the lifetime of the JVM.
     */
    private void simulateInput(String... lines) {
        String data = String.join("\n", lines) + "\n";
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        resetIoReader();
    }

    private void resetIoReader() {
        try {
            Field readerField = Class.forName("java.lang.IO").getDeclaredField("br");
            readerField.setAccessible(true);
            readerField.set(null, null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not reset java.lang.IO's cached reader for testing", e);
        }
    }

    @Nested
    @DisplayName("addNewContact()")
    class AddNewContact {

        @Test
        @DisplayName("Adds a contact so it can be found afterwards")
        void addNewContact_addsContactToList() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");

            contacts.addNewContact(alice);

            assertEquals(alice, contacts.findPersonByFirstName("Alice"));
        }

        @Test
        @DisplayName("Keeps the contact list sorted by first name after adding")
        void addNewContact_keepsListSortedByFirstName() {
            Contact charlie = contact("Charlie", "Chaplin", "0701111111", "charlie@example.com");
            Contact alice = contact("Alice", "Andersson", "0702222222", "alice@example.com");
            Contact bob = contact("Bob", "Builder", "0703333333", "bob@example.com");

            contacts.addNewContact(charlie);
            contacts.addNewContact(alice);
            contacts.addNewContact(bob);

            assertAll(
                    () -> assertEquals("Alice", contacts.contacts.get(0).getFirstName()),
                    () -> assertEquals("Bob", contacts.contacts.get(1).getFirstName()),
                    () -> assertEquals("Charlie", contacts.contacts.get(2).getFirstName())
            );
        }
    }

    @Nested
    @DisplayName("findPersonByFirstName(String)")
    class FindPersonByFirstName {

        @Test
        @DisplayName("Finds a contact ignoring case")
        void findPersonByFirstName_existingNameDifferentCase_returnsContact() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            Contact found = contacts.findPersonByFirstName("aLICE");

            assertEquals(alice, found);
        }

        @Test
        @DisplayName("Returns null when no contact matches the first name")
        void findPersonByFirstName_unknownName_returnsNull() {
            contacts.addNewContact(contact("Alice", "Andersson", "0701111111", "alice@example.com"));

            Contact found = contacts.findPersonByFirstName("Zelda");

            assertNull(found);
        }
    }

    @Nested
    @DisplayName("findPersonByLastName(String)")
    class FindPersonByLastName {

        @Test
        @DisplayName("Finds a contact ignoring case")
        void findPersonByLastName_existingNameDifferentCase_returnsContact() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            Contact found = contacts.findPersonByLastName("ANDERSSON");

            assertEquals(alice, found);
        }

        @Test
        @DisplayName("Returns null when no contact matches the last name")
        void findPersonByLastName_unknownName_returnsNull() {
            contacts.addNewContact(contact("Alice", "Andersson", "0701111111", "alice@example.com"));

            Contact found = contacts.findPersonByLastName("Smith");

            assertNull(found);
        }
    }

    @Nested
    @DisplayName("findPersonByEmail(String)")
    class FindPersonByEmail {

        @Test
        @DisplayName("Finds a contact ignoring case")
        void findPersonByEmail_existingEmailDifferentCase_returnsContact() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            Contact found = contacts.findPersonByEmail("ALICE@EXAMPLE.COM");

            assertEquals(alice, found);
        }

        @Test
        @DisplayName("Returns null when no contact matches the email")
        void findPersonByEmail_unknownEmail_returnsNull() {
            contacts.addNewContact(contact("Alice", "Andersson", "0701111111", "alice@example.com"));

            Contact found = contacts.findPersonByEmail("nobody@example.com");

            assertNull(found);
        }
    }

    @Nested
    @DisplayName("deleteContact(Contact)")
    class DeleteContact {

        @Test
        @DisplayName("Removes the contact from the list")
        void deleteContact_existingContact_removesContact() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            contacts.deleteContact(alice);

            assertAll(
                    () -> assertNull(contacts.findPersonByFirstName("Alice")),
                    () -> assertTrue(contacts.contacts.isEmpty())
            );
        }

        @Test
        @DisplayName("Does nothing and does not throw when contact is null")
        void deleteContact_nullContact_doesNothing() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            assertDoesNotThrow(() -> contacts.deleteContact(null));

            assertFalse(contacts.contacts.isEmpty());
        }
    }

    @Nested
    @DisplayName("editContact(Contact)")
    class EditContact {

        @Test
        @DisplayName("Updates all fields when new values are provided")
        void editContact_allFieldsProvided_updatesContact() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            simulateInput("Alicia", "Svensson", "0709999999", "alicia@example.com");
            contacts.editContact(alice);

            assertAll(
                    () -> assertEquals("Alicia", alice.getFirstName()),
                    () -> assertEquals("Svensson", alice.getLastName()),
                    () -> assertEquals("0709999999", alice.getPhoneNumber()),
                    () -> assertEquals("alicia@example.com", alice.getEmail())
            );
        }

        @Test
        @DisplayName("Keeps original values when all answers are left blank")
        void editContact_blankAnswers_keepsOriginalValues() {
            Contact alice = contact("Alice", "Andersson", "0701111111", "alice@example.com");
            contacts.addNewContact(alice);

            simulateInput("", "", "", "");
            contacts.editContact(alice);

            assertAll(
                    () -> assertEquals("Alice", alice.getFirstName()),
                    () -> assertEquals("Andersson", alice.getLastName()),
                    () -> assertEquals("0701111111", alice.getPhoneNumber()),
                    () -> assertEquals("alice@example.com", alice.getEmail())
            );
        }

        @Test
        @DisplayName("Does nothing and does not throw when contact is null")
        void editContact_nullContact_doesNothing() {
            assertDoesNotThrow(() -> contacts.editContact(null));
        }
    }

    @Nested
    @DisplayName("createNewContact()")
    class CreateNewContact {

        @Test
        @DisplayName("Creates and adds a contact when all fields are provided")
        void createNewContact_validFirstName_addsContact() {
            simulateInput("Alice", "Andersson", "0701111111", "alice@example.com");

            contacts.createNewContact();

            Contact created = contacts.findPersonByFirstName("Alice");
            assertAll(
                    () -> assertEquals("Alice", created.getFirstName()),
                    () -> assertEquals("Andersson", created.getLastName())
            );
        }

    }
}
