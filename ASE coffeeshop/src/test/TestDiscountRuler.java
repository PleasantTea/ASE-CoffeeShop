package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.DiscountRuler;
import main.MenuItem;

public class TestDiscountRuler {

    private DiscountRuler discountRuler = new DiscountRuler();
    

    @Test
    @DisplayName("When total amount is greater than 100, Discount 1 is applied")
    public void whenAmountIsGreaterThan100ThenApplyDiscount1() {
        // Arrange
        Float billAmount = 150.0f;
        ArrayList<MenuItem> customerOrder = new ArrayList<>();

        // Act
        Float discountedAmount = discountRuler.applyDiscounts(billAmount, customerOrder);

        // Assert
        assertEquals(135.0f, discountedAmount); // 10% discount applied
        assertEquals(15.0f, DiscountRuler.getTotalDiscountAmount());
      
    }

    
    @Test
    @DisplayName("When order contains Coffee, Drinks, and Dessert, Discount 2 is applied")
    public void whenAllCategoriesPresentThenApplyDiscount2() {
        // Arrange
        Float billAmount = 60.0f;
        ArrayList<MenuItem> customerOrder = new ArrayList<>();

        // Arrange items
        MenuItem item1 = new MenuItem();
        item1.setItemId("COFFEE001");
        item1.setItemName("Latte");
        item1.setPrice(24f);
        item1.setCategory("Coffee");
        
        MenuItem item2 = new MenuItem();
        item2.setItemId("DRINKS001");
        item2.setItemName("Cola");
        item2.setPrice(20f);
        item2.setCategory("Drinks");
        
        MenuItem item3 = new MenuItem();
        item3.setItemId("DESSERT001");
        item3.setItemName("Cake");
        item3.setPrice(16f);
        item3.setCategory("Dessert");

        // Add to order
        customerOrder.add(item1);
        customerOrder.add(item2);
        customerOrder.add(item3);

        // Act
        Float discountedAmount = discountRuler.applyDiscounts(billAmount, customerOrder);

        // Assert
        assertEquals(50.0f, discountedAmount); // $10 discount applied
        assertEquals(10.0f, DiscountRuler.getTotalDiscountAmount());
   
    }

    @Test
    @DisplayName("When order does not contain all categories and not over 100, no discount is not applied")
    public void whenNotAllCategoriesPresentThenDoNotApplyDiscount2() {
        // Arrange
        Float billAmount = 50.0f;
        ArrayList<MenuItem> customerOrder = new ArrayList<>();
        
        // Arrange items
        MenuItem item1 = new MenuItem();
        item1.setItemId("COFFEE001");
        item1.setItemName("Latte");
        item1.setPrice(24f);
        item1.setCategory("Coffee");
        
        MenuItem item2 = new MenuItem();
        item2.setItemId("DRINKS001");
        item2.setItemName("Cola");
        item2.setPrice(20f);
        item2.setCategory("Drinks");

        // Add to order
        customerOrder.add(item1);
        customerOrder.add(item2);

        // Act
        Float discountedAmount = discountRuler.applyDiscounts(billAmount, customerOrder);

        // Assert
        assertEquals(50.0f, discountedAmount); // No Discount applied 
       
    }

    @Test
    @DisplayName("When Discount 1 and Discount 2 can both be applied, apply Discount 1")
    public void whenBothDiscountsCanBeAppliedThenPreferDiscount1() {
        // Arrange
        Float billAmount = 200.0f;
        ArrayList<MenuItem> customerOrder = new ArrayList<>();
        
        // Arrange items
        MenuItem item1 = new MenuItem();
        item1.setItemId("COFFEE001");
        item1.setItemName("Latte");
        item1.setPrice(24f);
        item1.setCategory("Coffee");
        
        MenuItem item2 = new MenuItem();
        item2.setItemId("DRINKS001");
        item2.setItemName("Cola");
        item2.setPrice(20f);
        item2.setCategory("Drinks");
        
        MenuItem item3 = new MenuItem();
        item3.setItemId("DESSERT001");
        item3.setItemName("Cake");
        item3.setPrice(16f);
        item3.setCategory("Dessert");

        // Add to order
        customerOrder.add(item1);
        customerOrder.add(item2);
        customerOrder.add(item3);

        // Act
        Float discountedAmount = discountRuler.applyDiscounts(billAmount, customerOrder);

        // Assert
        assertEquals(180.0f, discountedAmount); // Discount 1 applied (10% off)
        assertNotEquals(190.0f, discountedAmount); // Discount 2 ($10) not applied
       
    }
}
