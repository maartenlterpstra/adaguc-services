package nl.knmi.adaguc.security.user;

import java.io.IOException;


import lombok.Getter;
import nl.knmi.adaguc.tools.ElementNotFoundException;
import nl.knmi.adaguc.config.MainServicesConfigurator;
import nl.knmi.adaguc.security.PemX509Tools;
import nl.knmi.adaguc.security.PemX509Tools.X509UserCertAndKey;
import nl.knmi.adaguc.security.SecurityConfigurator;
import nl.knmi.adaguc.tools.Debug;
import nl.knmi.adaguc.tools.Tools;

public class User {
	@Getter
	String homeDir = null;

	@Getter
	String userId = null;

	@Getter
	String dataDir = null;

	private X509UserCertAndKey userCert;


	public static String makePosixUserId(String userId){
		if (userId == null)
			return null;

		userId = userId.replace("http://", "");
		userId = userId.replace("https://", "");
		userId = userId.replaceAll("/", ".");
		return userId;
	}


	public User(String _id) throws IOException, ElementNotFoundException {
		Debug.println("New user ID is made :["+_id+"]");
		String userWorkspace = MainServicesConfigurator.getUserWorkspace();
		userId = makePosixUserId(_id);
		homeDir=userWorkspace+"/"+userId;
		dataDir = homeDir+"/data";
		Tools.mksubdirs(homeDir);
		Tools.mksubdirs(dataDir);
		Debug.println("User Home Dir: "+homeDir);
	}

	/**
	 * Create NetCDF .httprc or .dodsrc resource file and store it in the users
	 * home directory
	 * 
	 * @param user
	 *          The user object
	 * @throws IOException
	 * @throws ElementNotFoundException 
	 */
	private synchronized void createNCResourceFile()
			throws IOException, ElementNotFoundException {
		String fileContents = 
				"HTTP.SSL.VALIDATE=0\n" + 
				"HTTP.COOKIEJAR=" + this.homeDir + "/.dods_cookies\n" + 
				"HTTP.SSL.CERTIFICATE="	+ this.homeDir + "/cert.crt" + "\n" +
				"HTTP.SSL.KEY="	+ this.homeDir + "/cert.key" + "\n" + 
				"HTTP.SSL.SSLv3="+this.homeDir + "/cert.crt"+"\n" +
				"HTTP.SSL.CAPATH="+ SecurityConfigurator.getTrustRootsCADirectory();
		Debug.println("createNCResourceFile for user "+this.userId+":\n"+fileContents);
		Tools.writeFile(this.homeDir + "/.httprc", fileContents);
		Tools.writeFile(this.homeDir + "/.dodsrc", fileContents);
	}
	public void setCertificate(X509UserCertAndKey userCert) throws IOException, ElementNotFoundException {
		/* TODO could optinally write cert to user basket */

		
		PemX509Tools.writeCertificateToPemFile(userCert.getUserSlCertificate(), this.homeDir + "/cert.crt");
		PemX509Tools.writePrivateKeyToPemFile(userCert.getPrivateKey(), this.homeDir + "/cert.key");
	
		this.userCert = userCert;
		createNCResourceFile();
	}

	public X509UserCertAndKey getCertificate() {
		return this.userCert;
	}

}
