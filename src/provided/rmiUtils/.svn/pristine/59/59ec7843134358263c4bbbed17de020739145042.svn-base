package provided.rmiUtils;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

import provided.rmiUtils.classServer.ClassFileServer;


/**
 * Concrete implementaion of the IRMIUtils, which are utilities to make it 
 * easier to start/stop the RMI system, access local/remote Registries and 
 * get the local IP address.   <em>It is NOT recommended that students write
 * their own RMI management code as this code has been thoroughly tested and 
 * should satify all the needs of the project.</em>
 * 
 * @author swong
 * 
 */
public class RMIUtils implements IRMIUtils {

	/**
	 * If true, then only detects private IP addresses.   
	 * Should be set false for server use (i.e. non-private addresses).   
	 * See auxiliary constructor. 
	 */
	private boolean privateAddrOnly = true;

	/**
	 * The last address selected for use.
	 */
	private String last_addr = null;

	
	/**
	 * Strategy to display an informational message.  Defaults to
	 * System.out.println()
	 */
	private Consumer<String> infoMsgStrategy = System.out::println;
	
	/**
	 * Strategy to display an error message.  Defaults to
	 * System.err.println()
	 */	
	private Consumer<String> errMsgStrategy = System.err::println;		

	/**
	 * Constructor for the class. Combines the given output command with the
	 * internal default commands to send status info to both the view connected 
	 * to by the input outputCmd as well as standard out and error.
	 * 
	 * @param outputCmd  command used to output status and error information
	 * @throws IllegalArgumentException If the given outputCmd is null.
	 */
	public RMIUtils(final Consumer<String> outputCmd) {
		// safety check
		if(null==outputCmd) {
			String errMsg = "[RMIUtils constructor] The given outputCmd function is null!";
			System.err.println(errMsg);
			throw new IllegalArgumentException(errMsg);
		}
		
		// Combine the new display strategies with the ones to ensure that the messages.
		// get displayed on stdout and stderr as well as on the GUI.
		final Consumer<String> defaultInfoMsgStrategy = this.infoMsgStrategy;
		final Consumer<String> defaultErrMsgStrategy = this.errMsgStrategy;
		this.infoMsgStrategy = (s) -> {
			defaultInfoMsgStrategy.accept(s);
			outputCmd.accept(s);
		};
		this.errMsgStrategy = (s) -> {
			defaultErrMsgStrategy.accept(s);
			outputCmd.accept("[ERROR!!] "+s);
		};
	}

	/**
	 * Auxiliary constructor used to set the getLocalAddress() method to return non-private addresses, 
	 * e.g. when used on a server rather than a wireless laptop.
	 * @param outputCmd  command used to output status and error information
	 * @param privateAddrOnly  If false, will look for non-private IP addresses as well.
	 */
	public RMIUtils(Consumer<String> outputCmd, boolean privateAddrOnly) {
		this(outputCmd);
		this.privateAddrOnly = privateAddrOnly;
	}

	/**
	 * Returns the Registry on the local machine on port
	 * IRMI_Defs.REGISTRY_PORT, as a server would require. Creates the Registry
	 * if it isn't already running. Returns null system if unable to create a
	 * Registry if needed.
	 * 
	 * @return the Registry or null if no Registry could be made or found.
	 */
	@Override
	public Registry getLocalRegistry() {

		try {
			// First try to create a registry
			return LocateRegistry.createRegistry(IRMI_Defs.REGISTRY_PORT);
		} catch (RemoteException e) {
			showInfoMsg("[RMIUtils.getLocalRegistry()] Could not create registry: " + e + "\n"+
					"Attempting to locate existing registry...");
			try {
				// Creating a new registry will fail if it already exists, so
				// try to find it.
				Registry registry = LocateRegistry
						.getRegistry(IRMI_Defs.REGISTRY_PORT);
				showInfoMsg("[RMIUtils.getLocalRegistry()] Success! Found Registry: "
						+ registry);
				return registry;
			} catch (RemoteException e2) {
				showErrorMsg("[RMIUtils.getLocalRegistry()] Could not get registry on port "
						+ IRMI_Defs.REGISTRY_PORT + ". \n" + e);
				return null;
			}
		}
	}

	/**
	 * Returns the Registry on the given machine on port
	 * IRMI_Defs.REGISTRY_PORT, as a client would require.
	 * 
	 * 
	 * @param host
	 *            the IP address or host name of the remote machine.
	 * @return The remote Registry or null if it could not be located.
	 * @throws IllegalArgumentException if host is null or an empty string.
	 */
	@Override
	public Registry getRemoteRegistry(String host) {
		// safety check
		if(null==host || "".equals(host)) {
			String errMsg = "[RMIUtils.getRemoteRegistry()] The given host value is either null or an empty string!";
			errMsgStrategy.accept(errMsg);	
			throw new IllegalArgumentException(errMsg);
		}
		try {
			Registry registry = LocateRegistry.getRegistry(host,
					IRMI_Defs.REGISTRY_PORT);
			showInfoMsg("[RMIUtils.getRemoteRegistry()] Success! Found Registry: "
					+ registry);
			return registry;
		} catch (RemoteException e) {
			showErrorMsg("[RMIUtils.getRemoteRegistry()] Could not get registry at "
					+ host + ":" + IRMI_Defs.REGISTRY_PORT + ". \n" + e);
			return null;
		}
	}

	/**
	 * This method is designed to reliably return the actual local IP address
	 * across multiple platforms, particularly Linux. This method is a
	 * replacement for "java.net.InetAddress.getLocalHost().getHostAddress()"
	 * which will return the loopback address in Linux, not the actual IP
	 * address. Also, Java tends to report many virtual network adapters as 
	 * "non-virtual".  This method only returns IPv4 addresses, not IPv6 addresses,
	 * which are non-loopback, operational and non-virtual, as reported by Java.   
	 * By default, privateAddrOnly is set to true, so only private IP addresses are 
	 * detected.   If privateAddr = false (having used the auxiliary constructor)
	 * then all valid addresses are detected. If multiple potentially valid addresses are 
	 * detected, then a dialog box will pop up to ask the user to select the proper address.
	 * The selected address is cached so the next call to getLocalAddress() will simply return
	 * the cached address value.
	 * 
	 * @return A non-loopback, non-virtual IPv4 address found for the system.
	 * @throws SocketException
	 *             thrown when there is a problem retrieving the network
	 *             interfaces.
	 * @throws UnknownHostException
	 *             thrown when the local host address cannot be found.
	 */
	@Override
	public String getLocalAddress() throws SocketException,
	UnknownHostException {
		if (last_addr == null) {  // Only look for the address once

			// The code below is needed for Linux to find the host's real
			// (non-loopback) IP address.
			Enumeration<NetworkInterface> nics = NetworkInterface
					.getNetworkInterfaces();
			ArrayList<String> addr_choices = new ArrayList<String>();  // list of possible addresses
			while (nics.hasMoreElements()) {
				NetworkInterface nic = nics.nextElement();
				if (nic.isUp() && !nic.isVirtual()) {
					Enumeration<InetAddress> addrs = nic.getInetAddresses();
					while (addrs.hasMoreElements()) {
						InetAddress addr = addrs.nextElement();
						if (!addr.isLoopbackAddress() && (addr instanceof Inet4Address)) {
							showInfoMsg("[RMIUtils.getLocalAddress()] Found address = " + addr.getHostAddress() );

							if(privateAddrOnly) {  // Only add the address if it is a private address
								addPrivateAddr(addr.getHostAddress(), addr_choices);
							}
							else {  // Always add the address
								addr_choices.add(addr.getHostAddress());							
							}
						}
					}
				}
			}

			switch(addr_choices.size()) {
			case 0:  // couldn't find an address in the above search process, so go with Java's default address result if there is one.
				showInfoMsg("[RMIUtils.getLocalAddress()] The potentially filtered address search returned no results.  Defaulting to Java's default address, if it exists.\n");
				last_addr =  java.net.InetAddress.getLocalHost().getHostAddress();
				break;

			case 1:  // only one address found
				last_addr = addr_choices.get(0);
				break;

			default:  // Multiple potential addresses found
				String[] addr_array = addr_choices.toArray(new String[addr_choices.size()]);

				int addrIdx = JOptionPane.showOptionDialog(null, "Select the IP address of the physical network adapter:","Multiple IP Addresses Found!",JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,  addr_array, addr_array[0]); 
				if (addrIdx == JOptionPane.CLOSED_OPTION) {
					addrIdx = 0;   // Just take the first address in this case.
				}
				last_addr = addr_array[addrIdx];
				break;

			}
		}
		return last_addr;
	}

	/**
	 * Adds the given host address to the addr_choices ArrayList if the host address is a private address, i.e.
	 * 10.x.x.x, 192.188.x.x or 172.16.x.x-172.31.x.x
	 * @param hostAddress  The host address to check and possibly add
	 * @param addr_choices  The ArrayList to add the host address to if it is a private address
	 */
	private void addPrivateAddr(String hostAddress,	ArrayList<String> addr_choices) {
		if(hostAddress.startsWith("10.") || hostAddress.startsWith("192.168.")){
			addr_choices.add(hostAddress);
		}
		else if (hostAddress.startsWith("172.")) {
			String[] digits = hostAddress.split("\\.");   // Note that split() uses regex so must escape the period character.
			int d2 = Integer.parseInt(digits[1]);
			if ((16 <= d2) && (d2<32)){
				addr_choices.add(hostAddress);
			}
		}
	}

	/**
	 * Start up the RMI system.   This method should be called 
	 * before any other activity with that involves RMI.   This method
	 * configures the system properties RMI needs, starts the security
	 * manager and starts the class server. <br>
	 * Typical classServerPort values are: <br>
	 * IRMI_Defs.CLASS_SERVER_PORT_SERVER and <br>
	 * IRMI_Defs.CLASS_SERVER_PORT_CLIENT
	 * @param classServerPort the port that the class server will use
	 */
	@Override
	public void startRMI(int classServerPort){
		configSecurityManager();
		configRMIProperties(classServerPort);
		startClassFileServer(classServerPort);

	}

	/**
	 * Sets the java.rmi.server.hostname and java.rmi.server.codebase
	 * system properties which control the automatic remote dynamic 
	 * class loading.  This must be called before starting the class server.
	 * @param classServerPort  The port the class server will use.
	 */
	private void configRMIProperties(int classServerPort) {
		// Logs all RMI activity to System.err
		System.setProperty("java.rmi.server.logCalls", "true"); 

		try {
			// Try to get figure out this server's IP address and save it as the
			// RMI server hostname.
			System.setProperty("java.rmi.server.hostname", getLocalAddress());

			System.setProperty("java.rmi.server.codebase",
					"http://" + System.getProperty("java.rmi.server.hostname")
					+ ":" + classServerPort + "/");
			System.setProperty("java.rmi.server.useCodebaseOnly", "false"); // Must be false to allow remote class dynamic loading (defaults to true for JDK 1.7+)
			showInfoMsg("[RMIUtils.configRMIProperties()] Configured system properties:\n"+
					"java.rmi.server.hostname: "
					+ System.getProperty("java.rmi.server.hostname") + "\n"+
					"java.rmi.server.codebase: "
					+ System.getProperty("java.rmi.server.codebase") + "\n"+
					"java.rmi.server.useCodebaseOnly: "
					+ System.getProperty("java.rmi.server.useCodebaseOnly") );

		} catch (Exception e) {
			showErrorMsg("[RMIUtils.configRMIProperties()] Error getting local host address: " + e + "\n");
		}

	}

	/**
	 * Sets the java.security.policy system property to point at the location 
	 * of the security policy file, which is assumed to be at 
	 * "provided\rmiUtils\server.policy"  (file separators adjusted to 
	 * match operating system).   the security manager is then started.
	 * This method must be called before starting the
	 * class server.
	 */
	private void configSecurityManager() {
		// file.separator is "\" in Windows and "/" in Unix/Linux/Mac.
		String sep = System.getProperty("file.separator");
		String classpath = System.getProperty("user.dir"); // Need this because File cannot find policy if user.dir has been changed from its default when given a relative pathname.
		String policyFilePath = classpath+sep+"provided" + sep + "rmiUtils" + sep + "server.policy";

		File policyFile = new File(policyFilePath); // Better robustness if an absolute path is used here.

		System.out.println("policyFile = "+policyFile+", "+policyFile.getAbsolutePath());

		if (!policyFile.isFile()) {
		    showErrorMsg("[ERROR!!] [RMIUtils.configSecurityManager()] <><><> !!! Security policy FILE NOT FOUND !!! <><><>\n" +
	            "Expected file at " + policyFile.getAbsolutePath() +
	            "\nJava security exceptions are likely.\n");
		}

		System.setProperty("java.security.policy", policyFilePath);
		showInfoMsg("[RMIUtils.configSecurityManager()] java.security.policy: "
				+ System.getProperty("java.security.policy"));

		// Start the security manager
		if (System.getSecurityManager() == null) {
			showInfoMsg("[RMIUtils.configSecurityManager()] Installing new Security Manager...");
			System.setSecurityManager(new SecurityManager());
			showInfoMsg("[RMIUtils.configSecurityManager()] Security Manager = " + System.getSecurityManager());
		}
	}

	/**
	 * A class file server to enable remote dynamic class loading of the
	 * ICompute object.
	 */
	private ClassFileServer classFileServer = null;


	/**
	 * Start the class file server to support remote dynamic class loading.
	 * This method must be called after configSecurityManager() and 
	 * configRMIProperties().   If the reference to the class file 
	 * server, "classFileServer" is null, it is assumed that the class file
	 * server is not runnng.
	 * @param classServerPort  the port the class file server will use.
	 */
	private void startClassFileServer(int classServerPort) {
		if (null != classFileServer)
			stopClassFileServer();

		String userDir = System.getProperty("user.dir");
		showInfoMsg("[RMIUtils.startClassFileServer()] user.dir = " + userDir);
		try {
			classFileServer = new ClassFileServer(classServerPort,
					System.getProperty("user.dir"), infoMsgStrategy, errMsgStrategy);
		} catch (java.io.IOException e) {
			showErrorMsg("[RMIUtils.startClassFileServer()] Unable to start ClassServer: " + e.getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * Stop the class file server and reset the reference to it, 
	 * "classFileServer", to null.
	 */
	private void stopClassFileServer() {
		classFileServer.stop();
		classFileServer = null;
	}

	/**
	 * Performs all necessary work to shut the RMI system down,
	 * such as shut the class server down.   
	 * This method MUST be called before exiting the system or phantom 
	 * processes may persist. 
	 */
	@Override
	public void stopRMI() {
		stopClassFileServer();
	}
	
	/**
	 * Encapsulated behavior to show an informational message
	 * @param msg  Message to show.
	 */
	protected void showInfoMsg(String msg) {
		this.infoMsgStrategy.accept(msg);
	}

	/**
	 * Encapsulated behavior to show an error message
	 * @param msg  Message to show.
	 */
	protected void showErrorMsg(String msg) {
		this.errMsgStrategy.accept(msg);
	}	

}
