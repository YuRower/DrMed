package processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.awt.Desktop;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class GenerateDocx {
	private final static Logger LOGGER = Logger.getLogger(GenerateDocx.class);

	private static final String MAIN_DOCUMENT_PATH = "word/document.xml";

	public static Boolean generateAndSendDocx(String templateName, Map<String, Object> substitutionData,
			String TEMPLATE_DIRECTORY_ROOT, boolean flagEnd) {
		LOGGER.debug("method generateAndSendDocx ");
		LOGGER.debug(" file for replacing data " + substitutionData);
		LOGGER.debug("tmp file " + templateName);

		String templateLocation = TEMPLATE_DIRECTORY_ROOT + templateName;
		LOGGER.debug(templateLocation);
		String userTempDir = null;
		userTempDir = TEMPLATE_DIRECTORY_ROOT + "/";
		LOGGER.debug(userTempDir);

		try {
			unzip(new File(templateLocation), new File(userTempDir));
			changeData(new File(userTempDir + MAIN_DOCUMENT_PATH), substitutionData);
	//		clearTmpinformation(new File(userTempDir + MAIN_DOCUMENT_PATH), substitutionData);

			zip(new File(userTempDir), new File(userTempDir + templateName));
			deleteTempData(new File(userTempDir));
			if (flagEnd) {
				openDocx(new File(userTempDir + templateName));
			}
		} catch (IOException ioe) {
			LOGGER.error(ioe);
			return false;
		}

		return true;
	}

	private static void openDocx(File file) {
		LOGGER.info("method openDocx");
		try {
			if (Desktop.isDesktopSupported()) {
				LOGGER.info("File will be opened");

				Desktop.getDesktop().open(file);
			}

		} catch (IOException ioe) {
			LOGGER.error(ioe.getCause());
		}
		LOGGER.debug(file.getAbsoluteFile());

	}

	private static void unzip(File zipfile, File directory) throws IOException {
		LOGGER.info("method unzip");
		LOGGER.debug(zipfile + " " + directory);

		ZipFile zfile = new ZipFile(zipfile);
		Enumeration<? extends ZipEntry> entries = zfile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File file = new File(directory, entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				file.getParentFile().mkdirs();
				InputStream in = zfile.getInputStream(entry);
				try {
					LOGGER.debug(file);
					copy(in, file);
				} finally {
					in.close();
				}
			}
		}
		LOGGER.info("Done Unziping!!");
	}

	private static void changeData(File targetFile, Map<String, Object> substitutionData) throws IOException {
		LOGGER.info("method changeData");
		LOGGER.debug("file wiche will be changed " + targetFile);
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
			LOGGER.error(e.fillInStackTrace());
			br.close();
			throw e;
		}
		Iterator<?> substitutionDataIterator = substitutionData.entrySet().iterator();
		while (substitutionDataIterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, String> pair = (Map.Entry<String, String>) substitutionDataIterator.next();
			LOGGER.info("===================>" + pair.getKey() + " " + pair.getValue());
			if (docxTemplate.contains(pair.getKey())) {
				if (pair.getValue() != null) {
					docxTemplate = docxTemplate.replace(pair.getKey(), pair.getValue());
					LOGGER.info("Got somtinhg inside XML!!" + pair.getKey() + pair.getValue());
				} else {
					docxTemplate = docxTemplate.replace(pair.getKey(), "");
					LOGGER.debug("replace null");
				}
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);
			fos.write(docxTemplate.getBytes("UTF-8"));
			LOGGER.debug("Successfully wrote to the targetFile!!" + targetFile);
			fos.close();
		} catch (IOException e) {
			fos.close();
			throw e;
		}
	}


	private static void zip(File directory, File zipfile) throws IOException {
		LOGGER.info("method zip");
		LOGGER.debug(directory + " " + zipfile);
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		OutputStream out = new FileOutputStream(zipfile);
		Closeable res = out;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));

					} else {
						if (kid.getName().contains(".docx"))
							continue;
						zout.putNextEntry(new ZipEntry(name));
						copy(kid, zout);
						zout.closeEntry();
					}
				}
			}
		} finally {
			res.close();
		}
	}

	public static void deleteTempData(File file) throws IOException {
		LOGGER.info("method deleteTempData");
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					deleteTempData(fileDelete);
				}
				if (file.list().length == 0) {
					file.delete();
				}
			}
		} else {
			if (!file.getName().endsWith(".docx")) {
				file.delete();
			}
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	private static void copy(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}

	private static void copy(InputStream in, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			copy(in, out);
		} finally {
			out.close();
		}
	}

}