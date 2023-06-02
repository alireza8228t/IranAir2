import java.io.IOException;
import java.io.RandomAccessFile;

public class Flight {
    private String id;
    private String fromWhere;
    private String toWhere;
    private String day;
    private short time;

    private final byte STRING_LENGTH = 8;

    // Setting and getting the id of the specified flight
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Setting and getting the origin of the specified flight
    public String getFromWhere() {
        return fromWhere;
    }
    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    // Setting and getting the destination of the specified flight
    public String getToWhere() {
        return toWhere;
    }
    public void setToWhere(String toWhere) {
        this.toWhere = toWhere;
    }

    // Setting and getting the day of the specified flight
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }

    // Setting and getting the time of the specified flight
    public short getTime() {
        return time;
    }
    public void setTime(short time) {
        this.time = time;
    }

    public void writeFlightInFile(RandomAccessFile file) throws IOException {
        file.writeChars(this.id);
        file.writeChars(File.getInstance().fixStringLength(this.fromWhere, this.STRING_LENGTH));
        file.writeChars(File.getInstance().fixStringLength(this.toWhere, this.STRING_LENGTH));
        file.writeChars(File.getInstance().fixStringLength(this.day, this.STRING_LENGTH));
        file.writeShort(this.time);
    }

    public void readFlightFromFile(RandomAccessFile file) throws IOException {
        this.id = File.getInstance().readString(file, (byte) 4);
        this.fromWhere = File.getInstance().readString(file, this.STRING_LENGTH);
        this.toWhere = File.getInstance().readString(file, this.STRING_LENGTH);
        this.day = File.getInstance().readString(file, this.STRING_LENGTH);
        this.time = file.readShort();
    }

    public void readFlightFromFile(RandomAccessFile file, boolean readFrom, boolean readTo, boolean readDay, boolean readTime) throws IOException {
        this.id = File.getInstance().readString(file, (byte) 4);

        if (readFrom)
            this.fromWhere = File.getInstance().readString(file, this.STRING_LENGTH);
        else
            file.seek(file.getFilePointer() + (this.STRING_LENGTH * 2));

        if (readTo)
            this.toWhere = File.getInstance().readString(file, this.STRING_LENGTH);
        else
            file.seek(file.getFilePointer() + (this.STRING_LENGTH * 2));

        if (readDay)
            this.day = File.getInstance().readString(file, this.STRING_LENGTH);
        else
            file.seek(file.getFilePointer() + (this.STRING_LENGTH * 2));

        if (readTime)
            this.time = file.readShort();
        else
            file.seek(file.getFilePointer() + 2);
    }

    // it returns the string which shows the fields of the specified flight......
    public String toString() {
        return String.format("%-25s%-25s%-25s%-25s%d",
                this.id, this.fromWhere, this.toWhere, this.day, this.time);
    }
}
