package application;

public class Item {

		public Item() {
		super();
	}
		public Item(String itemID, String itemName, String itemBarcode, double itemPrice) {
		super();
		ItemID = itemID;
		ItemName = itemName;
		ItemBarcode = itemBarcode;
		ItemPrice = itemPrice;
	}
		public String getItemID() {
		return ItemID;
	}
	public void setItemID(String itemID) {
		ItemID = itemID;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getItemBarcode() {
		return ItemBarcode;
	}
	public void setItemBarcode(String itemBarcode) {
		ItemBarcode = itemBarcode;
	}
	public double getItemPrice() {
		return ItemPrice;
	}
	public void setItemPrice(double itemPrice) {
		ItemPrice = itemPrice;
	}
		public String ItemID;
		public String ItemName ;
		public String ItemBarcode; 
		public double ItemPrice ;
}
