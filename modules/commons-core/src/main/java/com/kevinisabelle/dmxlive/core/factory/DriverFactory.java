package com.kevinisabelle.dmxlive.core.factory;

import com.kevinisabelle.dmxlive.api.Driver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Kevin
 */
public class DriverFactory {

	private static Logger logger = Logger.getLogger(DriverFactory.class);
	private List<Driver> loadedDrivers;

	public DriverFactory() throws IOException {

		loadDriversInClassPath();
	}

	private void loadDriversInClassPath() throws IOException {

		logger.info("Loading drivers in classpath...");
		
		File[] list = FileUtils.getFile(System.getProperty("java.class.path")).listFiles();

		for (File file : list) {
			System.out.println(file.getPath());

			if (!file.getName().contains(".jar")) {
				continue;
			}

			JarFile jar = new JarFile(file);

			ZipEntry entry = jar.getEntry("dlc.txt");

			if (entry == null) {

				continue;
			}

			InputStream stream = jar.getInputStream(entry);
			String manifestTxt = IOUtils.toString(stream, Charset.defaultCharset());
			AddDriverFromManifest(manifestTxt);
		}
		
		logger.info("Done loading drivers.");
	}

	private void AddDriverFromManifest(String manifestTxt) {
				
		logger.info("Driver available: " + manifestTxt);
	}

	public Driver getDriver(String name) {
		return loadedDrivers.stream().filter(dr -> dr.getName().equals(name)).findFirst().orElse(null);
	}
}
