package KRB.soccer.group.manager;

import java.util.List;


import android.graphics.Bitmap;

public class GroupOfPlayers {
	
	private String name;
	private String admin;
	private String location;
	private List<String> gathering;
	private Bitmap logo;
	private List<Player> players;
	
	public GroupOfPlayers(){
//		GroupOfPlayers("","","", null, null, null);
	}
	
	public GroupOfPlayers( String groupName, String groupAdmin, String groupLocation, List<String> groupGathering, Bitmap groupLogo, List<Player> groupPlayers ){
		SetGroupName(groupName);
		SetGroupAdmin(groupAdmin);
		SetGroupLocation(groupLocation);
		SetGroupGathering(groupGathering);
		SetGroupLogo(groupLogo);
		SetPlayers(groupPlayers);
		
	} 
	
	public void SetGroupName(String groupName) {
		if(groupName != null)
			name = groupName;
		else
			name = "";
	}
	
	public String GetGroupName(){
		return name;
	}
	
	

	public void SetGroupAdmin(String groupAdmin) {
		// TODO Auto-generated method stub
		if(groupAdmin != null)
			admin = groupAdmin;
		else
			admin = "";
		
	}

	public String GetGroupAdmin(){
		return admin;
	}
	
	public void SetGroupLocation(String groupLocation) {
		// TODO Auto-generated method stub
		if(groupLocation != null)
			location = groupLocation;
		else
			location = "";
		
	}

	public String GetGroupLocation(){
		return location;
	}
	
	public void SetGroupGathering(List<String> groupGathering) {
		// TODO Auto-generated method stub
//		if(groupGathering != null)
			gathering = groupGathering;
//		else
//			gathering = "";
		
	}

	public List<String> GetGroupGathering(){
		return gathering;
	}
	
	
	public void SetGroupLogo(Bitmap groupLogo) {
		
		logo = groupLogo;
		
	}
	
	public Bitmap GetGroupLogo(){
		return logo;
	}
	
	
	public void SetPlayers(List<Player> groupPlayers) {
		players = groupPlayers;
	}

	public List<Player> GetPlayers(){
		return players;
	}
	
}
