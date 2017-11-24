package ro.kuberam.getos.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmValue;

public final class Utils {

	private final static String TAG = Utils.class.getSimpleName();

	public static void showAlert(AlertType type, String header, String content) {
		Alert alert = new Alert(type);

		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	public static void showAlert(AlertType type, Exception ex) {
		String content;

		if (ex.getCause() != null) {
			content = ex.getCause().getLocalizedMessage();
		} else {
			content = ex.getLocalizedMessage();
		}

		Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
		showAlert(AlertType.ERROR, null, content);
	}

	public static String getExtension(File file) {
		String name = file.getName();
		int i = file.getName().lastIndexOf('.');
		if (i >= 0 && name.length() >= i + 1) {
			return file.getName().substring(i + 1).toLowerCase();
		} else {
			return "";
		}
	}

	public static XdmValue transform(File xml, InputStream xquery, boolean omitXmlDeclaration, URI baseURI,
			Map<String, String> parameters) throws SaxonApiException, IOException {
		XdmValue result = null;

		Source xmlSrc = new StreamSource(xml);

		Processor proc = new Processor(true);
		XQueryCompiler xqueryCompiler = proc.newXQueryCompiler();

		if (baseURI != null) {
			xqueryCompiler.setBaseURI(baseURI);
		}

		XQueryExecutable xqueryExecutable = xqueryCompiler.compile(xquery);
		XQueryEvaluator xqueryEvaluator = xqueryExecutable.load();
		xqueryEvaluator.setSource(xmlSrc);

		if (parameters != null) {
			for (Entry<String, String> parameter : parameters.entrySet()) {
				xqueryEvaluator.setExternalVariable(new QName(parameter.getKey()),
						new XdmAtomicValue(parameter.getValue()));
			}
		}

		result = xqueryEvaluator.evaluate();

		return result;
	}

}
