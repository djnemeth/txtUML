package hu.elte.txtuml.project.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import hu.elte.txtuml.project.wizards.pages.NewXtxtUMLFileCreationPage;

public class XtxtUMLFileCreatorWizard extends Wizard implements INewWizard {

	public static final String TITLE = "XtxtUML File";
	public static final String DESCRIPTION = "Create new XtxtUML file";
	
	private IWorkbench workbench;
	private IStructuredSelection selection;
	private NewXtxtUMLFileCreationPage page;

	public XtxtUMLFileCreatorWizard() {
		setWindowTitle(TITLE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		if (!page.isPageComplete()) {
			return false;
		}
		
		IFile file = page.createNewFile();
		boolean result = file != null;

		if (result) {
			try {
				IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public void addPages() {
		super.addPages();

		page = new NewXtxtUMLFileCreationPage();
		page.init(selection);

		addPage(page);
	}

}
