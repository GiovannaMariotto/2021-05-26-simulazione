package it.polito.tdp.yelp.model;

public class Arco {

	private Business b1;
	private Business b2;
	private Double peso;
	
	public Business getB1() {
		return b1;
	}
	public void setB1(Business b1) {
		this.b1 = b1;
	}
	public Business getB2() {
		return b2;
	}
	public void setB2(Business b2) {
		this.b2 = b2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	
	public Arco(String id1, String id2, double peso) {
		this.b1= new Business(id1);
		this.b2= new Business(id2);
		this.peso = peso;
	}
	
	
	
	
	
}
