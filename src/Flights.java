import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Flights {
    private final byte FLIGHT_LENGTH = 58;
    private final byte PLACE_OF_FROM = 8;

    public void fillFlightsFile() throws IOException {
        File.getInstance().getFlightsFile().seek(0);

        String[] cityList = {"Tehran", "Esfahan", "Yazd", "Mashhad", "Shiraz", "Kerman"};
        String[] dayList = {"Saturday", "Sunday", "Monday", "Tuesday"};
        short[] timeList = {8, 10, 18, 20};

        Random rand = new Random();
        Flight flight;
        int randomNum;
        boolean idEquality;

        for(int i = 0; i < 20; i++) {
            flight = new Flight();

            idEquality = true;
            do {
                randomNum = rand.nextInt(999) + 1;
                flight.setId(String.format("%04d", randomNum));
                if (checkFlightNotExistingInFile(File.getInstance().getFlightsFile(), File.getInstance().getFlightsFile().getFilePointer(), flight, true))
                    idEquality = false;
            } while (idEquality);

            randomNum = rand.nextInt(cityList.length);
            flight.setFromWhere(cityList[randomNum]);

            do {
                randomNum = rand.nextInt(cityList.length);
            } while(cityList[randomNum].equals(flight.getFromWhere()));
            flight.setToWhere(cityList[randomNum]);

            randomNum = rand.nextInt(dayList.length);
            flight.setDay(dayList[randomNum]);

            randomNum = rand.nextInt(timeList.length);
            flight.setTime(timeList[randomNum]);

            if(checkFlightNotExistingInFile(File.getInstance().getFlightsFile(), File.getInstance().getFlightsFile().getFilePointer(), flight, false))
                flight.writeFlightInFile(File.getInstance().getFlightsFile());
            else
                i--;
        }
    }

    public void readAllFlightsFromFileAndPrint(RandomAccessFile file) throws IOException {
        if (file.length() < this.FLIGHT_LENGTH) {
            System.out.println("No flights in the file!");
            return;
        }

        Flight flight = new Flight();

        System.out.print(Others.Color.BLUE);
        System.out.printf("%-25s%-25s%-25s%-25s%s%n", "flightId", "from", "to", "day", "time");
        System.out.print(Others.Color.RESET);

        for (long i = 0; i < file.length(); i += this.FLIGHT_LENGTH) {
            file.seek(i);

            flight.readFlightFromFile(file);

            System.out.println(flight);
        }
    }

    public byte searchFromFlightsFromFileAndPrint(RandomAccessFile file, Flight flightForFrom) throws IOException {
        byte countOfFlightsFound = 0;

        for (long i = this.PLACE_OF_FROM; i < file.length(); i += this.FLIGHT_LENGTH) {
            file.seek(i);

            if (File.getInstance().readString(file, (byte) 8).equals(flightForFrom.getFromWhere())) {
                file.seek(file.getFilePointer() - 24);

                flightForFrom.readFlightFromFile(file, false, true, true, true);

                if (countOfFlightsFound == 0) {
                    System.out.println();
                    System.out.print(Others.Color.BLUE);
                    System.out.printf("%-25s%-25s%-25s%-25s%s%n", "flightId", "from", "to", "day", "time");
                    System.out.print(Others.Color.RESET);
                }

                System.out.println(flightForFrom);

                countOfFlightsFound++;
            }
        }

        System.out.println();

        if (countOfFlightsFound == 0)
            throw new IllegalStateException("No flights found!");
        else
            return countOfFlightsFound;
    }

    public byte searchFromToFlightsFromFileAndPrint(RandomAccessFile file, Flight flightForFromTo) throws IOException {
        byte countOfFlightsFound = 0;

        for (long i = this.PLACE_OF_FROM; i < file.length(); i += this.FLIGHT_LENGTH) {
            file.seek(i);

            if (File.getInstance().readString(file, (byte) 8).equals(flightForFromTo.getFromWhere()))
                if (File.getInstance().readString(file, (byte) 8).equals(flightForFromTo.getToWhere())) {
                    file.seek(file.getFilePointer() - 40);

                    flightForFromTo.readFlightFromFile(file, false, false, true, true);

                    if (countOfFlightsFound == 0) {
                        System.out.println();
                        System.out.print(Others.Color.BLUE);
                        System.out.printf("%-25s%-25s%-25s%-25s%s%n", "flightId", "from", "to", "day", "time");
                        System.out.print(Others.Color.RESET);
                    }

                    System.out.println(flightForFromTo);

                    countOfFlightsFound++;
                }
        }

        System.out.println();

        if (countOfFlightsFound == 0)
            throw new IllegalStateException("No flights found!");
        else
            return countOfFlightsFound;
    }

    public byte searchFromToDayFlightsFromFileAndPrint(RandomAccessFile file, Flight flightForFromToDay) throws IOException {
        byte countOfFlightsFound = 0;

        for (long i = this.PLACE_OF_FROM; i < file.length(); i += this.FLIGHT_LENGTH) {
            file.seek(i);

            if (File.getInstance().readString(file, (byte) 8).equals(flightForFromToDay.getFromWhere()))
                if (File.getInstance().readString(file, (byte) 8).equals(flightForFromToDay.getToWhere()))
                    if (File.getInstance().readString(file, (byte) 8).equals(flightForFromToDay.getDay())) {
                        file.seek(file.getFilePointer() - 56);

                        flightForFromToDay.readFlightFromFile(file, false, false, false, true);

                        if (countOfFlightsFound == 0) {
                            System.out.println();
                            System.out.print(Others.Color.BLUE);
                            System.out.printf("%-25s%-25s%-25s%-25s%s%n", "flightId", "from", "to", "day", "time");
                            System.out.print(Others.Color.RESET);
                        }

                        System.out.println(flightForFromToDay);

                        countOfFlightsFound++;
                    }
        }

        System.out.println();

        if (countOfFlightsFound == 0)
            throw new IllegalStateException("No flights found!");
        else
            return countOfFlightsFound;
    }

    public byte searchFromToDayTimeFlightsFromFileAndPrint(RandomAccessFile file, Flight flightForFromToDayTime) throws IOException {
        byte countOfFlightsFound = 0;

        for (long i = this.PLACE_OF_FROM; i < file.length(); i += this.FLIGHT_LENGTH) {
            file.seek(i);

            if (File.getInstance().readString(file, (byte) 8).equals(flightForFromToDayTime.getFromWhere()))
                if (File.getInstance().readString(file, (byte) 8).equals(flightForFromToDayTime.getToWhere()))
                    if (File.getInstance().readString(file, (byte) 8).equals(flightForFromToDayTime.getDay()))
                        if (file.readShort() == flightForFromToDayTime.getTime()) {
                            file.seek(file.getFilePointer() - this.FLIGHT_LENGTH);

                            flightForFromToDayTime.readFlightFromFile(file, false, false, false, false);

                            if (countOfFlightsFound == 0) {
                                System.out.println();
                                System.out.print(Others.Color.BLUE);
                                System.out.printf("%-25s%-25s%-25s%-25s%s%n", "flightId", "from", "to", "day", "time");
                                System.out.print(Others.Color.RESET);
                            }

                            System.out.println(flightForFromToDayTime);

                            countOfFlightsFound++;
                        }
        }

        if (countOfFlightsFound == 0)
            throw new IllegalStateException("No flights found!");
        else
            return countOfFlightsFound;
    }

    private boolean checkFlightNotExistingInFile(RandomAccessFile file, long lastByte, Flight flight, boolean idChecking) throws IOException {
        for (long i = 0; i < lastByte; i += this.FLIGHT_LENGTH) {
            file.seek(i);

            if (idChecking)
                if (File.getInstance().readString(file, (byte) 4).equals(flight.getId()))
                    return false;
            else
                if (File.getInstance().readString(file, (byte) 8).equals(flight.getFromWhere()))
                    if (File.getInstance().readString(file, (byte) 8).equals(flight.getToWhere()))
                        if (File.getInstance().readString(file, (byte) 8).equals(flight.getDay()))
                            if (file.readShort() == flight.getTime())
                                return false;
        }

        file.seek(lastByte);
        return true;
    }
}
