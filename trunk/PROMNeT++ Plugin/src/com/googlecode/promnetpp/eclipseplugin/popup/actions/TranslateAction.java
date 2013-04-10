package com.googlecode.promnetpp.eclipseplugin.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.googlecode.promnetpp.eclipseplugin.TranslateWizard;

public class TranslateAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection selection;
	
	/**
	 * Constructor for Action1.
	 */
	public TranslateAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			IFile inputFile = (IFile) ((IStructuredSelection)selection)
					.getFirstElement();
			IPath pathToInputFile = inputFile.getLocation();
			TranslateWizard wizard = new TranslateWizard();
			wizard.setPathToInputFile(pathToInputFile);
			wizard.setProject(inputFile.getProject());
			WizardDialog dialog = new WizardDialog(shell, wizard);
			int wizardReturnCode = dialog.open();
			if (wizardReturnCode == Window.OK) {
				try {
					inputFile.getProject().refreshLocal(IResource.DEPTH_ONE,
							null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
