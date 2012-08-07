package com.soccer.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.soccer.entities.impl.DAOPlayer;
import com.soccer.lib.SoccerException;

public class EntityManager {
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static IDAOPlayer readPlayer(InputStream is) throws SoccerException {
		try {
			return mapper.readValue(is, DAOPlayer.class);
		} catch (IOException e) {
			throw new SoccerException("Could not create Player from Input Stream", e);
		}
	}
	
	public static IDAOPlayer readPlayer(String str) throws SoccerException {
		try {
			return mapper.readValue(str.getBytes(), DAOPlayer.class);
		} catch (IOException e) {
			throw new SoccerException("Could not create Player from Input Stream", e);
		}
	}
	
	public static ArrayList<IDAOPlayer> readPlayers(String str) throws SoccerException {
		try {
			return mapper.readValue(str.getBytes(), new TypeReference<ArrayList<DAOPlayer>>(){});
		} catch (IOException e) {
			throw new SoccerException("Could not create Player from Input Stream", e);
		}
	}

	public static String writePlayer(IDAOPlayer player) throws SoccerException {
		try {
			return mapper.writeValueAsString(player);
		} catch (IOException e) {
			throw new SoccerException("Could not write Player to a JSON string", e);
		}
	}

	
	public static void writePlayerToStream(IDAOPlayer player, OutputStream out) throws SoccerException {
		try {
			mapper.writeValue(out, player);
		} catch (IOException e) {
			throw new SoccerException("Could not write Player to a JSON string", e);
		}
	}
	
	public static String writePlayers(List<IDAOPlayer> players) throws SoccerException {
		try {
			return mapper.writeValueAsString(players);
		} catch (IOException e) {
			throw new SoccerException("Could not write List of Players as JSON string", e);
		}
	}

	public static void writePlayersToStream(List<IDAOPlayer> players, OutputStream out) throws SoccerException {
		try {
			mapper.writeValue(out, players);
		} catch (IOException e) {
			throw new SoccerException("Could not write List of Players as JSON string", e);
		}
	}
}
