package com.unicorntoast.ant.ivy.eclipse;

import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class GenerateClasspath extends Task {
	
	// state
	
	private String output;
	private ArrayList<StreamContributor> contributors = new ArrayList<StreamContributor>();
	
	// impl
	
	@Override
	public void execute() throws BuildException {
		
		try {
			
			EclipseUtils.createClasspath(
				output,
				contributors
			);
			
		} catch (IOException e) {
			throw new BuildException(e);
		}
		
	}
	
	// nested
	
	public IvyContributor createIvy() {
		IvyContributor ivyContributor = new IvyContributor();
		contributors.add(ivyContributor);
		return ivyContributor;
	}
	
	public class IvyContributor implements StreamContributor {
		
		String settings;
		String input;

		@Override
		public void write(PrintStream out) throws IOException {
			
			try {
				ClasspathUtils.build(
						getProject().getBaseDir().getAbsolutePath(),
						IvyUtils.resolve(
							IvyUtils.create(
								SettingsUtils.load(settings)
							),
							input
						)
					);
			} catch(ParseException e) {
				throw new RuntimeException(String.format("ivy: settings='%s' input='%s'",settings,input),e);
			}
			
		}
		
		public void setSettings(String settings) {
			this.settings = settings;
		}
		
		public void setInput(String input) {
			this.input = input;
		}
		
	}
	
	public CustomEntry createEntry() {
		CustomEntry customEntry = new CustomEntry();
		contributors.add(customEntry);
		return customEntry;
	}
	
	public class CustomEntry implements ClasspathEntry {
		
		String kind;
		String path;
		String output;

		@Override
		public void write(PrintStream out) throws IOException {
			out.append("<classpathentry");
			
			writeAttribute(out,"kind",kind);
			writeAttribute(out,"path",path);
			writeAttribute(out,"output",output);
			
			out.append("/>");
		}

		private void writeAttribute(PrintStream out, String name, String value) {
			if( value==null )
				return;
			out.append(" ").append(name).append("=\"").append(value).append("\"");
		}
		
		public void setKind(String kind) {
			this.kind = kind;
		}
		
		public void setPath(String path) {
			this.path = path;
		}
		
	}
	
	// stupido
	
	public void setOutput(String output) {
		this.output = output;
	}

}
