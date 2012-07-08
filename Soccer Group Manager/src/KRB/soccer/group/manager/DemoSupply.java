package KRB.soccer.group.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class DemoSupply {
	
	
	public GroupOfPlayers MakeDemoGroup(String name){
		String admin = "KRB";
		String location = "Gol Time!";
		
		ArrayList <String> gathering = new ArrayList<String>();
		gathering.add("Sunday");
		gathering.add("21:30");
		
		ArrayList <Player> players = new ArrayList<Player>();
		players.add(MakeDemoPlayer("demo player"));
		players.add(MakeDemoPlayer("ohana"));
		Bitmap logo = null;

		return new GroupOfPlayers(name, admin, location, gathering, logo, players );
				
	}
	
	public Player MakeDemoPlayer(String name){
		List<String> contacts = new ArrayList<String>();
		contacts.add("0544241273");
		contacts.add("avi.baruc1@gmail.com");
		
		URL facebookPage = null;
		List<URL> picList = null;
		try {
			facebookPage = new URL("http://www.facebook.com/?ref=logo");
			// TODO Auto-generated catch block
			
			picList = new ArrayList<URL>();
			URL pic = new URL("C:\\Users\\admin\\Pictures\\030520122181.jpg");
			picList.add(pic);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}

		Bitmap profilePic = CreateDemoBitmap();  
		
		Player demoPlayer = new Player(name, "Baruch", "Trader", contacts, facebookPage, profilePic, picList);	
		return demoPlayer;
	}

	Bitmap CreateDemoBitmap(){
		int[] colors = new int[64 * 50];
		for (int y = 0; y < 50; y++) {    
			for (int x = 0; x < 50; x++) {  
				int r = x * 255 / (50 - 1);   
				int g = y * 255 / (50 - 1);     
				int b = 255 - Math.min(r, g);   
				int a = Math.max(r, g);    
				colors[y * 64 + x] = (a << 24) | (r << 16) | (g << 8) | b; 
				}
			}
		return Bitmap.createBitmap(colors, 0, 64, 50, 50, Bitmap.Config.ARGB_8888);
	}
}



