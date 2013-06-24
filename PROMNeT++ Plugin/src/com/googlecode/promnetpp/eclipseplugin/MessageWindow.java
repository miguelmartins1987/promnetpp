package com.googlecode.promnetpp.eclipseplugin;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MessageWindow extends ApplicationWindow {

	private Text outputText;
	private Text errorText;
	private Button closeButton;

	public MessageWindow(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
	}

	@Override
	protected Control createContents(Composite parent) {
		Shell shell = getShell();
		shell.setText("PROMNeT++ Eclipse Plugin");
		parent.setSize(640, 480);
		//Center the window
		Monitor monitor = Display.getDefault().getPrimaryMonitor();
		Rectangle monitorBounds = monitor.getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = monitorBounds.x + (monitorBounds.width - shellBounds.width) / 2;
		int y = monitorBounds.y + (monitorBounds.height - shellBounds.height) / 2;
		shell.setLocation(x, y);
		//Set the layout first
		RowLayout layout = new RowLayout();
		layout.type = SWT.VERTICAL;
		parent.setLayout(layout);
		//Set the controls now
		new Label(parent, SWT.NULL).setText("Output");
		outputText = new Text(parent, SWT.MULTI | SWT.V_SCROLL);
		outputText.setLayoutData(new RowData(600,190));
		outputText.setEditable(false);
		outputText.setBackground(shell.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		new Label(parent, SWT.NULL).setText("Error(s)");
		errorText = new Text(parent, SWT.MULTI | SWT.V_SCROLL);
		errorText.setLayoutData(new RowData(600,190));
		errorText.setEditable(false);
		errorText.setBackground(shell.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		//Change the font of errors to red
		errorText.setForeground(shell.getDisplay().getSystemColor(
				SWT.COLOR_RED));
		//Close button
		closeButton = new Button(parent, SWT.PUSH);
		closeButton.setText("Close");
		closeButton.setEnabled(false);
		closeButton.setLayoutData(new RowData(620, 24));
		closeButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionEvent) {
				getShell().dispose();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionEvent) {
			}
		});
		return parent;
	}

	public void appendOutputText(String text, boolean appendNewLine) {
		appendText(outputText, text, appendNewLine);
	}

	public void appendErrorText(String text, boolean appendNewLine) {
		appendText(errorText, text, appendNewLine);
	}

	private void appendText(Text textField, String text,
			boolean appendNewLine) {
		textField.setText(textField.getText() + text);
		if (appendNewLine) {
			textField.setText(textField.getText() + System.getProperty(
					"line.separator"));
		}
	}

	public void startProcess(ProcessBuilder processBuilder) {
		ProcessThread processThread = new ProcessThread();
		processThread.setProcessBuilder(processBuilder);
		processThread.start();
		while (!processThread.isRunningProcess()) {
			refresh();
			sleep(17);
		}
		while (processThread.isAlive()) {
			appendText(outputText, processThread.getStandardOutput(), false);
			appendText(errorText, processThread.getStandardError(), false);
			refresh();
			sleep(17);
		}
		try {
			processThread.join();
		} catch (InterruptedException e) {
			//Ignore
		}
		closeButton.setEnabled(true);
	}

	private void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			//Ignore
		}
	}

	public void refresh() {
		getShell().update();
	}
}
