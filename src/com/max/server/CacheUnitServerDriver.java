package com.max.server;

import com.max.util.CLI;

public class CacheUnitServerDriver {

	public static void main(String[] args) {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server();
		cli.addPropertyChangeListener(server);
		new Thread(cli).start();

	}

}
