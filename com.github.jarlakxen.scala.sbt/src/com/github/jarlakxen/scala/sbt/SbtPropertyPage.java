package com.github.jarlakxen.scala.sbt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import com.github.jarlakxen.scala.sbt.util.UIUtil;

/**
 * The property page to configure the SBT project.
 *
 * @author Naoki Takezoe
 */
public class SbtPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {

	private List<Button> sbtRadioButtons;

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		// sbt version
		UIUtil.createLabel(composite, "SBT Version:");
		Composite group = new Composite(composite, SWT.NULL);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		SbtProjectConfiguration config = new SbtProjectConfiguration(getProject());
		
		sbtRadioButtons = new ArrayList<Button>();
		for (SbtVersion version : SbtVersion.values()) {
			Button sbtRadioButton = new Button(group, SWT.RADIO);
			sbtRadioButton.setText("sbt " + version.getPrefix());
			sbtRadioButton.setData(version);
			
			if(config.getSbtVersion() == version){
				sbtRadioButton.setSelection(true);
			}
			
			sbtRadioButtons.add(sbtRadioButton);
		}

		return composite;
	}

	public IProject getProject(){
		IAdaptable adaptable = getElement();
		if(adaptable instanceof IProject){
			return (IProject) adaptable;
		} else if(adaptable instanceof IJavaProject){
			return ((IJavaProject) adaptable).getProject();
		}
		throw new IllegalStateException("Can't convert to IProject: " + adaptable.getClass());
	}

	@Override
	public boolean performOk() {
		SbtProjectConfiguration config = new SbtProjectConfiguration(getProject());
		
		for (Button radio : sbtRadioButtons) {
			if (radio.getSelection()) {
				config.setSbtVersion((SbtVersion) radio.getData());
			}
		}
		
		config.saveConfiguration();

		return true;
	}

	@Override
	protected void performDefaults() {
		sbtRadioButtons.get(sbtRadioButtons.size()-1).setSelection(true);
	}

}
