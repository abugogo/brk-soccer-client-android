package KRB.soccer.group.manager;

import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;

public class Player {
	
	private String firstName;
	private String LastName;
	private String Occupation;
	private List<String> contacts;
	private URL facebookPage;
//	private Bitmap profilePic;
	private List<URL> pictures;
	
	public Player(){
//		Player("","","",null,null,null,null);
	}
	

	public Player( String firstName, String lastName, String occupation, List<String> contacts, URL facebookPage, Bitmap profilePic, List<URL> pictures){
		SetFirstName(firstName);
		SetLastName(lastName);
		SetOccupation(occupation);
		SetContacts(contacts);
		SetFacebookPage(facebookPage);
//		SetProfilePic(profilePic);
		SetPictures(pictures);
	}
	


	public void SetFirstName(String newName) {
		// TODO Auto-generated method stub
		firstName = newName;
	}
	
	public String getFirstName(){
		return firstName;
	}
	


	public void SetLastName(String newName) {
		// TODO Auto-generated method stub
		LastName = newName;
	}

	public String GetLastName(){
		return LastName;
	}
	
	
	public void SetOccupation(String newOccupation) {
		// TODO Auto-generated method stub
		Occupation = newOccupation;
	}

	public String GetOccupation(){
		return Occupation;
	}
	
	
	public void SetContacts(List<String> newContacts) {
		// TODO Auto-generated method stub
		contacts = newContacts;
	}

	public List<String> GetContacts(){
		return contacts;
	}
	
	
	public void SetFacebookPage(URL newFacebookPage) {
		// TODO Auto-generated method stub
		facebookPage = newFacebookPage;
	}
	
	public URL GetFacebookPage(){
		return facebookPage;
	}
	
/*
	public void SetProfilePic(Bitmap newProfilePic) {
		// TODO Auto-generated method stub
		profilePic = newProfilePic;
	}
	
	public Bitmap GetProfilePic(){
		return profilePic;
	}
*/	
	
	public void SetPictures(List<URL> newPictures) {
		// TODO Auto-generated method stub
		pictures = newPictures;
	}
	
	public List<URL> GetPictures(){
		return pictures;
	}

	public void AddPicture(URL newPpicture) {
		// TODO Auto-generated method stub
		pictures.add(newPpicture);
	}
	
//	public URL RemovePicture( URL picture ){
//	}

	

}
