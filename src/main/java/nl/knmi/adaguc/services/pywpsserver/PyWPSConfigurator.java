package nl.knmi.adaguc.services.pywpsserver;

import nl.knmi.adaguc.tools.ElementNotFoundException;
import nl.knmi.adaguc.config.ConfigurationReader;
import nl.knmi.adaguc.config.ConfiguratorInterface;
import nl.knmi.adaguc.tools.Debug;
import nl.knmi.adaguc.tools.MyXMLParser.XMLElement;

/**
 * 
 * @author maartenplieger
 * 
<?xml version="1.0" encoding="UTF-8"?>
<adaguc-services>
  <pywps-server>
    <pywpsexecutable>/home/c3smagic/code/maartenplieger/pywps-server/bin/adagucserver</pywpsexecutable>
    <export>ADAGUC_PATH=/home/c3smagic/code/maartenplieger/pywps-server/</export>
    <export>ADAGUC_TMP=/tmp</export>
    <export>ADAGUC_CONFIG=/home/c3smagic/code/maartenplieger/pywps-server/data/config/adaguc.autoresource.xml</export>
    <export>ADAGUC_DATARESTRICTION="FALSE"</export>
    <export>ADAGUC_LOGFILE=/tmp/adaguc.autoresource.log</export>
    <export>ADAGUC_ERRORFILE=/tmp/adaguc.autoresource.errlog</export>
    <export>ADAGUC_FONT=/home/c3smagic/code/maartenplieger/pywps-server/data/fonts/FreeSans.ttf</export>
    <export>ADAGUC_ONLINERESOURCE=http://adaguc-services/adagucserver?</export>
  </pywps-server>
</adaguc-services>
*/


public class PyWPSConfigurator implements ConfiguratorInterface {
	private static String PyWPSExecutable=null;
	private static String PyWPSConfigTemplate = null;
	private static String PyWPSOutputDir = null;	
	private static String PyWPSProcessesDir = null;
	private static String TempDir = null;
	private static String[] environmentVariables = {
	};
	static ConfigurationReader configurationReader = new ConfigurationReader ();
	public void doConfig(XMLElement  configReader) throws ElementNotFoundException {
		if(configReader.getNodeValue ("adaguc-services.pywps-server")==null){
			Debug.println("adaguc-services.pywps-server is not configured");
			return;
		}
		PyWPSExecutable      = configReader.getNodeValueMustNotBeUndefined ("adaguc-services.pywps-server.pywpsexecutable");
		PyWPSConfigTemplate  = configReader.getNodeValueMustNotBeUndefined ("adaguc-services.pywps-server.pywpsconfigtemplate");
		PyWPSOutputDir       = configReader.getNodeValueMustNotBeUndefined ("adaguc-services.pywps-server.pywpsoutputdir");
		PyWPSProcessesDir    = configReader.getNodeValueMustNotBeUndefined ("adaguc-services.pywps-server.pywpsprocessesdir");
		TempDir              = configReader.getNodeValueMustNotBeUndefined ("adaguc-services.pywps-server.tmp");
		environmentVariables = configReader.getNodeValues("adaguc-services.pywps-server.export");
	}

	public static String getPyWPSExecutable() throws ElementNotFoundException  {
		configurationReader.readConfig();
		return PyWPSExecutable;
	}
	
	public static String getTempDir() throws ElementNotFoundException  {
		configurationReader.readConfig();
		return TempDir;
	}
	
	public static String getPyWPSConfigTemplate() throws ElementNotFoundException {
		configurationReader.readConfig();
		return PyWPSConfigTemplate;
	}

	public static String[] getPyWPSEnvironment() throws ElementNotFoundException {
		configurationReader.readConfig();
		return environmentVariables;
	}

	public static String getPyWPSOutputDir() throws ElementNotFoundException {
		configurationReader.readConfig();
		return PyWPSOutputDir;
	}

	public static String getPyWPSProcessesDir() throws ElementNotFoundException {
		configurationReader.readConfig();
		return PyWPSProcessesDir;
	}

	public static String getPyWPSConfig() throws ElementNotFoundException {
		String pyWPSConfigTemplate = PyWPSConfigurator.getPyWPSConfigTemplate();
		return pyWPSConfigTemplate + "adaguc-services-pywps-config.cfg";
	}	
}