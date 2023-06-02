import java.io.IOException;
import java.io.RandomAccessFile;

public class User {
    private String username;
    private String password;

    private final byte STRING_LENGTH = 50;
    private final byte FLIGHT_LENGTH = 58;
    private final short USER_LENGTH = 1360;

    /**
     *
     * @return the field username in this class
     */
    public String getUsername() {
        return this.username;
    }

    /**
     *
     * @param username To be assigned to field username in this class
     * @param withValidate If true, checks if username is at least 3 and at most 10 characters then assigns it
     *                     to field username in this class;
     *                     otherwise, throws IllegalArgumentException
     */
    public void setUsername(String username, boolean withValidate) {
        if (withValidate) {
            if(username.length() < 3 || username.length() > 10)
                throw new IllegalArgumentException
                        ("Invalid Username!\nShould be at least 3 and at most 50 characters!");
        }

        this.username = username;
    }

    /**
     *
     * @return the field password in this class
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @param password To be assigned to field password in this class
     * @param withValidate If true, checks if password is at least 8 and at most 20 characters and contains
     *                     one small, one capital, and one numerical character;
     *                     otherwise, throws IllegalArgumentException
     */
    public void setPassword(String password, boolean withValidate) {
        if (!withValidate) {
            this.password = password;
            return;
        }

        boolean capital = false;
        boolean small = false;
        boolean number = false;

        if(password.length() < 8 || password.length() > 20)
            throw new IllegalArgumentException
                    ("Invalid password!\nShould be at least 8 and at most 50 characters!\n" +
                            "Also should contain one small, one capital and one numerical character!");

        for(int i = 0; i < password.length(); i++) {
            if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                capital = true;
            else if(password.charAt(i) >= 'a' && password.charAt(i) <= 'z')
                small = true;
            else if(password.charAt(i) >= '0' && password.charAt(i) <= '9')
                number = true;

            if (capital && small && number) {
                this.password = password;
                return;
            }
        }

        throw new IllegalArgumentException
                ("Invalid password!\nShould be at least 8 and at most 50 characters!\n" +
                        "Also should contain one small, one capital and one numerical character!");
    }

    public void writeUserEndOfFile(RandomAccessFile file) throws IOException {
        file.seek(file.length());

        file.writeChars(File.getInstance().fixStringLength(this.username, this.STRING_LENGTH));
        file.writeChars(File.getInstance().fixStringLength(this.password, this.STRING_LENGTH));
        file.writeChars(" ".repeat(20 * (this.FLIGHT_LENGTH / 2)));
    }

    public void writeFlightForUserInFile(RandomAccessFile file, Flight flight) throws IOException {
        long placeOfWritingFlight;

        if (isFoundUserInFile(file, false)) {
            placeOfWritingFlight = checkWhereToWriteFlightForUser(file, flight);

            if (placeOfWritingFlight == -2)
                throw new IllegalStateException("Mentioned flight exists in flight list of the user!");
            else if (placeOfWritingFlight == -1)
                throw new IllegalStateException("No free space in the file to write more flights for this user!");
            else {
                file.seek(placeOfWritingFlight);

                flight.writeFlightInFile(file);
            }
        }
        else
            throw new IllegalStateException("User not found in the file!");
    }


    public void readFlightsOfUserFromFileAndPrint(RandomAccessFile file) throws IOException {
        if (isFoundUserInFile(file, false)) {
            Flight flight = new Flight();
            long i = file.getFilePointer() + ((this.STRING_LENGTH * 2) * 2);

            for (byte counter = 0; counter < 20; counter++, i += this.FLIGHT_LENGTH) {
                file.seek(i);

                if (file.readChar() != ' ') {
                    file.seek(file.getFilePointer() - 2);

                    if (counter == 0) {
                        System.out.print(Others.Color.BLUE);
                        System.out.printf("--------%s%s%s's Flight(s)--------%n", Others.Color.CYAN, this.username, Others.Color.BLUE);
                        System.out.print(Others.Color.RESET);

                        System.out.print(Others.Color.BLUE);
                        System.out.printf("%-25s%-25s%-25s%-25s%s%n", "flightId", "from", "to", "day", "time");
                        System.out.print(Others.Color.RESET);
                    }

                    flight.readFlightFromFile(file);

                    System.out.println(flight);
                }
                else {
                    if (counter == 0)
                        System.out.println(Others.Color.RED + "No flights in your flight list!" + Others.Color.RESET);

                    break;
                }
            }
        }
        else
            throw new IllegalStateException("User not found in the file!");
    }

    public boolean isFoundUserInFile(RandomAccessFile file, boolean withPasswordCheck) throws IOException {
        for (long i = 0; i < file.length(); i += this.USER_LENGTH) {
            file.seek(i);

            if (withPasswordCheck) {
                if (File.getInstance().readString(file, this.STRING_LENGTH).equals(this.username))
                    if (File.getInstance().readString(file, this.STRING_LENGTH).equals(this.password)) {
                        file.seek(file.getFilePointer() - ((this.STRING_LENGTH * 2) * 2));

                        return true;
                    }
            }
            else {
                if (File.getInstance().readString(file, this.STRING_LENGTH).equals(this.username)) {
                    file.seek(file.getFilePointer() - (this.STRING_LENGTH * 2));

                    return true;
                }
            }
        }

        return false;
    }

    private long checkWhereToWriteFlightForUser(RandomAccessFile file, Flight flight) throws IOException {
        long i = file.getFilePointer() + ((this.STRING_LENGTH * 2) * 2);

        for (byte counter = 0; counter < 20; counter++, i += this.FLIGHT_LENGTH) {
            file.seek(i);

            if (file.readChar() != ' ') {
                file.seek(file.getFilePointer() - 2);

                if (File.getInstance().readString(file, (byte) 4).equals(flight.getId()))
                    if (File.getInstance().readString(file, (byte) 8).equals(flight.getFromWhere()))
                        if (File.getInstance().readString(file, (byte) 8).equals(flight.getToWhere()))
                            if (File.getInstance().readString(file, (byte) 8).equals(flight.getDay()))
                                if (file.readShort() == flight.getTime())
                                    return -2;
            }
            else
                return file.getFilePointer() - 2;
        }

        return -1;
    }
}
