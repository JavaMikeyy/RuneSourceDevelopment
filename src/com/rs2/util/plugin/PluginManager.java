package com.rs2.util.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;

/**
 * Manages all plugins for the server.
 * @author Tommo
 *
 */
public class PluginManager {
	
	/**
	 * A linked-list of currently loaded plugins.
	 */
	private static List<AbstractPlugin> plugins = new LinkedList<AbstractPlugin>();
	
	/**
	 * Loads plugins located in the com.rs2.util.plugin.impl package.
	 */
	public static void loadPlugins() {
		try {
			System.out.println("Loading plugins..");
			Class[] pluginClasses = getClasses("com.rs2.util.plugin.impl");
			
			for (Class s : pluginClasses) {
				AbstractPlugin p = (AbstractPlugin) s.newInstance();
				register(p);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called every server cycle (600ms).
	 * Calls onTick() in the plugin.
	 */
	public static void tick() {
		synchronized (plugins) {
			Iterator<AbstractPlugin> iter = plugins.iterator();
			
			while (iter.hasNext()) {
				iter.next().onTick();
			}
		}
	}
	
	/**
	 * Powers the plug-in's tick (so long as requested in plugin)
	 */
	public static void reset() {
		synchronized (plugins) {
			Iterator<AbstractPlugin> iter = plugins.iterator();
			
			while (iter.hasNext()) {
				iter.next().reset();
			}
		}
	}
	
	/**
	 * Loops through the plugins and
	 * calls onDestroy().
	 */
	public static void close() {
		synchronized (plugins) {
			Iterator<AbstractPlugin> iter = plugins.iterator();
			
			while (iter.hasNext()) {
				iter.next().onDestroy();
			}
		}
	}
	
	/**
	 * Registers a new plugin.
	 * @param plugin The plugin to register.
	 */
	private static void register(AbstractPlugin plugin) {
		synchronized (plugins) {
			System.out.println("Loaded plugin: " + plugin.getName() + " v" + plugin.getVersion() + " by " + plugin.getAuthor());
			plugin.onCreate();
			plugins.add(plugin);
		}
	}
	
	/**
	 * De-registers the specified plugin.
	 * @param plugin The plugin to de-register.
	 */
	@Deprecated
	private static void deregister(AbstractPlugin plugin) {
		synchronized (plugins) {
			plugin.onDestroy();
			plugins.remove(plugin);
		}
	}
	
	/**
	 * Returns all of the classes found in the specified package.
	 * @param packageName The package name
	 * @return An array of generic class objects - the plugin classes.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Returns a list of classes found in the specified directory.
     * @param directory The directory.
     * @param packageName The package name.
     * @return A list of found classes.
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
    
	/**
	 * Dispatches the packet arrival event to active plugins.
	 * @param player The player receiving the packet.
	 * @param packet The received packet.
	 * @return Return TRUE if this packet should be processed elsewhere, 
	 * return FALSE if this packet should not be processed elsewhere.
	 */
    public static boolean onPacketArrival(Player player, Packet packet) {
		synchronized (plugins) {
			Iterator<AbstractPlugin> iter = plugins.iterator();
			
			while (iter.hasNext()) {
				if (!iter.next().onPacketArrival(player, packet)) return false;
			}
		}
    	return true;
    }
    
    /**
     * Dispatches an onPlayerTick event
     * allowing for the plugin
     * to manipulate player specific data.
     * @param player The player.
     */
    public static void onPlayerTick(Player player) {
		synchronized (plugins) {
			Iterator<AbstractPlugin> iter = plugins.iterator();
			
			while (iter.hasNext()) {
				iter.next().onPlayerTick(player);
			}
		}
    }

}
