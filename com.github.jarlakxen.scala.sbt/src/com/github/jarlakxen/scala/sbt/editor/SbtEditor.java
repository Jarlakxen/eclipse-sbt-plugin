package com.github.jarlakxen.scala.sbt.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import scala.tools.eclipse.InteractiveCompilationUnit;
import scala.tools.eclipse.ScalaEditor;
import scala.tools.eclipse.ScalaPlugin;
import scala.tools.eclipse.ScalaSourceViewerConfiguration;
import scala.tools.eclipse.lexical.ScalaDocumentPartitioner;

/**
 * The text editor for *.sbt files.
 * 
 * @author Naoki Takezoe
 */
public class SbtEditor extends CompilationUnitEditor implements ScalaEditor {

	private IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			handlePreferenceStoreChanged(event);
		}
	};

	private SbtOutlinePage outlinePage;

	public SbtEditor() {
		ScalaPlugin.plugin().getPreferenceStore().addPropertyChangeListener(preferenceListener);
	}

	@Override
	public void dispose() {
		super.dispose();
		ScalaPlugin.plugin().getPreferenceStore().removePropertyChangeListener(preferenceListener);
	}

	@Override
	protected void setSourceViewerConfiguration(SourceViewerConfiguration configuration) {
		if (configuration instanceof ScalaSourceViewerConfiguration) {
			super.setSourceViewerConfiguration(configuration);
		} else {
			setSourceViewerConfiguration(new ScalaSourceViewerConfiguration(getPreferenceStore(),
					ScalaPlugin.plugin().getPreferenceStore(), this) {
				@Override
				public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
					return null;
				}
			});
		}
	}

	@Override
	public ScalaDocumentPartitioner createDocumentPartitioner() {
		return new ScalaDocumentPartitioner(false);
	}

	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);
		update();
	}

	public void doSaveAs() {
		super.doSaveAs();
		update();
	}

	private void update() {
		if (outlinePage != null) {
			outlinePage.update();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new SbtOutlinePage(this);
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

	@Override
	public InteractiveCompilationUnit getInteractiveCompilationUnit() {
		return (InteractiveCompilationUnit) getInputJavaElement();
	}

}
