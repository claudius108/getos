package ro.kuberam.getos.modules.incrementalDownload;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class BatchDownload {

	public static void main(String[] args) {
		String host = "http://virtual1.bibnat.ro";
		String basePath = "Harti";
		String resourceId = "H-III-113";
		String partId = "H-III-113_";
		String partExtension = ".jpg";
		int numberOfParts = 234;
		Path targetPath = Paths.get("/home/claudius/Downloads/nume/Dicţionarul geografic al judeţului Vasluiŭ dicţionar geografic statistic şi economic cu notiţe istorice");

		try {
			List<URL> urls = new ArrayList<URL>();

			for (int i = 1; i <= numberOfParts; i++) {
				String resourceUri = String.join("/", host, basePath, resourceId,
						partId + String.format("%04d", i) + partExtension);
				urls.add(new URI(resourceUri).toURL());
			}

			run(urls, targetPath);
			
		} catch (URISyntaxException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void run(List<URL> urls, Path targetPath) {
		for (URL url : urls) {
			try (InputStream in = url.openStream()) {
				Files.copy(in, targetPath.resolve(Paths.get(url.getFile()).getFileName()), StandardCopyOption.REPLACE_EXISTING);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
