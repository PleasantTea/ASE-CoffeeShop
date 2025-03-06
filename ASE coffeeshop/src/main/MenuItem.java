package main;

/**
 * The class contains details about a menu item, 
 * including its category, itemId, price, itemName, and description.
 */
public class MenuItem {
	
	private String category;
	private String itemId;
	private Float price;
	private String itemName;
	private String description;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Return a string, displaying the item name and its price,
     * will be used in the GUI section.
     * @return A formatted string containing item name and price.
     */
    public String toString() {
		return itemName + "        " + price;
	}
}
