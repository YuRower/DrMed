package processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class ReportGenerator {

	private static final String MAIN_DOCUMENT_PATH = "Документ Microsoft Word.xml";
	private static final String TEMPLATE_DIRECTORY_ROOT = "C:\\Users\\bigda\\Desktop";

	public static Boolean generateAndSendDocx(String templateName, Map<String, String> substitutionData) {

		// String templateLocation = TEMPLATE_DIRECTORY_ROOT + templateName;

		String userTempDir = TEMPLATE_DIRECTORY_ROOT + "/"; // + userTempDir + "/";

		try {

			changeData(new File(userTempDir + MAIN_DOCUMENT_PATH), substitutionData);

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			return false;
		}

		return true;
	}

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
					docxTemplate = docxTemplate.replace(pair.getKey(), "not found");
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
