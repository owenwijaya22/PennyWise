# PennyWise - Personal Finance Management System

The project emerged from recognizing several key challenges:

1. Financial Literacy Gap

- Many individuals struggle with basic budgeting and expense tracking
- Limited understanding of spending patterns and financial habits
- Need for a simple yet comprehensive tool to learn financial management

2. Student-Specific Needs

- Integration of student/staff discount systems
- Managing limited income effectively
- Balancing academic expenses with daily needs

3. Digital Solution Requirements

- Need for offline accessibility (local file storage)
- Data privacy concerns with cloud-based solutions
- Desire for a lightweight, no-subscription alternative to commercial apps

## Features

- 👤 User Authentication
  - Register new users
  - Login/logout functionality
  - Account management

- 📦 Data Storage
  - Save user data to a local file
  - Load user data from a local file
  - Data persistence across sessions
- 💰 Transaction Management
  - Add income and expense transactions
  - Integrate with student/staff discount deals, or customize your own!
  - Categorize transactions
  - View transaction history
- 📊 Budget Planning
  - Set monthly budgets
  - Update existing budgets
  - Budget limit warnings
- 📈 Financial Analysis
  - Calculate total income
  - Track monthly expenses/income, as well as by category
  - Monitor current balance
  - Analyze spending patterns

## Getting Started

### Prerequisites (if you don't have Eclipse)

- **Java SE Development Kit 21 or higher**
  - Download here: [Java SE Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (recommended version 21)
  - Ensure your JDK is installed and added to the PATH environment variables:
    1. Right-click on My Computer and select Properties.
    2. On the Advanced tab, select Environment Variables.
    3. Edit the PATH variable to point to the location of the Java Runtime Environment (JRE).
       - Example: `C:\Program Files\Java\jdk-21\bin`
    - [Environment Variable Setup Preview](https://imgur.com/a/SyW6GX6)

### Running the Application

#### With Eclipse

1. Open the terminal inside Eclipse (CTRL + ALT + T) and navigate to the `Release/` directory.
2. Run the `PennyWise.jar` file using the command:
   `java -jar PennyWise.jar`
3. [Running Preview with Eclipse](https://imgur.com/a/OiWP5ky)
4. Register, login, and start managing your finances!

#### With JDK 21

- Open the terminal and cd into the Release/ directory
- Run the PennyWise.jar file using the command `java -jar PennyWise.jar`
- [Running Preview with JDK 21](https://imgur.com/a/j7lVg5c)
- Register, login, and start managing your finances!

`
## Documentation

1. Check the basic documentation at ./PennyWise/doc/index.html
2. Source code files are also annotated with JAutoDoc comments
3. Test files are manually annotated

<div style="page-break-before: always;" />

## Project Structure

```
PennyWise/
├── bin/ # Compiled bytecode
├── doc/ # JavaDoc documentation
├── pennywise_data/ # Runtime generated data files, the program has a failsafe system to automatically create the dir if it doesn't exist.
├── test_data/ # Data files for JUnit tests
├── release/ # Release .jar binary
├── src/pennywise/ # Source code inside pennywise package
│ ├── interfaces/
│ │ ├── IDataStorage.java
│ │ └── TransactionCategory.java
│ ├── model/
│ │ ├── Budget.java
│ │ ├── Discount.java
│ │ ├── ExpenseCategory.java
│ │ ├── IncomeCategory.java
│ │ ├── Transaction.java
│ │ └── User.java
│ ├── service/
│ │ ├── AuthenticationService.java
│ │ ├── BudgetManager.java
│ │ ├── TransactionAnalyzer.java
│ │ └── TransactionManager.java
│ ├── storage/
│ │ └── FileDataStorage.java
│ ├── ui/
│ │ ├── handlers/
│ │ │ ├── AccountHandler.java
│ │ │ ├── BudgetHandler.java
│ │ │ ├── DiscountHandler.java
│ │ │ ├── Handler.java
│ │ │ ├── MenuHandler.java
│ │ │ └── TransactionHandler.java
│ │ ├── ConsoleUI.java
│ │ └── UIConstants.java
│ ├── utils/
│ │ └── DiscountManager.java
│ └── PennyWise.java
├── src/test/ # All the relevant tests are included in the dir
│ └── pennywise/
│ ├── model/
│ ├── service/
│ ├── storage/
│ ├── stubs/
│ ├── ui-handlers/****
│ └── PennyWiseTest.java
```
