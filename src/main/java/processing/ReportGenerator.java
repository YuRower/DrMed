package processing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import javafx.fxml.FXML;

public class ReportGenerator {

	private static final String MAIN_DOCUMENT_PATH = "Документ Microsoft Word.xml";
	private static final String TEMPLATE_DIRECTORY_ROOT = "C:\\Users\\bigda\\Desktop";


	/* PUBLIC METHODS */

	/**
	 * Generates .docx document from given template and the substitution data
	 * 
	 * @param templateName     Template data
	 * @param substitutionData Hash map with the set of key-value pairs that
	 *                         represent substitution data
	 * @return
	 */
	
	public static Boolean generateAndSendDocx(String templateName, Map<String, String> substitutionData) {

		//String templateLocation = TEMPLATE_DIRECTORY_ROOT + templateName;

		String userTempDir = TEMPLATE_DIRECTORY_ROOT + "/"; // + userTempDir + "/";

		try {

			changeData(new File(userTempDir + MAIN_DOCUMENT_PATH), substitutionData);

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Substitutes keys found in target file with corresponding data
	 * 
	 * @param targetFile       Target file
	 * @param substitutionData Map of key-value pairs of data
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void changeData(File targetFile, Map<String, String> substitutionData) throws IOException {

		BufferedReader br = null;
		String docxTemplate = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null)
				docxTemplate = docxTemplate + temp;
			br.close();
			targetFile.delete();
		} catch (IOException e) {
			br.close();
			throw e;
		}

		Iterator substitutionDataIterator = substitutionData.entrySet().iterator();
		while (substitutionDataIterator.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry<String, String>) substitutionDataIterator.next();
			if (docxTemplate.contains(pair.getKey())) {
				if (pair.getValue() != null)
					docxTemplate = docxTemplate.replace(pair.getKey(), pair.getValue());
				else
					docxTemplate = docxTemplate.replace(pair.getKey(), "NEDOSTAJE");
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);
			fos.write(docxTemplate.getBytes("UTF-8"));
			fos.close();
		} catch (IOException e) {
			fos.close();
			throw e;
		}
	}

}
