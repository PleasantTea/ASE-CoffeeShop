package controller;

import model.CustomerQueue;
import model.Staff;
import view.StateDisplayGUI;

import javax.swing.*;
import java.util.ArrayList;

public class StaffController {
    private final CustomerQueue queue;
    private final StateDisplayGUI gui;
    private final ArrayList<Staff> staffList;
    private int staffCounter = 1;  // Record employee ID

    public StaffController(CustomerQueue queue, StateDisplayGUI gui) {
        this.queue = queue;
        this.gui = gui;
        this.staffList = new ArrayList<>();
    }

    // Start initial employee threads (e.g. two)
    public void startInitialStaff() {
        addStaff();
        addStaff();
    }

    public void addStaff() {
        if (staffList.size() >= 3) {
            JOptionPane.showMessageDialog(null, "There are at most three staffs!");
            return;
        }
        int nextNumber = findAvailableStaffNumber();
        
        Staff staff = new Staff(nextNumber, queue, gui);
        staffList.add(staff);
        staff.start();
        
    }

    public void removeStaff() {
        if (staffList.size() > 1) {
            Staff staff = staffList.remove(staffList.size() - 1);
            staff.finish(); // Mark employee completion of work

            gui.setStaffText(staff.getStaffNumber(), "");
        } else {
            JOptionPane.showMessageDialog(null, "Please do not remove the last staff.");
        }
    }

    public void setAllStaffSpeed(int speed) {
        for (Staff s : staffList) {
            s.setSpeed(speed);
        }
    }

    private int findAvailableStaffNumber() {
        // 1~3 Available number
        for (int i = 1; i <= 3; i++) {
            boolean taken = false;
            for (Staff s : staffList) {
                if (s.getStaffNumber() == i) {
                    taken = true;
                    break;
                }
            }
            if (!taken) return i;
        }
        return staffCounter++; // fallback
    }
}
