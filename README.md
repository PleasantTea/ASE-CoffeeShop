# ASE CoffeeShop ☕️

A Java-based simulation system for managing a coffee shop. The system includes customer ordering, order processing by staff, real-time queue management, and discount strategies. Built following object-oriented principles and test-driven development.

---

## 🚀 Features

- 📋 **CSV-based Order & Menu Loading**  
  Load menu and customer orders from external CSV files (`Menu.csv`, `Orders.csv`).

- 🧺 **Basket System**  
  Customers can add menu items into a basket and confirm orders.

- 💰 **Discount System**  
  Applies discounts based on rules like:
  - 10% off for total amount ≥ 100
  - ¥10 off for ordering items across 3 or more categories
  - Applies only the better of the two discounts if both are met

- 👨‍🍳 **Multi-threaded Staff Simulation**  
  Multiple staff members handle queued customer orders concurrently.

- 🧑‍💻 **Observer Pattern**  
  GUI is updated dynamically when customer queues change.

- 📊 **Final Report Generation**  
  Automatically generates a report summarizing all processed orders.

- ✅ **JUnit Testing**  
  Core modules tested using JUnit for correctness and reliability.

---

## 📁 Project Structure

```bash
ASE coffeeshop/
├── main/                  # Entry point and core logic
│   ├── Main.java          # Program entry and startup controller
│   ├── Basket.java        # Manages customer’s basket and finalizing orders
│   ├── Order.java         # Represents a single order
│   ├── MenuItem.java      # Represents a menu item
│   ├── DiscountRuler.java # Applies and calculates discounts
│   ├── Logger.java        # Logging mechanism
│   ├── ReportGenerator.java # Report generator
├── model/                 # Business logic
│   ├── CustomerQueue.java # Manages the waiting queues (priority + normal)
│   ├── Staff.java         # Worker thread that processes orders
├── controller/
│   └── StaffController.java # [Optional if still present]
├── view/                  # GUI interfaces
│   ├── StartGUI.java
│   ├── CoffeeShopGUI.java
│   └── StateDisplayGUI.java
├── observer/              # Observer pattern
│   └── QueueObserver.java # Interface for observing queue state changes
├── fileRead/              # File I/O
│   ├── MenuFileRead.java
│   ├── OrdersFileRead.java
├── exception/             # Custom exceptions
│   ├── InvalidMenuFileReadException.java
│   └── InvalidOrdersFileReadException.java
├── test/                  # JUnit tests
│   ├── TestBasket.java
│   ├── TestMenuFileRead.java
│   ├── TestOrdersFileRead.java
│   └── TestDiscountRuler.java
