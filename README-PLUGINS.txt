-----PLUGINS-----

1) How they work
	There are 2 plugins, global and local. Plugins will load automatically however they must
	be in the package "com.rs2.model.util.plugin.impl"
	
	Global plugins will only be instantiated once and serve a more
	generalised purpose, for example, announcing server-wide messages (see example).
	
	On the other hand, each player will have their own instance of a local plugin,
	this allows the plugin to manipulate per-player data, for example, a woodcutting plugin (see example).
	
2) How to create a plugin
	Creating a plugin is easy, first choose whether the plugin will be a global or a local type (explained above).
	
	Once created, a plugin must inherit these methods:
	
	String getName() - Returns the name of the plugin.
	String getAuthor() - Returns the name of the author of the plugin.
	double getVersion() - Returns the decimal version of the plugin.
	void onCreate() - Called when the plugin is created.
	void onDestroy() - Called when the plugin is destroyed.
	
	There are also other methods available, check out the appropriate javadocs for more information.

Written by Tommo (Fusion T)