package main;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

public class Logger {
	 private static volatile Logger instance;
	    private static Object mutex = new Object();
	    private Logger() { } // Private constructor
	    /**
	     * Constructor of the logger class. Creates a new singleton instance of logger only if there is no other active logger.
	     * @return returns the logger singleton.
	     */
	    public static Logger getInstance() {
	        if (instance == null) {
	            synchronized (mutex) {
	                if (instance == null)
	                    instance = new Logger();
	            }
	        }
	        return instance;
	    }


	    /**
	     * Adds messages passed to it to a string in the logger.
	     * @param message Is the message passed to the logger.
	     */
	    private final StringBuilder logBuilder = new StringBuilder();
	    public void info(String message) {
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        String a = timestamp + ": " + message + "\n";
	        System.out.println(a);
	        logBuilder.append(a);
	    }
	  

	    /**
	     * Prints the messages stored in the log to the Log file.
	     */
	    public void printFile() {
	        try {
	            System.out.println("PRINTED");
	            PrintWriter out = new PrintWriter(new FileWriter("dataFiles/LoggerFile.txt", true)); // Supplementary mode
	            out.println(logBuilder.toString());  
	            out.close();
				ReportGenerator.getInstance().generateReport(); // Used to generate report
				info("Report generated successfully!");
				info("CoffeeShop APP is closed.");
	            System.exit(0); // exit program
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
