package org.dosimonline.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.ArrayList;
import org.dosimonline.models.Notification;
import org.dosimonline.models.NotificationManager;
import org.dosimonline.models.connection.ChatMessage;
import org.dosimonline.models.connection.NewConnection;
import org.dosimonline.models.connection.NotificationModel;
import org.dosimonline.models.connection.PlayerSummary;
import org.dosimonline.models.connection.PlayersUpdate;
import org.dosimonline.models.entities.Dos;
import org.newdawn.slick.Color;
/**
 *
 * @author gilnaa
 */
public class DosimOnlineServer extends Listener implements Runnable{
	private Server server;
	private int connectionCount;
	private int maxPlayers;
	
	public DosimOnlineServer(int udpPort, int tcpPort, int maxPlayers) throws IOException {
		this.maxPlayers = maxPlayers;
		this.connectionCount = 0;
		
		server = new Server();
		server.start();
		server.bind(tcpPort, udpPort);
		server.addListener(this);
		register(server.getKryo());
	}
	
	@Override
	public void received (Connection c, Object obj) {
		DosConnection connection = (DosConnection)c;
		
		if(connection.getName() == null) {
			if(connectionCount >= maxPlayers) {
				connection.close();
				return;
			}
			if(obj instanceof NewConnection){
				connection.setName(((NewConnection)obj).playerName);
				// Notify all connected player about the new connection.
				NotificationModel m = new NotificationModel();
				m.color = Color.blue;
				m.message = connection.getName() + " has connected.";
				server.sendToAllTCP(m);
				// Increase the player count.
				connectionCount++;
			} else {
				return;
			}
		}
		
		if(obj instanceof ChatMessage) {
			ChatMessage m = (ChatMessage)obj;
			m.id = connection.getID();
			server.sendToAllExceptTCP(connection.getID(), obj);
		}
	}
	
	@Override
	public void disconnected (Connection c) {
		if(((DosConnection)c).getName() == null)
			return;
		connectionCount--;
	}

	private void register(Kryo kryo) {
		kryo.register(ChatMessage.class);
		kryo.register(NewConnection.class);
		kryo.register(PlayerSummary.class);
		kryo.register(PlayerSummary[].class);
		kryo.register(PlayersUpdate.class);
		kryo.register(NotificationModel.class);
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
