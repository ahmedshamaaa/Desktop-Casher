package application;

import java.time.LocalDateTime;

public class Invoice {

	private int InvoiceNumber;
    public Invoice() {
		super();
	}
	public Invoice(int invoiceNumber, LocalDateTime invoiceTime, double paidAmount, double invoiceTotal) {
		super();
		InvoiceNumber = invoiceNumber;
		InvoiceTime = invoiceTime;
		PaidAmount = paidAmount;
		InvoiceTotal = invoiceTotal;
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
	public double getPaidAmount() {
		return PaidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		PaidAmount = paidAmount;
	}
	public double getInvoiceTotal() {
		return InvoiceTotal;
	}
	public void setInvoiceTotal(double invoiceTotal) {
		InvoiceTotal = invoiceTotal;
	}
	private LocalDateTime InvoiceTime;
    private double PaidAmount;
    private double InvoiceTotal;
}
