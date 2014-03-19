package com.sap.dirigible.ide.workspace.wizard.project.sample;

import com.sap.dirigible.ide.workspace.wizard.project.create.ProjectTemplateType;

public abstract class SamplesModel {
	private SamplesCategory parent;
	private ProjectTemplateType template;

	protected void setParent(SamplesCategory parent) {
		this.parent = parent;
	}

	public SamplesCategory getParent() {
		return parent;
	}

	public SamplesModel() {

	}

	public SamplesModel(ProjectTemplateType template) {
		this.template = template;
	}

	public ProjectTemplateType getTemplate() {
		return template;
	}

	public void setTemplate(ProjectTemplateType template) {
		this.template = template;
	}
}
