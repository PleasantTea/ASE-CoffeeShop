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
    private int staffCounter = 1;  // 记录员工编号

    public StaffController(CustomerQueue queue, StateDisplayGUI gui) {
        this.queue = queue;
        this.gui = gui;
        this.staffList = new ArrayList<>();
    }

    // 启动初始员工线程（比如两个）
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
        if (!staffList.isEmpty()) {
            Staff staff = staffList.remove(staffList.size() - 1);
            staff.finish(); // 标记员工完成工作

            //gui.setStaffText(staff.getStaffNumber(), "");
        } else {
            JOptionPane.showMessageDialog(null, "No more employees to remove!");
        }
    }

    public void setAllStaffSpeed(int speed) {
        for (Staff s : staffList) {
            s.setSpeed(speed);
        }
    }

    private int findAvailableStaffNumber() {
        // 1~3 可用编号
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
        return staffCounter++; // fallback（理论上不会执行到）
    }

    
}
