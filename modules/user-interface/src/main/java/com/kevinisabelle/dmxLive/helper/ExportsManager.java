package com.kevinisabelle.dmxLive.helper;


import com.kevinisabelle.dmxlive.core.scripting.Playlist;
import com.kevinisabelle.dmxlive.core.scripting.Song;
import com.lowagie.text.DocumentException;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

/**
 * Manages the exports of play lists to PDF.
 * @author kisabelle
 */
public class ExportsManager {

	private static Logger logger = Logger.getLogger(ExportsManager.class);

	protected ExportsManager(){
		
	}
	
	/**
	 * Converts html text info pdf and saves it into a file.
	 * @param htmlText
	 * @param destinationFile
	 * @param imagesSourceFolder0
	 * @throws DocumentException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException 
	 */
	public static void convertToPDF(String htmlText, File destinationFile, String imagesSourceFolder0) throws DocumentException, IOException, ParserConfigurationException, SAXException {

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(htmlText.getBytes("UTF-8")));

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(doc, imagesSourceFolder0);

		OutputStream os = new FileOutputStream(destinationFile);
		renderer.layout();
		renderer.createPDF(os);
		os.close();
	}

	/**
	 * Converts a Playlist object into html text.
	 * @param playlist
	 * @return 
	 */
	public static String convertOrderToHTML(Playlist playlist) {

		logger.debug("Converting playlist to html...");
		/* Header */

		StringBuilder buf = new StringBuilder("<html><head></head><body>");

		buf.append("<center><h1>").append(convertStringToHTML(playlist.getTitle())).append("</h1></center><br/>");

		int i = 1;
		
		for (Song song : playlist.getSongs()){
			
			buf.append("<h1>")
					.append(i).append(") ")
					.append(convertStringToHTML(song.getTitle()))
					.append(" (").append(song.getSignature())
					.append(" @ ").append(song.getBpm())
					.append(" bpm)")
					.append("</h1>");
			
			i++;
			
		}

		buf.append("</body></html>");
		/* Produce the footer */

		logger.debug("done converting playlist to html.");

		return buf.toString();
	}
	
	private static String convertStringToHTML(String htmlString){
		
		return StringEscapeUtils.escapeXml(htmlString);
	}
}
