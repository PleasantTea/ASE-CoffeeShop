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
        Staff staff = new Staff(staffCounter++, queue, gui);
        staffList.add(staff);
        staff.start();
    }

    public void removeStaff() {
        if (!staffList.isEmpty()) {
            Staff staff = staffList.remove(staffList.size() - 1);
            staff.finish(); // 标记员工完成工作
        } else {
            JOptionPane.showMessageDialog(null, "没有更多员工可移除！");
        }
    }

    public void setAllStaffSpeed(int speed) {
        for (Staff s : staffList) {
            s.setSpeed(speed);
        }
    }
}
