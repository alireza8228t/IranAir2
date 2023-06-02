import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class File {
    private static final File file = new File();
    private RandomAccessFile flightsFile;
    private RandomAccessFile usersFile;

    private File() {
        try {
            this.flightsFile = new RandomAccessFile("flights.dat", "rw");

            this.usersFile = new RandomAccessFile("users.dat", "rw");
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public static File getInstance() {
        return file;
    }

    public RandomAccessFile getFlightsFile() {
        return this.flightsFile;
    }

    public RandomAccessFile getUsersFile() {
        return this.usersFile;
    }


    public String fixStringLength(String str, byte len) {
        if (str.length() >= len)
            return str.substring(0, len);
        else {
            str += " ".repeat(len - str.length());

            return str;
        }
    }

    public String readString(RandomAccessFile file, byte len) throws IOException {
        StringBuilder str = new StringBuilder();

        for (byte i = 0; i < len; i++)
            str.append(file.readChar());

        return String.valueOf(str).trim();
    }
}
