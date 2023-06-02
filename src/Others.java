import java.io.IOException;
import java.util.Scanner;

public class Others {
    private final short USER_LENGTH = 1360;


     // Shows the user panel in the output and gets an integer as an option to register new user or login
     // an existing user

    public void userPanel() {
        Scanner input = new Scanner(System.in);

        System.out.print(Color.PURPLE);
        System.out.println("----------------Welcome To Iran Air Site----------------\n");
        System.out.println("-User Panel:\n");
        System.out.print(Color.RESET);

        System.out.println("-Enter #1 to register");
        System.out.println("-Enter #2 to login");

        try {
            switch (input.nextLine()) {
                case "1":
                    guidancePanel(register());
                    break;
                case "2":
                    guidancePanel(login());
                    break;
                default:
                    System.out.println(Color.RED + "Invalid Input!" + Color.RESET);
                    System.out.println("Back to user panel");

                    userPanel();
            }
        }
        catch (Exception e) {
            System.out.println(Color.RED + e.getMessage() + Color.RESET);
            System.out.println("Back to user panel");

            userPanel();
        }
    }

    /**
     *
     * @param user Shows the guidance panel for him/her
     *             and gets an integer as an option to show list of all flights, search flights,
     *             show user's flights, or go back to user panel
     */
    private void guidancePanel(User user) {
        Scanner input = new Scanner(System.in);
        Flights flights = new Flights();
        Flight flight = new Flight();

        System.out.print(Color.PURPLE);
        System.out.printf("%n%n-----Hi %s%s%s, Welcome To Iran Air Site-----%n%n", Color.CYAN, user.getUsername(), Color.PURPLE);
        System.out.println("-Guidance Panel:\n");
        System.out.print(Color.RESET);

        System.out.println("-Enter #1 to show list of all flights");
        System.out.println("-Enter #2 to search flights");
        System.out.println("-Enter #3 to show your flight(s)");
        System.out.println("-Enter #4 to logout");

        try {
            switch (input.nextLine()) {
                case "1":
                    System.out.print(Color.GREEN);
                    System.out.println("-".repeat(43) + "List Of All Flights" + "-".repeat(42));
                    System.out.print(Color.RESET);

                    flights.readAllFlightsFromFileAndPrint(File.getInstance().getFlightsFile());

                    System.out.print(Color.GREEN);
                    System.out.println("-".repeat(104));
                    System.out.print(Color.RESET);

                    System.out.print(Color.YELLOW + "\n-Press 0 to back to guidance panel  " + Color.RESET);
                    input.nextLine();

                    guidancePanel(user);
                    break;
                case "2":
                    System.out.print(Color.BLUE);
                    System.out.println("-".repeat(23) + "Search Flights" + "-".repeat(23));
                    System.out.print(Color.RESET);

                    System.out.println("Please enter your flight info to check if any flight exists:\n");

                    subSearchFlights(user, flight, SearchField.FROM);
                    subSearchFlights(user, flight, SearchField.TO);
                    subSearchFlights(user, flight, SearchField.DAY);
                    subSearchFlights(user, flight, SearchField.TIME);
                    break;
                case "3":
                    try {
                        user.readFlightsOfUserFromFileAndPrint(File.getInstance().getUsersFile());
                    }
                    catch (Exception e) {
                        System.out.println(Color.RED + e.getMessage() + Color.RESET);
                    }

                    System.out.print(Color.YELLOW + "\n-Press 0 to back to guidance panel  " + Color.RESET);
                    input.nextLine();

                    guidancePanel(user);
                    break;
                case "4":
                    userPanel();
                    break;
                default:
                    System.out.println(Color.RED + "Invalid Input!" + Color.RESET);
                    System.out.println("Back to guidance panel");

                    guidancePanel(user);
            }
        }
        catch (Exception e) {
            System.out.println(Color.RED + e.getMessage() + Color.RESET);
            System.out.println("Back to guidance panel");

            guidancePanel(user);
        }
    }

    /**
     *
     * @return The user registered (his/her username and password are gotten in input)
     */
    private User register() throws IOException {
        Scanner input = new Scanner(System.in);
        User user = new User();

        System.out.println(Color.BLUE + "----------Register New User----------" + Color.RESET);

        while (true) {
            try {
                System.out.print("Enter your username: ");
                user.setUsername(input.nextLine(), true);

                if(user.isFoundUserInFile(File.getInstance().getUsersFile(), false))
                    throw new IllegalStateException("This Username Exists!");

                break;
            }
            catch (Exception e) {
                System.out.println(Color.RED + e.getMessage() + Color.RESET);

                while (true) {
                    System.out.print(Color.YELLOW + "\n-Press 9 to try again or 0 to back to user panel  " + Color.RESET);

                    switch (input.nextLine()) {
                        case "9":
                            break;
                        case "0":
                            userPanel();
                            return null;
                        default:
                            System.out.println(Color.RED + "Invalid Input!" + Color.RESET);
                            continue;
                    }
                    break;
                }
            }
        }

        while (true) {
            try {
                System.out.print("Enter your password: ");
                user.setPassword(input.nextLine(), true);

                break;
            }
            catch (Exception e) {
                System.out.println(Color.RED + e.getMessage() + Color.RESET);

                while (true) {
                    System.out.print(Color.YELLOW + "\n-Press 9 to try again or 0 to back to user panel  " + Color.RESET);

                    switch (input.nextLine()) {
                        case "9":
                            break;
                        case "0":
                            userPanel();
                            return null;
                        default:
                            System.out.println(Color.RED + "Invalid Input!" + Color.RESET);
                            continue;
                    }
                    break;
                }
            }
        }

        user.writeUserEndOfFile(File.getInstance().getUsersFile());
        System.out.println(Color.GREEN + "\nSuccessful Registration!" + Color.RESET);

        return user;
    }

    /**
     *
     * @return The user logged in successfully (username and password are gotten in input and will be checked if
     * there is a user with such information; otherwise, throws exception or goes back to user panel)
     */
    private User login() throws IOException {
        if(File.getInstance().getUsersFile().length() < this.USER_LENGTH)
            throw new IllegalStateException("No User Registered!");

        Scanner input = new Scanner(System.in);
        User user = new User();

        while (true) {
            System.out.println(Color.BLUE + "----------Login User----------" + Color.RESET);

            System.out.print("Enter your username: ");
            user.setUsername(input.nextLine(), false);

            System.out.print("Enter your password: ");
            user.setPassword(input.nextLine(), false);

            if (user.isFoundUserInFile(File.getInstance().getUsersFile(), true)) {
                System.out.println(Color.GREEN + "\nSuccessful Login!" + Color.RESET);

                return user;
            }

            System.out.println(Color.RED + "User Not Found!" + Color.RESET);

            while (true) {
                System.out.print(Color.YELLOW + "\n-Press 9 to try again or 0 to back to user panel  " + Color.RESET);

                switch (input.nextLine()) {
                    case "9":
                        break;
                    case "0":
                        userPanel();
                        return null;
                    default:
                        System.out.println(Color.RED + "Invalid Input!" + Color.RESET);
                        continue;
                }
                break;
            }
        }
    }

    private enum SearchField {FROM, TO, DAY, TIME}

    /**
     *
     * @param user Search flights for this user (maybe he/she wants to add a flight in users file)
     * @param flight Use its fields depending on searchField
     * @param searchField Search flights for fields origin, destination, day, or time
     * @return Flight which is passed through this method and filled one field of it
     */
    private Flight subSearchFlights(User user, Flight flight, SearchField searchField) {
        Scanner input = new Scanner(System.in);
        Flights flights = new Flights();

        while (true) {
            try {
                switch (searchField) {
                    case FROM:
                        System.out.print("From: ");
                        flight.setFromWhere(input.nextLine());

                        if (flight.getFromWhere().equals("0")) {
                            guidancePanel(user);
                            return null;
                        }

                        if (flights.searchFromFlightsFromFileAndPrint(File.getInstance().getFlightsFile(), flight) == 1)
                            addFlight(user, flight);
                        break;
                    case TO:
                        System.out.print("To: ");
                        flight.setToWhere(input.nextLine());

                        if (flight.getToWhere().equals("0")) {
                            guidancePanel(user);
                            return null;
                        }

                        if (flights.searchFromToFlightsFromFileAndPrint(File.getInstance().getFlightsFile(), flight) == 1)
                            addFlight(user, flight);
                        break;
                    case DAY:
                        System.out.print("Day of departure: ");
                        flight.setDay(input.nextLine());

                        if (flight.getDay().equals("0")) {
                            guidancePanel(user);
                            return null;
                        }

                        if (flights.searchFromToDayFlightsFromFileAndPrint(File.getInstance().getFlightsFile(), flight) == 1)
                            addFlight(user, flight);
                        break;
                    case TIME:
                        System.out.print("Time of departure: ");
                        flight.setTime(input.nextShort());
                        input.nextLine();

                        if (flight.getTime() == 0) {
                            guidancePanel(user);
                            return null;
                        }

                        flights.searchFromToDayTimeFlightsFromFileAndPrint(File.getInstance().getFlightsFile(), flight);

                        addFlight(user, flight);
                        break;
                }

                return flight;
            }
            catch (Exception e) {
                System.out.print(Color.RED);
                if (e.getMessage() == null) {
                    input.nextLine();

                    System.out.println("Time of departure should be a nonnegative number between 1 and 24!");
                }
                else
                    System.out.println(e.getMessage());
                System.out.print(Color.RESET);

                while (true) {
                    System.out.print(Color.YELLOW + "\n-Press 9 to try again or 0 to back to guidance panel  " + Color.RESET);

                    switch (input.nextLine()) {
                        case "9":
                            break;
                        case "0":
                            guidancePanel(user);
                            return null;
                        default:
                            System.out.println(Color.RED + "Invalid Input!" + Color.RESET);
                            continue;
                    }
                    break;
                }
            }
        }
    }

    /**
     *
     * @param user To add flight for, in users file
     * @param flight Which should be added for specified user.
     *               This method should be used in work with menus
     */
    private void addFlight(User user, Flight flight) {
        Scanner input = new Scanner(System.in);

        System.out.print(Color.YELLOW + "\n-Press 9 to add flight ticket or 0 to back to guidance panel  " + Color.RESET);

        switch (input.nextLine()) {
            case "9":
                try {
                    user.writeFlightForUserInFile(File.getInstance().getUsersFile(), flight);

                    System.out.println(Color.GREEN + "\nSuccessfully added flight!" + Color.RESET);

                    guidancePanel(user);
                }
                catch (Exception e) {
                    System.out.println(Color.RED + e.getMessage() + Color.RESET);
                    System.out.println("Back to guidance panel");

                    guidancePanel(user);
                }
                break;
            case "0":
                guidancePanel(user);
                break;
            default:
                System.out.println(Color.RED + "Invalid Input!" + Color.RESET);

                addFlight(user, flight);
        }
    }

    /**
     * For having outputs in different colors
     */
    enum Color {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m");

        private final String code;

        Color(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return this.code;
        }
    }
}
