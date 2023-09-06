package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class MovieTicketBookingSystem {

	private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "student";
    private static final String DB_PASSWORD = "student12";

    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Display Available Movies");
                System.out.println("2. Book Tickets");
                System.out.println("3. View Bookings");
                System.out.println("4. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        displayAvailableMovies(connection);
                        break;
                    case 2:
                        bookTickets(connection, scanner);
                        break;
                    case 3:
                        viewBookings(connection);
                        break;
                    case 4:
                    	System.out.println("Thanks for using the movie ticket booking system!");
                        connection.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAvailableMovies(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM movies");
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Available Movies:");
        System.out.println("ID\tName\t\t\tTime\t\tAvailable Seats");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (resultSet.next()) {
            int id = resultSet.getInt("movie_id");
            String name = resultSet.getString("movie_name");
            Timestamp time = resultSet.getTimestamp("movie_time");
            int availableSeats = resultSet.getInt("available_seats");

            String formattedTime = dateFormat.format(time);

            System.out.println(id + "\t" + name + "\t" + formattedTime + "\t\t" + availableSeats);
        }

        resultSet.close();
        statement.close();

        System.out.println(); // Add a newline to separate the movie list from the menu.
    }

    private static void bookTickets(Connection connection, Scanner scanner) throws SQLException {
        displayAvailableMovies(connection);

        System.out.print("Enter the ID of the movie you want to book: ");
        int movieId = scanner.nextInt();

        // Check if the movie exists and has available seats
        PreparedStatement checkMovieStmt = connection.prepareStatement("SELECT available_seats FROM movies WHERE movie_id = ?");
        checkMovieStmt.setInt(1, movieId);
        ResultSet movieResult = checkMovieStmt.executeQuery();

        if (movieResult.next()) {
            int availableSeats = movieResult.getInt("available_seats");

            if (availableSeats > 0) {
                System.out.print("Enter your name: ");
                scanner.nextLine(); // Consume the newline character
                String customerName = scanner.nextLine();

                System.out.print("Enter the number of tickets to book: ");
                int numTickets = scanner.nextInt();

                if (numTickets <= availableSeats) {
                    // Insert the booking into the 'bookings' table, letting the database generate the booking_id
                    PreparedStatement bookTicketStmt = connection.prepareStatement("INSERT INTO bookings (movie_id, customer_name, num_tickets, booking_time) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
                    bookTicketStmt.setInt(1, movieId);
                    bookTicketStmt.setString(2, customerName);
                    bookTicketStmt.setInt(3, numTickets);
                    bookTicketStmt.executeUpdate();
                    bookTicketStmt.close();

                    // Update the 'movies' table with the new available seats
                    PreparedStatement updateSeatsStmt = connection.prepareStatement("UPDATE movies SET available_seats = available_seats - ? WHERE movie_id = ?");
                    updateSeatsStmt.setInt(1, numTickets);
                    updateSeatsStmt.setInt(2, movieId);
                    updateSeatsStmt.executeUpdate();
                    updateSeatsStmt.close();

                    System.out.println("Tickets booked successfully!");
                } else {
                    System.out.println("Not enough available seats for your request.");
                }
            } else {
                System.out.println("No available seats for this movie.");
            }
        } else {
            System.out.println("Movie not found.");
        }

        checkMovieStmt.close();
    }


    private static void viewBookings(Connection connection) throws SQLException {
    	 System.out.print("Enter your name to view your bookings: ");
         Scanner scanner = new Scanner(System.in);
         String customerName = scanner.nextLine();

         PreparedStatement viewBookingsStmt = connection.prepareStatement("SELECT * FROM bookings b INNER JOIN movies m ON b.movie_id = m.movie_id WHERE b.customer_name = ?");
         viewBookingsStmt.setString(1, customerName);
         ResultSet resultSet = viewBookingsStmt.executeQuery();

         if (!resultSet.isBeforeFirst()) {
             System.out.println("No bookings found for " + customerName);
         } else {
             System.out.println("Your Bookings:");
             System.out.println("Booking ID\tMovie Name\tBooking Time\t\tNumber of Tickets");

             while (resultSet.next()) {
                 int bookingId = resultSet.getInt("booking_id");
                 String movieName = resultSet.getString("movie_name");
                 Timestamp bookingTime = resultSet.getTimestamp("booking_time");
                 int numTickets = resultSet.getInt("num_tickets");

                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String formattedTime = dateFormat.format(bookingTime);

                 System.out.println(bookingId + "\t\t" + movieName + "\t" + formattedTime + "\t" + numTickets);
             }
         }
         viewBookingsStmt.close();
     }
}


// JavaBean class to represent a Movie
class Movie {
    private int id;
    private String name;
    private Timestamp time;
    private int availableSeats;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
