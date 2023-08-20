package application;

import java.time.LocalDateTime;

public class InvoiceDetails {
    public InvoiceDetails() {
		super();
	}
	public InvoiceDetails(int invoiceNumber, LocalDateTime invoiceTime, int itemIndex, String itemId,
			double itemPrice) {
		super();
		InvoiceNumber = invoiceNumber;
		InvoiceTime = invoiceTime;
		ItemIndex = itemIndex;
		ItemId = itemId;
		ItemPrice = itemPrice;
	}
	public int getInvoiceNumber() {
		return InvoiceNumber;
	}
	public void setInvoiceNumber(int invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}
	public LocalDateTime getInvoiceTime() {
		return InvoiceTime;
	}
	public void setInvoiceTime(LocalDateTime invoiceTime) {
		InvoiceTime = invoiceTime;
	}
	public int getItemIndex() {
		return ItemIndex;
	}
	public void setItemIndex(int itemIndex) {
		ItemIndex = itemIndex;
	}
	public String getItemId() {
		return ItemId;
	}
	public void setItemId(String itemId) {
		ItemId = itemId;
	}
	public double getItemPrice() {
		return ItemPrice;
	}
	public void setItemPrice(double itemPrice) {
		ItemPrice = itemPrice;
	}
	private int InvoiceNumber;
    private LocalDateTime InvoiceTime;
    private int ItemIndex;
    private String ItemId;
    private double ItemPrice;

}
