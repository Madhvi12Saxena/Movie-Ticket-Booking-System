# Movie Ticket Booking System

## Description
The Movie Ticket Booking System is a Java-based console application that allows users to browse available movies, book tickets, and view their bookings. This system interacts with an Oracle database to manage movie and booking data.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contributing](#contributing)

## Installation
1. Clone the repository to your local machine: git clone https://github.com/yourusername/movie-ticket-booking-system.git

2. Set up the Oracle database and ensure you have the necessary JDBC driver.

3. Update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` constants in the `MovieTicketBookingSystem.java` file with your database connection details.

4. Compile the Java code:`javac MovieTicketBookingSystem.java`

5. Run the application:`java MovieTicketBookingSystem`

## Usage
1. Select options from the menu:
- Option 1: Display Available Movies - Lists the available movies with details.
- Option 2: Book Tickets - Allows you to book tickets for a selected movie.
- Option 3: View Bookings - Displays your booking history.
- Option 4: Exit - Exits the application.

2. Follow the on-screen prompts to interact with the system.

## Configuration
- Database Configuration: Update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` constants in the `MovieTicketBookingSystem.java` file with your database connection details.

## Contributing
Contributions are welcome! If you'd like to contribute to this project, please follow these guidelines:
- Fork the repository.
- Create a new branch for your feature or bug fix.
- Make your changes and commit them.
- Push your changes to your fork.
- Submit a pull request to the main repository.
