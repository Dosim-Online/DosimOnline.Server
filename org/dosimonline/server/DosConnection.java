package org.dosimonline.server;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author gilnaa
 */
public class DosConnection extends Connection {
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}
