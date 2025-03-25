package main;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import main.ReportGenerator;

public class Logger {
	 private static volatile Logger instance;
	    private static Object mutex = new Object();
	    private boolean printed;
	    String logstring = "";

	    /**
	     * Sets printed to false;
	     */
	    private Logger() {
	        printed = false;
	    }

	    
	    /**
	     * Constructor of the logger class. Creates a new singleton instance of logger only if there is no other active logger.
	     * @return returns the logger singleton.
	     */
	    /*
	    public static Logger getInstance() {
	        Logger result = instance;
	        if (result == null) {
	            synchronized (mutex) {
	                result = instance;
	                if (result == null)
	                    instance = result = new Logger();
	            }
	        }
	        return result;
	    }*/
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
	    
	    public void info(String message) {
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	        String a = timestamp + ": " + message + "\n";
	        System.out.println(a);
	        logstring += a;
	    }
	  
	    /**
	     * @return Returns if the logger has already printed the log. This method can only be accessed by one thread at at time.
	     */
	    public synchronized boolean print() {
	        if (!printed) {
	            printed = true;
	            return true;
	        }
	        return false;
	    }

	    /**
	     * Prints the messages stored in the log to the Log file.
	     */
	    public void printFile() {
	        try {
	            System.out.println("PRINTED");
	            PrintWriter out = new PrintWriter(new FileWriter("ASE coffeeshop/src/logFiles/LoggerFile.txt", true)); // Supplementary mode
	            out.println(logstring);
	            out.close();
				ReportGenerator.getInstance().generateReport();
				System.out.println("Report generated successfully!");
	            System.exit(0);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
