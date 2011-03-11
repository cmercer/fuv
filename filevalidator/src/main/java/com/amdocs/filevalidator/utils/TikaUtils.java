package com.amdocs.filevalidator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TikaUtils {

	/** Logger */
	@XmlTransient
	private static Logger logger = LoggerFactory.getLogger(TikaUtils.class);
	
	/**
	 * Runs Tika and returns the content type
	 */
	public static String getTikaContentType(File file) {
		ContentHandlerDecorator contenthandler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
		Parser parser = new AutoDetectParser();
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			parser.parse(in, contenthandler, metadata, new ParseContext());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while determining mime-type from input stream. {}", e.getMessage(), e);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Error while closing file. {}", e.getMessage(), e);
				}
			}
		}
		
		String contentType = metadata.get(Metadata.CONTENT_TYPE);
		return contentType;
	}

}
