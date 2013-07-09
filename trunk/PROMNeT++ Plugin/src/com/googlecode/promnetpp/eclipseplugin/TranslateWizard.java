/*
 * Copyright (c) 2013, Miguel Martins
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
package com.googlecode.promnetpp.eclipseplugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TranslateWizard extends Wizard {

	private IPath inputFilePath;
	private IProject project;
	private TranslateWizardPage page;

	public TranslateWizard() {
		setWindowTitle("PROMNeT++");
		page = new TranslateWizardPage("");
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		Shell messageWindowShell = new Shell();
		MessageWindow messageWindow = new MessageWindow(messageWindowShell);
		messageWindow.open();
		messageWindow.appendOutputText("Building PROMNeT++'s process...", true);
		
		List<String> command = new ArrayList<String>();
		//java -enableassertions -jar <path_to_jar>.jar <input file>
		command.add("java");
		command.add("-enableassertions");
		command.add("-jar");
		command.add("\"" + page.getJARFilePath() + "\"");
		command.add("\"" + inputFilePath.toString() + "\"");
		
		/*
		 * We need to add an extra command-line argument if the user chose
		 * to specify their own configuration file.
		 */
		if (!page.isUsingDefaultConfigurationFile()) {
			command.add("\"" + page.getConfigurationFilePath() + "\"");
		}
		
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(project.getLocation().toFile());
		processBuilder.environment().put("PROMNETPP_HOME",
				page.getPROMNeTppHomeVariable());
		
		messageWindow.appendOutputText("Starting PROMNeT++...", true);
		messageWindow.appendOutputText(command.toString(), true);
		messageWindow.refresh();
		messageWindow.startProcess(processBuilder);
		return true;
	}

	public void setPathToInputFile(IPath inputFilePath) {
		this.inputFilePath = inputFilePath;
	}

	public void setProject(IProject project) {
		this.project = project;	
	}

	private class TranslateWizardPage extends WizardPage {

		private Composite container;
		private Button specifyConfigurationFileButton;
		private Button useDefaultConfigurationButton;
		private Button sameAsJarButton;
		private Button otherLocationButton;
		private Text configurationFileText;
		private Text JARFileConfigurationText;
		private Text PROMNeTppHomeText;

		protected TranslateWizardPage(String pageName) {
			super(pageName);
			setTitle("Translate to C++ code");
		}

		@Override
		public void createControl(Composite parent) {
			final String inputFilePathAsString = inputFilePath.toString();
			container = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout(1, false);
			container.setLayout(layout);
			//Let the user specify the location to promnetpp.jar
			new Composite(container, SWT.NULL) {
				{
					setLayout(new GridLayout(3, false));
					new Label(this, SWT.NULL).setText("promnetpp.jar location:"
							+ "");
					JARFileConfigurationText = new Text(this, SWT.NULL);
					JARFileConfigurationText.setText(
							"C:/Users/Miguel Martins/Documents/NetBeansProjects"
									+ "/promnetpp/dist/promnetpp.jar");
					Button browseJARButton = new Button(this, SWT.PUSH);
					browseJARButton.setText("...");
					browseJARButton.addSelectionListener(new
							SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							FileDialog dialog = new FileDialog(getShell(),
									SWT.OPEN);
							dialog.setFilterExtensions(new String [] {"*.jar"});
							String result = dialog.open();
							if (result != null) {
								JARFileConfigurationText.setText(result);
							}
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					});
				}
			};
			//PROMNETPP_HOME environment variable
			new Composite(container, SWT.NULL) {
				{
					setLayout(new GridLayout(1, false));
					new Label(this, SWT.NULL).setText("PROMNETPP_HOME");
					new PROMNeTppHomeComposite(this, SWT.NULL);
				}
			};
			//Show the user the input file they selected
			new Composite(container, SWT.NULL) {
				{
					setLayout(new GridLayout(2, false));
					new Label(this, SWT.NULL).setText("Input file:");
					new Label(this, SWT.NULL).setText(inputFilePathAsString);
				}
			};

			//Allow them to specify the configuration file (XML)
			new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(
					new GridData(GridData.FILL_HORIZONTAL));
			new Label(container, SWT.NULL).setText("Configuration file");
			new ConfigurationComposite(container, SWT.NULL);
			setControl(container);
		}


		public boolean isUsingDefaultConfigurationFile() {
			if (useDefaultConfigurationButton.getSelection()) {
				return true;
			} else if (specifyConfigurationFileButton.getSelection()) {
				return false;
			}
			throw new RuntimeException("No valid configuration radio button" +
					" selected.");
		}
		
		public String getConfigurationFilePath() {
			return configurationFileText.getText();
		}

		public String getJARFilePath() {
			return JARFileConfigurationText.getText();
		}

		public String getPROMNeTppHomeVariable() {
			if (sameAsJarButton.getSelection()) {
				IPath path = new Path(JARFileConfigurationText.getText());
				path = path.removeLastSegments(1);
				return path.toPortableString();
			} else if (otherLocationButton.getSelection()) {
				return PROMNeTppHomeText.getText();
			} else {
				throw new RuntimeException("No radio button selected!");
			}
		}

		//Custom composites
		private class ConfigurationComposite extends Composite {

			public ConfigurationComposite(Composite parent, int style) {
				super(parent, style);
				setLayout(new GridLayout(2, false));
				//Use default configuration
				useDefaultConfigurationButton = new Button(this, SWT.RADIO);
				new Label(this, SWT.NULL).setText("Use default configuration" +
						" file (\"default-configuration.xml\")");
				specifyConfigurationFileButton = new Button(this, SWT.RADIO);
				//Manual specification
				new Composite(this, SWT.NULL) {
					{
						setLayout(new GridLayout(2, false));
						new Label(this, SWT.NULL).setText("Specify manually:");
						configurationFileText = new Text(this, SWT.SINGLE);
						configurationFileText.setText("my-configuration-"
								+ "file.xml");
					}
				};
				useDefaultConfigurationButton.setSelection(true);
			}

		}

		private class PROMNeTppHomeComposite extends Composite {

			public PROMNeTppHomeComposite(Composite parent, int style) {
				super(parent, style);
				setLayout(new GridLayout(4, false));
				sameAsJarButton = new Button(this, SWT.RADIO);
				new Label(this, SWT.NULL).setText("Same as the location of" +
						" the JAR file.");
				new Label(this, SWT.NULL);
				new Label(this, SWT.NULL);
				otherLocationButton = new Button(this, SWT.RADIO);
				new Label(this, SWT.NULL).setText("Other (please specify):");
				PROMNeTppHomeText = new Text(this, SWT.NULL);
				PROMNeTppHomeText.setText("C:/Users/Miguel Martins/" +
						"Documents/NetBeansProjects/promnetpp");
				Button chooseHomeButton = new Button(this, SWT.PUSH);
				chooseHomeButton.setText("...");
				chooseHomeButton.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						DirectoryDialog dialog = new DirectoryDialog(
								getShell(), SWT.OPEN);
						String result = dialog.open();
						if (result != null) {
							PROMNeTppHomeText.setText(result);
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e)
					{
					}
				});
				sameAsJarButton.setSelection(true);
			}
		};
	}
}
