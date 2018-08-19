package com.hans.fm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	private String email;
	
	@Column(name="friends")
	@ElementCollection
	private List<String> friends;
	
	@Column(name="blocked")
	@ElementCollection
	private List<String> blocked;
	
	@Column(name="subscriptions")
	@ElementCollection
	private List<String> subscriptions;
	
	public User() {
		this.friends = new ArrayList<>();
		this.blocked = new ArrayList<>();
		this.subscriptions = new ArrayList<>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public List<String> getBlocked() {
		return blocked;
	}

	public void setBlocked(List<String> blocked) {
		this.blocked = blocked;
	}

	public List<String> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<String> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	public void addSubscription(String email) {
		Boolean exist = false;
		for(String em : this.subscriptions) {
			if(em.equalsIgnoreCase(email))
			{
				exist = true;
				break;
			}
		}
		
		if(!exist)
			this.subscriptions.add(email);
	}
	
	public void removeSubscription(String email) {
		ListIterator<String> iter = this.subscriptions.listIterator();
		while(iter.hasNext()){
		    if(iter.next().equals(email)){
		        iter.remove();
		    }
		}
	}
	
	public void block(String email) {
		Boolean exist = false;
		for(String em : this.blocked) {
			if(em.equalsIgnoreCase(email))
			{
				exist = true;
				break;
			}
		}
		
		if(!exist)
			this.blocked.add(email);
	}
	
	public void unblock(String email) {
		ListIterator<String> iter = this.blocked.listIterator();
		while(iter.hasNext()){
		    if(iter.next().equals(email)){
		        iter.remove();
		    }
		}
	}
	
	public void addFriend(String email) {
		Boolean exist = false;
		for(String em : this.friends) {
			if(em.equalsIgnoreCase(email))
			{
				exist = true;
				break;
			}
		}
		
		if(!exist)
			this.friends.add(email);
	}
	
	public void removeFriend(String email) {
		ListIterator<String> iter = this.friends.listIterator();
		while(iter.hasNext()){
		    if(iter.next().equals(email)){
		        iter.remove();
		    }
		}
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", friends=" + friends + ", blocked=" + blocked + ", subscriptions="
				+ subscriptions + "]";
	}

}
