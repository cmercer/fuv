package com.amdocs.filevalidator.modules;

import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileTypeModuleTest {

	private static final String WORDFILE_DOCX = "src/test/resources/wordfile.docx";
	private static final String WORDFILE_WRONG = "src/test/resources/wordfile.wrong-ext";
	private static final String WORDFILE_OLD_DOCX = "src/test/resources/wordfile-old.doc";
	private static final String WORDFILE_OLD_WRONG = "src/test/resources/wordfile-old.wrong-ext";
	private static final String TEXTFILE = "src/test/resources/textfile";
	private static final String PDFFILE_PDF = "src/test/resources/pdffile.pdf";
	private static final String PDFFILE_WRONG = "src/test/resources/pdffile.wrong-ext";
	private static final String JPGFILE_JPG = "src/test/resources/jpgfile.jpg";
	private static final String JPGFILE_WRONG = "src/test/resources/jpgfile.wrong-ext";
	private static final String JPGFILE_JPEG = "src/test/resources/jpgfile.JPEG";
	
	
	private static FileTypeModule moduleAllowedWordCheckExt;
	private static String WORD_EXT = "<file-type-module><allowed-types>word</allowed-types><force-ext-check/></file-type-module>";
	private static FileTypeModule moduleAllowedWordNotCheckExt;
	private static String WORD_NOT_EXT = "<file-type-module><allowed-types>WORD</allowed-types></file-type-module>";
	private static FileTypeModule moduleAllowedWordPdfCheckExt;
	private static String WORD_PDF_EXT = "<file-type-module><allowed-types>WORD, pdf</allowed-types><force-ext-check/></file-type-module>";
	private static FileTypeModule moduleAllowedWordPdfNotCheckExt;
	private static String WORD_PDF_NOT_EXT = "<file-type-module><allowed-types>WORD pdf</allowed-types></file-type-module>";
	private static FileTypeModule moduleAllowedPdfJpgCheckExt;
	private static String PDF_JPG_EXT = "<file-type-module><allowed-types>pdf images</allowed-types><force-ext-check/></file-type-module>";
	
	
	
	/**
	 * Extracts the filename from a file path (which can include or not a directory before)
	 */	
	public static String extractFileName(String filePath) {		
		return filePath.substring(filePath.lastIndexOf(File.separator) + File.separator.length());
	}
	
	@BeforeClass
	public static void init() throws Exception { 
		JAXBContext jc = JAXBContext.newInstance(FileTypeModule.class);
		Unmarshaller um = jc.createUnmarshaller();
		
		moduleAllowedWordCheckExt = (FileTypeModule)um.unmarshal(new StringReader(WORD_EXT));
		moduleAllowedWordNotCheckExt = (FileTypeModule)um.unmarshal(new StringReader(WORD_NOT_EXT));
		moduleAllowedWordPdfCheckExt = (FileTypeModule)um.unmarshal(new StringReader(WORD_PDF_EXT));
		moduleAllowedWordPdfNotCheckExt = (FileTypeModule)um.unmarshal(new StringReader(WORD_PDF_NOT_EXT));
		moduleAllowedPdfJpgCheckExt = (FileTypeModule)um.unmarshal(new StringReader(PDF_JPG_EXT));
	}
	
	@Test
	public void testAllowedWordCheckExt() throws Exception { 
		Assert.assertEquals(true,  moduleAllowedWordCheckExt.validate(new File(WORDFILE_DOCX), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(WORDFILE_WRONG), false));
		Assert.assertEquals(true,  moduleAllowedWordCheckExt.validate(new File(WORDFILE_OLD_DOCX), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(WORDFILE_OLD_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(TEXTFILE), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(PDFFILE_PDF), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(PDFFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPEG), false));
	}
	
	@Test
	public void testAllowedWordNotCheckExt() throws Exception {
		Assert.assertEquals(true,  moduleAllowedWordNotCheckExt.validate(new File(WORDFILE_DOCX), false));
		Assert.assertEquals(true,  moduleAllowedWordNotCheckExt.validate(new File(WORDFILE_WRONG), false));
		Assert.assertEquals(true,  moduleAllowedWordNotCheckExt.validate(new File(WORDFILE_OLD_DOCX), false));
		Assert.assertEquals(true,  moduleAllowedWordNotCheckExt.validate(new File(WORDFILE_OLD_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(TEXTFILE), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(PDFFILE_PDF), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(PDFFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPEG), false));
	}
	
	
	@Test
	public void testAllowedWordPdfCheckExt() throws Exception { 
		Assert.assertEquals(true,  moduleAllowedWordPdfCheckExt.validate(new File(WORDFILE_DOCX), false));
		Assert.assertEquals(false, moduleAllowedWordPdfCheckExt.validate(new File(WORDFILE_WRONG), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfCheckExt.validate(new File(WORDFILE_OLD_DOCX), false));
		Assert.assertEquals(false, moduleAllowedWordPdfCheckExt.validate(new File(WORDFILE_OLD_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordPdfCheckExt.validate(new File(TEXTFILE), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfCheckExt.validate(new File(PDFFILE_PDF), false));
		Assert.assertEquals(false, moduleAllowedWordPdfCheckExt.validate(new File(PDFFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPEG), false));
	}
	
	@Test
	public void testAllowedWordPdfNotCheckExt() throws Exception {
		Assert.assertEquals(true,  moduleAllowedWordPdfNotCheckExt.validate(new File(WORDFILE_DOCX), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfNotCheckExt.validate(new File(WORDFILE_WRONG), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfNotCheckExt.validate(new File(WORDFILE_OLD_DOCX), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfNotCheckExt.validate(new File(WORDFILE_OLD_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordPdfNotCheckExt.validate(new File(TEXTFILE), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfNotCheckExt.validate(new File(PDFFILE_PDF), false));
		Assert.assertEquals(true,  moduleAllowedWordPdfNotCheckExt.validate(new File(PDFFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedWordCheckExt.validate(new File(JPGFILE_JPEG), false));
	}

	@Test
	public void testAllowedPdfJpgNotCheckExt() throws Exception {
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(WORDFILE_DOCX), false));
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(WORDFILE_WRONG), false));
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(WORDFILE_OLD_DOCX), false));
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(WORDFILE_OLD_WRONG), false));
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(TEXTFILE), false));
		Assert.assertEquals(true,  moduleAllowedPdfJpgCheckExt.validate(new File(PDFFILE_PDF), false));
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(PDFFILE_WRONG), false));
		Assert.assertEquals(true,  moduleAllowedPdfJpgCheckExt.validate(new File(JPGFILE_JPG), false));
		Assert.assertEquals(false, moduleAllowedPdfJpgCheckExt.validate(new File(JPGFILE_WRONG), false));
		Assert.assertEquals(true,  moduleAllowedPdfJpgCheckExt.validate(new File(JPGFILE_JPEG), false));
	}
}
