package org.tutorials.kafka.OrderProducer.customSerializer;

public class Order {
	
	private String customerName;
	private String product;
	private int quantity;
	
	
	public Order(String customerName, String product, int quantity) {
		super();
		this.customerName = customerName;
		this.product = product;
		this.quantity = quantity;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	

}
