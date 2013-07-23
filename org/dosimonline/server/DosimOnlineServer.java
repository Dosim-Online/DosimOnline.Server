package org.dosimonline.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.ArrayList;
import org.dosimonline.models.connection.ChatMessage;
import org.dosimonline.models.connection.NewConnection;
import org.dosimonline.models.connection.PlayerSummary;
import org.dosimonline.models.connection.PlayersUpdate;
import org.dosimonline.models.entities.Dos;
/**
 *
 * @author gilnaa
 */
public class DosimOnlineServer extends Listener {
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
		kryo.register(Dos.class);
		kryo.register(PlayerSummary.class);
		kryo.register(PlayerSummary[].class);
		kryo.register(PlayersUpdate.class);
	}
}
