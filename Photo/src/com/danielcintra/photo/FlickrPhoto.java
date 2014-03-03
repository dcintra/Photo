package com.danielcintra.photo;

public class FlickrPhoto {
	String id;
	  String secret;
	  String farm;
	  String server;
	  String title;
	  String owner;
	  String date;
	  String desc;
	  String imageURLSmall;
	  String imageURLMed;
	   
	//Constructors
	public FlickrPhoto() {
		// TODO Auto-generated constructor stub
	}
	     FlickrPhoto(String photo_id, String secret, String farm, String server,
	    		String title, String owner, String date, String imageURLSmall, String imageURLMed, String desc) {
	    	
	    	this.id = photo_id;
	    	this.secret = secret;
	    	this.farm = farm;
	    	this.server = server;
	    	this.title = title;
	    	this.owner = owner;
	    	this.date = date;
	    	this.imageURLSmall = imageURLSmall;
	    	this.imageURLSmall = imageURLMed;
	    	this.desc = desc;
	    }
	     
	     //Setters
	     
	     public void setId(String id) {
			this.id = id;
		}
	     
	     public void setSecret(String secret) {
			this.secret = secret;
		}
	     
	     public void setServer(String server) {
			this.server = server;
		}
	     
	     public void setFarm(String farm) {
			this.farm = farm;
		}
	     
	     public void setTitle(String title) {
			this.title = title;
		}
	     
	     public void setDesc(String desc) {
			this.desc = desc;
		}
	     
	     public void setImageURLSmall(String imageURLSmall) {
			this.imageURLSmall = imageURLSmall;
		}
	     public void setImageURLMed(String imageURLMed) {
				this.imageURLMed = imageURLMed;
			}
	     public void setOwner(String owner) {
			this.owner = owner;
		}
	     public void setDate(String date) {
			this.date = date;
		}
	     
	     //Getters
	     
	     public String getDate() {
			return date;
		}
	    public String getImageURLSmall() {
			return imageURLSmall;
		}
	    public String getImageURLMed() {
			return imageURLMed;
		}
	    public String getDesc() {
			return desc;
		}
	    public String getOwner() {
			return owner;
		}
	    public String getTitle() {
			return title;
		}
	    
	    
	    
	   

}
