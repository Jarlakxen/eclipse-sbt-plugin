package com.github.jarlakxen.scala.sbt.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * Provides utility methods for building user interface.
 * 
 * @author Facundo Viale
 */
public class UIUtil {

	/**
	 * Creates the Label with the given text.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param text
	 *            the label text
	 */
	public static Label createLabel(Composite parent, String text) {
		return createLabel(parent, text, null);
	}

	/**
	 * Creates the Label with the given text.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param text
	 *            the label text
	 */
	public static Label createLabel(Composite parent, String text, GridData gridData) {
		Label label = new Label(parent, SWT.NULL);
		label.setText(text);
		if (gridData != null) {
			label.setLayoutData(gridData);
		}
		label.setAlignment(SWT.VERTICAL + SWT.CENTER);
		return label;
	}

	/**
	 * Shows the error dialog with the given message.
	 * 
	 * @param message
	 *            the error message to show in the dialog.
	 */
	public static void showErrorDialog(final String message) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", message);
			}
		});
	}

	public static GridData createGridData(int option, int colspan) {
		GridData gd = new GridData(option);
		gd.horizontalSpan = colspan;
		return gd;
	}

	public static GridData createGridDataWithWidth(int width) {
		GridData gd = new GridData();
		gd.minimumWidth = width;
		gd.widthHint = width;
		return gd;
	}
}
