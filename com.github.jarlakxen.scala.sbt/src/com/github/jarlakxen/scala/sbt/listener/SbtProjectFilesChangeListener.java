package com.github.jarlakxen.scala.sbt.listener;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.github.jarlakxen.scala.sbt.SbtPlugin;
import com.github.jarlakxen.scala.sbt.jobs.UpdateProjectJob;
import com.github.jarlakxen.scala.sbt.util.UIUtil;

public class SbtProjectFilesChangeListener implements IResourceChangeListener {

	private static final List<String> SBT_PROJECT_FILES = asList("build.sbt", "project/plugins.sbt", "project/build.properties", "project/Build.scala");
	
	public static void register() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new SbtProjectFilesChangeListener(), IResourceChangeEvent.POST_CHANGE);
	}

	private SbtProjectFilesChangeListener() {
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		List<IProject> projects = getProjects(event.getDelta());
		
		for(IProject project : projects){
			new UpdateProjectJob(project).schedule();
		}
	}

	private List<IProject> getProjects(IResourceDelta delta) {
		final List<IProject> projects = new ArrayList<IProject>();
		try {
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					// only interested in changed resources (not added or
					// removed)
					if (delta.getKind() != IResourceDelta.CHANGED) {
						return true;
					}
					// only interested in content changes
					if ((delta.getFlags() & IResourceDelta.CONTENT) == 0) {
						return true;
					}

					IResource resource = delta.getResource();
					if (resource.getType() == IResource.FILE && isProjectFile(resource) && !projects.contains(resource.getProject())) {
						projects.add(resource.getProject());
					}
					return true;

				}
			});
		} catch (CoreException ex) {
			UIUtil.showErrorDialog(ex.toString());
			SbtPlugin.logException(ex);
		}
		return projects;
	}
	
	private boolean isProjectFile(IResource resource){
		String resourcePath =  resource.getProjectRelativePath().toString();
		for(String projectFile : SBT_PROJECT_FILES){
			if(projectFile.equals(resourcePath)){
				return true;
			}
		}
		return false;
	}

}
