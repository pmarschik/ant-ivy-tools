package com.unicorntoast.ant.ivy.static_classpath;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;

public abstract class ClasspathUtils {
	
	private static final String TYPE_JAR = "jar";

	public static void store(String basepath, ResolveReport report, String output) throws IOException {
		
		OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(output)));
		try {
			
			for(ArtifactDownloadReport r : report.getAllArtifactsReports())  {
				Artifact a = r.getArtifact();
				
				if( TYPE_JAR.equals(a.getType()))
					out.append(path(basepath,r)).append('\n');
			}
			
		} finally {
			out.close();
		}
		
	}

	private static CharSequence path(String basepath, ArtifactDownloadReport r) {
		String path = r.getLocalFile().getAbsolutePath();
		if( path.startsWith(basepath) )
			path = path.substring(basepath.length());
		return path;
	}

	public static ArrayList<String> load(String input) throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
		try {
			String line;
			while( (line=in.readLine()) != null )
				result.add(line);
		} finally {
			in.close();
		}
		return result;
	}

}