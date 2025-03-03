package main;

import java.util.ArrayList;
import java.util.HashSet;


public class DiscountRuler {
	private static Float totalDiscount = (float)0;
	  /**
	   * Common method to apply Discount 1 or 2
	   * Discount 1 is preferred over Discount 2
	   * Apply only one Discount
	   * returns billAmount if no discount is applied, else returns discounted amount
	   * 
	   */
	  public Float applyDiscounts(Float billAmount, ArrayList<MenuItem> customerOrder) {
		  	
		  	Float amountAfterDiscount1 = (float) 0;
		  	
		  	//Check if discount 1 can be applied
		  	amountAfterDiscount1 = calculateDiscount1(billAmount);
		  			  	
			//discount 1 is not applied
			if(billAmount!= null && (billAmount.equals(amountAfterDiscount1))){ 												
				
				//check for discount 2
				Float amountAfterDiscount2 = calculateDiscount2(billAmount, customerOrder);
				if(	billAmount!= null && !(billAmount.equals(amountAfterDiscount2))  ){					
					return amountAfterDiscount2;
				}
			}
			//discount 1 is applied
			else{ 				
				return amountAfterDiscount1;
			}

			return billAmount;
		  //
	  }
	  
	  public static Float getTotalDiscountAmount() {
		  return totalDiscount;
	  }
	  
	  public static void setTotalDiscountAmount(Float value) {
		  totalDiscount = value;
	  }
	 
	  
	  
	  public Float calculateDiscount1(Float amount) {
		  
		  if(isDiscount1Applicable(amount)) {
			  totalDiscount = totalDiscount +  (amount * 0.1f);
			  return amount * 0.9f;
		  }
		  return amount;
	  }
	  
	  public Float calculateDiscount2 (Float billAmount, ArrayList<MenuItem> customerOrder) {
		  if(isDiscount2Applicable(customerOrder)) {
			  totalDiscount = totalDiscount + (float)10;
			  return billAmount - 10;
		  }
		  return billAmount;
	  } 
	  
	  // *********** Private Methods *********************//
	
	  /**
	   * This method checks if Discount 1 can be applied.
	   * Condition: Order Amount is > 100
	   * else there is no discount so return the same amount 
	   * @return: bill Amount if no discount, BillAmount - 10 if otherwise
	   */
	 
	  private Boolean isDiscount1Applicable(Float amount) {
			//if number of order is not equal to 3 then don't apply discount
			if(amount>=100) {
				return true;
			}
			
			else{							
				return false;				
				
			}			
		}
	  
	  
	  /**
	   * This method checks if Discount 2 can be applied...It checks below conditions
	   * 1) Size of order is 3 or more ...so that all categories applies
	   * 2) All three categories are present in the order
	   * @return : true if discount 2 is applied , false otherwise
	   */
	  
	  private Boolean isDiscount2Applicable(ArrayList<MenuItem> customerOrder) {
			//if number of order is not equal to 3 then don't apply discount
			if((customerOrder.size() < 3)) {
				return false;
			}
			else
			{
				HashSet<String> uniqueCategory = new HashSet<String>();
				for(MenuItem custOrder: customerOrder) {
					
					String category = custOrder.getCategory();
					
					System.out.println(category);
					uniqueCategory.add(category);
					
				}
				if(uniqueCategory.contains("Coffee")  && uniqueCategory.contains("Drinks") && uniqueCategory.contains("Dessert")) {					
					return true;
				}
				else {					
					return false;
				}
				
			}
			
		}

}
