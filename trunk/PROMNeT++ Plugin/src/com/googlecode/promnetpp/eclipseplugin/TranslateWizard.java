package com.googlecode.promnetpp.eclipseplugin;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.swt.widgets.MessageBox;
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
		ProcessBuilder processBuilder = new ProcessBuilder("java",
				"-enableassertions",
				"-jar", "\"" + page.getJARFilePath() + "\"",
				"\"" + inputFilePath.toString() + "\"",
				"\"" + page.getConfigurationFilePath() + "\"");

		processBuilder.directory(project.getLocation().toFile());
		processBuilder.environment().put("PROMNETPP_HOME",
				page.getPROMNeTppHomeVariable());
		try {
			Process process = processBuilder.start();
			int returnCode = process.waitFor();
			if (returnCode != 0) {
				InputStream errorStream = process.getErrorStream();
				byte [] errorBytes = new byte[errorStream.available()];
				errorStream.read(errorBytes);
				String error = new String(errorBytes);
				MessageBox errorBox = new MessageBox(getShell(),
						SWT.OK | SWT.ICON_ERROR);
				errorBox.setMessage("An errror has occurred.\n\n" + error);
				errorBox.open();
			} else {
				MessageBox successBox = new MessageBox(getShell(), SWT.OK);
				successBox.setMessage("Translation complete.");
				successBox.open();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
					setLayout(new GridLayout(3, false));
					new Label(this, SWT.NULL).setText("PROMNETPP_HOME:");
					PROMNeTppHomeText = new Text(this, SWT.NULL);
					PROMNeTppHomeText.setText("C:/Users/Miguel Martins/" +
							"Documents/NetBeansProjects/promnetpp");
					Button chooseHomeButton = new Button(this, SWT.PUSH);
					chooseHomeButton.setText("...");
					chooseHomeButton.addSelectionListener(new
							SelectionListener() {

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
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					});
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

		public String getConfigurationFilePath() {
			if (useDefaultConfigurationButton.getSelection()) {
				return "default-configuration.xml";
			} else if (specifyConfigurationFileButton.getSelection()) {
				return configurationFileText.getText();
			} else {
				System.err.println("No radio button is selected!");
				return null;
			}
		}

		public String getJARFilePath() {
			return JARFileConfigurationText.getText();
		}
		
		public String getPROMNeTppHomeVariable() {
			return PROMNeTppHomeText.getText();
		}

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
	}
}
