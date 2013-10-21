package com.github.jarlakxen.scala.sbt.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

/**
 * Provides utility methods for building user interface.
 *
 * @author Naoki Takezoe
 */
public class UIUtil {

	/**
	 * Creates the Label with the given text.
	 *
	 * @param parent the parent composite
	 * @param text the label text
	 */
	public static void createLabel(Composite parent, String text){
		Label label = new Label(parent, SWT.NULL);
		label.setText(text);
		label.setAlignment(SWT.VERTICAL + SWT.CENTER);
	}

	/**
	 * Shows the error dialog with the given message.
	 *
	 * @param message the error message to show in the dialog.
	 */
	public static void showErrorDialog(String message){
		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Error", message);
	}

	public static GridData createGridData(int option, int colspan){
		GridData gd = new GridData(option);
		gd.horizontalSpan = colspan;
		return gd;
	}

	public static GridData createGridDataWithWidth(int width){
		GridData gd = new GridData();
		gd.minimumWidth = width;
		gd.widthHint = width;
		return gd;
	}
}
