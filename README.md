# Contact Management App ![CI](https://github.com/IsaVII/Lex_5_GroupWorkshop_ContactManagementApp/actions/workflows/ci.yml/badge.svg)

A console-based Java application for managing a list of contacts: create, view, find, edit, delete, sort, and persist them to a CSV file. It was built as a group workshop project to practice core Java and object-oriented programming concepts, so the explanations below are written for people who are still new to Java.

## Features

- Create one contact, or keep creating contacts in a row until you say "no"
- View all contacts, which are kept sorted alphabetically by first name automatically whenever a contact is added
- Find a contact by first name, last name, or email (case-insensitive)
- Edit a found contact's details (leave a field blank to keep its current value)
- Delete a found contact
- Sort all contacts by first name or last name, ascending or descending, from a dedicated menu
- Contacts are loaded from `contacts.csv` on startup and saved back to it on exit, so your data survives between runs
- Input validation: menu choices and yes/no prompts are re-asked until the user types something valid

## Tech stack

- **Java 27** — the app is written in the modern "unnamed class / instance main method" style, so `ContactApp` has no `public static void main(String[] args)`; instead it has a plain `void main()`.
- **`java.io.IO`** — the newer, simplified console I/O API (`IO.println(...)`, `IO.readln(...)`) used instead of `Scanner` or `System.out`.
- **`java.util.ArrayList`** — stores the contacts in memory while the app runs.
- **`java.util.Comparator`** — used to sort contacts by different fields.
- **`java.io.FileReader` / `java.io.FileWriter`** — used by `SaveLoadContacts` to read and write the contacts as plain CSV text.
- **Maven** — used for project/build configuration (`pom.xml`).

## Getting started

### Prerequisites

- JDK 27 (the `pom.xml` targets Java 27; the instance-main-method and `IO` features used here need a recent JDK)
- Maven (optional — only needed if you want to build via `mvn`; an IDE like IntelliJ IDEA can run the project directly)

### Installation

```bash
git clone https://github.com/IsaVII/Lex_5_GroupWorkshop_ContactManagementApp.git
cd Lex_5_GroupWorkshop_ContactManagementApp
```

### Running

There is no packaged "main class" plugin configured in `pom.xml`, so the simplest ways to run the app are:

**From an IDE (recommended):** open the project, open `src/main/java/se/lexicon/ContactApp.java`, and run its `main()` method.

**From the command line:**

```bash
javac -d out src/main/java/se/lexicon/*.java
java -cp out se.lexicon.ContactApp
```

On startup the app reads `contacts.csv` from the project's working directory (via `SaveLoadContacts.loadContacts()`) and loads whatever contacts are already saved there. When the app exits (menu option 5), it writes the current contact list back to `contacts.csv` (via `SaveLoadContacts.saveContacts(...)`), overwriting the file. If `contacts.csv` doesn't exist yet, loading it will fail — make sure a (possibly empty) `contacts.csv` file exists before the first run.

## Usage

The app is driven entirely by typed numbers. The main menu looks like this:

```
==================
*** Main Menu ***
==================
What do you want to do?
    1. Print all contacts
    2. Create new contacts
    3. Find a contact by first name
    4. Sort contacts
    5. Exit
```

- **1 — Print all contacts**: lists every contact currently in memory.
- **2 — Create new contacts**: asks for first name, last name, phone number, and email, then asks "Do you want to create another contact?" (yes/no) so you can add several in a row.
- **3 — Find a contact**: opens a sub-menu to search by first name, last name, or email. Once a contact is found, you get another sub-menu to **edit** or **delete** it, or return to the main menu.
- **4 — Sort contacts**: opens a sub-menu with options to sort by first name (A-Z or Z-A) or last name (A-Z or Z-A), or return to the main menu. After sorting, the full contact list is printed.
- **5 — Exit**: saves all contacts back to `contacts.csv` and quits the app.

## Code walkthrough (for newer programmers)

The project has four classes in `src/main/java/se/lexicon/`. Here's what each one does and why.

### `Contact.java` — the blueprint for one contact

A **class** is a blueprint for creating objects. `Contact` describes what every single contact looks like:

- **Fields** (the data each contact holds):  `firstName`, `lastName`, `phoneNumber`, `email`.
- **Constructors**: a no-argument constructor `Contact()` (makes an empty contact you fill in later) and a constructor that takes all five values at once.
- **Getters and setters**: methods like `getFirstName()` and `setFirstName(...)` let other classes read or change a contact's fields without touching them directly. This pattern is called **encapsulation** — the fields are `private`, so they can only be read or changed through these methods.
- **`toString()`**: this overrides the default way Java turns an object into text, so printing a `Contact` shows a nicely formatted name, phone number, and email instead of something like `Contact@1a2b3c`.

Each `Contact` object created from this blueprint is one actual contact, e.g. "John Doe".

### `Contacts.java` — the manager that holds all contacts

This class owns a `private ArrayList<Contact> contacts` — a resizable list that can only be accessed from outside the class through `getContacts()` and `setContacts(ArrayList<Contact>)`, another example of **encapsulation**. It contains all the logic for working with the whole collection of contacts:

- `addNewContact(contact)` adds a contact to the list and re-sorts the list by first name.
- `createNewContact()` asks the user for first name, last name, phone number, and email via `IO.readln(...)` and builds a new `Contact` from the raw input — note that it does **not** currently check whether any of these fields are left blank.
- `printAllContacts()` loops over the list with a `for` loop and prints each contact.
- `findPersonByFirstName(...)`, `findPersonByLastName(...)`, `findPersonByEmail(...)` each loop through the list with a `for-each` loop and an `if` check (`equalsIgnoreCase`) to find a matching contact, returning `null` if nothing matches. Each one also has a no-argument overload (e.g. `findPersonByFirstName()`) that first asks the user for the search term via `IO.readln(...)` and then calls the version that takes the term as a parameter.
- `editContact(contact)` and `deleteContact(contact)` modify or remove a specific `Contact` object; both print a message and return early if `contact` is `null`.
- `sortByFirstName(boolean reversed)`, `sortByLastName(boolean reversed)`, `sortByEmail(boolean reversed)` use `Comparator.comparing(Contact::getFirstName)` (or last name / email) — a short way of saying "sort these objects by comparing the result of calling that getter on each one" — and call `.reversed()` on the comparator when `reversed` is `true`.
- `comparatorSortListByFirstName()` is a private, unused-by-the-menu method kept as an example of writing the same first-name sort with an explicit anonymous `Comparator<Contact>` instead of `Comparator.comparing(...)`.

This class demonstrates **collections** (storing many objects in one structure) and keeps all "list management" logic in one place, separate from the menu/input code.

### `SaveLoadContacts.java` — reading and writing contacts to disk

This class has two `static` methods that make the contact list persistent across runs by storing it as a simple CSV (comma-separated values) file at `contacts.csv`:

- `saveContacts(ArrayList<Contact> contacts)` builds one CSV line per contact (`id,firstName,lastName,phoneNumber,email`) using a `StringBuilder`, then writes the whole thing to `contacts.csv` with a `FileWriter`.
- `loadContacts()` reads `contacts.csv` character by character with a `FileReader`, splits the text into lines and then each line into its five comma-separated parts, and rebuilds a `Contact` object (and the `ArrayList<Contact>` containing all of them) from that data.
- Both methods wrap their file operations in a `try`/`catch` and rethrow any `IOException` as an unchecked `RuntimeException`, since this is a small console app without more elaborate error handling.

`ContactApp` calls `loadContacts()` once at startup and `saveContacts(...)` once at shutdown, so this class is the bridge between "contacts in memory" (an `ArrayList<Contact>` inside `Contacts`) and "contacts on disk" (`contacts.csv`).

### `ContactApp.java` — the menu and the program's entry point

This is where the program starts and where the user interacts with everything.

- `void main()` is the entry point. Instead of the traditional `public static void main(String[] args)`, it uses Java's newer, simpler instance-main-method style, which is allowed for small programs like this one. It loads contacts from disk, runs the menu loop, then saves contacts back to disk when the loop ends.
- `runApp()` contains the **main loop**: a `while (true)` loop that shows the menu, reads the user's choice, and uses a `switch` statement to decide what to do. Choosing "5. Exit" uses `return` to break out of the loop (and the method).
- `showMainMenu()`, `findContactMenu()`, `showContactMenu(...)`, and `showSortMenu()` each print their own small menu and use the same switch-based pattern. `findContactMenu()` keeps looping until a contact is actually found (`while (contact == null)`), and `showContactMenu(...)` lets you edit or delete the contact you just found.
- `createContacts()` uses a `while` loop together with a yes/no prompt so you can create several contacts without going back to the main menu each time.

### Input validation

Two small static helper methods make sure the app never crashes or gets stuck on bad input:

- `getMenuNumberFromUserInRange(message, minIncl, maxIncl)` keeps asking the user for a number until they type something that both (a) parses as an integer (using a `try`/`catch` around `Integer.parseInt(...)` to catch bad text) and (b) falls within the allowed range. The surrounding `while (!validInput)` loop is what makes it "keep asking."
- `getYesNoChoiceFromUser(message)` works the same way, but only accepts the words "yes" or "no" (case-insensitive).

Note that this kind of validation only applies to menu numbers and yes/no answers — the free-text fields asked for in `createNewContact()` (first name, last name, phone number, email) are not validated and can be left empty.

### OOP concepts used, in plain language

| Concept | Where it shows up |
|---|---|
| Class | `Contact`, `Contacts`, `SaveLoadContacts`, `ContactApp` — each is a blueprint for a type of thing or a group of related behavior |
| Object | Each individual contact created with `new Contact(...)` |
| Field | `firstName`, `lastName`, etc. — data stored inside an object |
| Constructor | `Contact()` and `Contact(id, firstName, ...)` — special methods that build a new object |
| Method | Named blocks of reusable code, e.g. `printAllContacts()`, `editContact(...)` |
| Getter / setter | `getFirstName()` / `setFirstName(...)` on `Contact`, and `getContacts()` / `setContacts(...)` on `Contacts` — controlled access to private fields |
| Encapsulation | Fields in `Contact` are `private`, and so is the `contacts` list inside `Contacts`; both are only accessible through getters/setters |
| Loop | `while` loops (menus keep repeating) and a `for` / `for-each` loop (looping through the contact list) |
| Conditional | `if`/`else` (e.g. checking for blank input when editing) and `switch` (menu choice handling) |
| Collection | `ArrayList<Contact>` — a list that can grow and shrink |
| Comparator | A reusable way of telling Java *how* to sort objects that aren't naturally ordered |
| Static method | `SaveLoadContacts.saveContacts(...)` / `loadContacts()` — utility methods called on the class itself, not on an instance |

## Running tests

There are currently no automated tests in the `main` branch of this project (no `src/test` directory, and `pom.xml` has no test dependencies). <!-- TODO: a JUnit 5 test suite for Contacts (ContactsTest.java) with add/find/edit/delete positive and negative cases exists on the separate "Optional_2" branch but has not been merged into main — confirm whether it should be merged before documenting a `mvn test` workflow here. -->

## Project structure

```
.
├── pom.xml                                   # Maven project configuration (Java 27)
├── contacts.csv                              # CSV data file: persisted contacts (id,firstName,lastName,phoneNumber,email)
├── Report_Group_1.md                         # Workshop planning notes: team, requirements, data design
└── src/
    └── main/
        ├── java/se/lexicon/
        │   ├── Contact.java                  # Blueprint for a single contact (fields, getters/setters, toString)
        │   ├── Contacts.java                 # Manages the ArrayList of contacts: add, find, edit, delete, sort
        │   ├── SaveLoadContacts.java          # Reads/writes contacts.csv to persist contacts between runs
        │   └── ContactApp.java               # Entry point, console menu loop, input validation helpers
        └── resources/
            ├── Arrays-Collections-Workshop.md # Original workshop assignment and requirements
            └── Trello.jpg                     # Screenshot of the team's Trello planning board
```

## Known limitations

- `createNewContact()` does not validate some of its input fields - last name, phone number, or email can be left blank when creating a contact.

