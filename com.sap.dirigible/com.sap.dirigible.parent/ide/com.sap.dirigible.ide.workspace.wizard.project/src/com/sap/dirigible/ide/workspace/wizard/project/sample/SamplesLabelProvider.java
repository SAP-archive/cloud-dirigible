package com.sap.dirigible.ide.workspace.wizard.project.sample;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sap.dirigible.ide.common.image.ImageUtils;

public class SamplesLabelProvider extends LabelProvider {
	private static final long serialVersionUID = 6752428028085740772L;

	private static final Image SAMPLE_ICON = ImageUtils
			.createImage(SampleProjectWizardGitTemplatePage.getIconURL("icon-sample.png"));

	@Override
	public String getText(Object element) {
		String text = null;
		if (element instanceof SamplesCategory) {
			text = ((SamplesCategory) element).getName();
		} else if (element instanceof SamplesProject) {
			text = ((SamplesProject) element).getTemplate().getDescription();
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {
		Image image = null;
		if (element instanceof SamplesCategory) {
			image = SAMPLE_ICON;
		} else if (element instanceof SamplesProject) {
			image = ((SamplesProject) element).getTemplate().getImage();
		}
		return image;
	}
}
