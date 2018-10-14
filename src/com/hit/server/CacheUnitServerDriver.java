package com.hit.server;

import com.hit.util.CLI;

public class CacheUnitServerDriver {

	public static void main(String[] args) {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server();
		cli.addPropertyChangeListener(server); // this way the server will listen for a CLI property
		new Thread(cli).start();

	}

}
