package com.github.jarlakxen.scala.sbt.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.github.jarlakxen.scala.sbt.SbtParser;
import com.github.jarlakxen.scala.sbt.SbtParser.SbtSetting;
import com.github.jarlakxen.scala.sbt.SbtPlugin;

public class SbtOutlinePage extends ContentOutlinePage {

	private SbtEditor editor;
	private RootModel root = new RootModel();

	public SbtOutlinePage(SbtEditor editor) {
		this.editor = editor;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new SbtContentProvider());
		viewer.setLabelProvider(new SbtLabelProvider());
		viewer.setInput(root);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object obj = selection.getFirstElement();
				if (obj instanceof SbtSetting) {
					try {
						int line = ((SbtSetting) obj).line;
						int offset = editor.getDocumentProvider().getDocument(editor.getEditorInput()).getLineOffset(line);
						editor.getSelectionProvider().setSelection(new TextSelection(offset, 0));

					} catch (BadLocationException ex) {
						SbtPlugin.logException(ex);
					}
				}
			}
		});

		update();
	}

	public void update() {
		if (getControl() == null || getControl().isDisposed()) {
			return;
		}
		String source = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		List<SbtSetting> settings = SbtParser.parse(source);
		root.settings.clear();
		root.settings.addAll(settings);

		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			viewer.refresh();
		}

		try {
			viewer.expandAll();
		} catch (Exception ex) {
			// ignore
		}
	}

	private static class RootModel {
		public List<SbtSetting> settings = new ArrayList<SbtSetting>();
	}

	private class SbtContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof RootModel) {
				return root.settings.toArray();
			} else if (parentElement instanceof SbtSetting) {
				SbtSetting setting = (SbtSetting) parentElement;
				if (setting.values != null) {
					return setting.values;
				}
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof RootModel) {
				return true;
			} else if (element instanceof SbtSetting) {
				if (((SbtSetting) element).values != null) {
					return true;
				}
			}
			return false;
		}

	}

	private class SbtLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element instanceof SbtSetting) {
				return SbtPlugin.getDefault().getImageRegistry().get(SbtPlugin.ICON_SETTING_KEY);
			} else if (element instanceof String) {
				return SbtPlugin.getDefault().getImageRegistry().get(SbtPlugin.ICON_SETTING_VALUE);
			}
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof SbtSetting) {
				SbtSetting setting = (SbtSetting) element;
				if (setting.values != null || setting.value == null) {
					return setting.key;
				} else {
					return setting.key + ": " + setting.value;
				}
			}
			return super.getText(element);
		}

	}

}
