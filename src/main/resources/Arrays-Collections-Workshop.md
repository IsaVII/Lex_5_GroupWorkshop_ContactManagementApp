![Lexicon Logo](https://lexicongruppen.se/media/wi5hphtd/lexicon-logo.svg)

# Workshop – Contact Management App

## The Scenario

You are building a console-based contact management application.

Think about what a contact manager should do and how a user would interact with it — then build it.

**Create `ContactApp.java`** inside `src/main/java/se/lexicon/`.  
Commit regularly as you make progress.

---

## Requirements

Before writing any code, think through the following:

- **Functionality** — what operations should a contact manager support? What would a user expect to be able to do?
- **Data** — what information does a contact hold? How should that be represented given your chosen data structure?
- **Data structure** — which structure fits the way contacts will be stored and accessed? Consider the trade-offs.
- **Constraints** — what rules should the system enforce? Think about what invalid states should never be allowed.
- **Code organisation** — how will you separate concerns? Each operation should have a clear home in your code.

> **Programing Principles to keep in mind:**
> - **SRP** *(Single Responsibility)* — each class and method should have one clear job.
> - **Separation of Concerns** — keep your menu logic, your data storage, and your business rules in separate places.
> - **KISS** *(Keep It Simple)* — prefer a straightforward solution over a clever one. If it's hard to explain, it's probably too complex.
> - **DRY** *(Don't Repeat Yourself)* — if you find yourself writing the same logic in more than one place, extract it into a method.
> - **YAGNI** *(You Aren't Gonna Need It)* — don't build features that haven't been asked for. Solve today's problem first.

---

## Optional Challenges

Once the core app works, try extending it:
- create a new branch for each challenge
- commit your work regularly
- merge your branch into the main branch when you're done

---

### Challenge 1 – Sort Contacts

Display all contacts sorted alphabetically by name.

---

### Challenge 2 – Unit Tests

Write unit tests for each data-access operation: add, search, delete, and update.

- Use JUnit 5 and place tests in `src/test/java/se/lexicon/`
- Each method should have at least one positive test case and one negative test case

---

### Challenge 3 – File I/O

**Export:** Save all contacts to a file when the user exits.  
**Import:** Load contacts from that file automatically when the app starts.

Support at least one format:
- Plain text — one contact per line, in a format that matches your storage approach
- CSV
- Excel (`.xlsx`)

---

## Submission

Push your project to GitHub and share the repository link with your instructor.