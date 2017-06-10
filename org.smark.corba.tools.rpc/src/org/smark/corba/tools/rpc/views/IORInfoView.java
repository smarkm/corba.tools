package org.smark.corba.tools.rpc.views;

import java.io.IOException;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.smark.corba.tools.core.NamingSearch;
import org.smark.corba.tools.core.RegistEntity;

public class IORInfoView extends ViewPart {

	public static final String ID = "org.smark.corba.tools.rpc.views.IORInfoView"; //$NON-NLS-1$
	public static final String[] tableColumns = {"Name","IRO"};
	
	private Text txtHost;
	private Text txtPort;
	private Table table;
	private TableViewer tableViewer;
	private Text txtNameProp;
	private Text txtIORProp;

	public IORInfoView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setLocation(0, 0);
		
		Group group = new Group(sashForm, SWT.NONE);
		group.setLayout(new GridLayout(4, false));
		
		Label lblHost = new Label(group, SWT.NONE);
		lblHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost.setText("Host:");
		
		txtHost = new Text(group, SWT.BORDER);
		txtHost.setText("127.0.0.1");
		txtHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(group, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Port:");
		
		txtPort = new Text(group, SWT.BORDER);
		txtPort.setText("2050");
		txtPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		
		Button btnConnect = new Button(group, SWT.NONE);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnConnect.setText("Get");
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<RegistEntity> entities = NamingSearch.loadIORs(txtHost.getText(), txtPort.getText());
				tableViewer.setInput(entities);
				/*for (RegistEntity registEntity : entities) {
					System.out.println(registEntity);
				}*/
			}
		});
		
		Composite compContent = new Composite(sashForm, SWT.NONE);
		compContent.setLayout(new GridLayout(2, false));
		
		final String linkTxt = "http://www2.parc.com/istl/projects/ILU/parseIOR/";
		Link link = new Link(compContent, SWT.NONE);
		link.setText("<a>"+linkTxt+"</a>");
		new Label(compContent, SWT.NONE);
		link.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Runtime.getRuntime().exec("start "+linkTxt);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		tableViewer = new TableViewer(compContent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		createTabelColumns(tableViewer);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection) event.getSelection();
				RegistEntity entity = (RegistEntity) selection.getFirstElement();
				txtNameProp.setText(entity.getName());
				txtIORProp.setText(entity.getIor());
				
			}
		});
		
		Composite composite = new Composite(compContent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtNameProp = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtNameProp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblNewLabel.setText("IOR:");
		
		txtIORProp = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtIORProp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setLabelProvider(new RegistEntityLableProvider());
		tableViewer.setContentProvider(new RegistEntityContentProvider());
		
		sashForm.setWeights(new int[] {2, 8});

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	private void createTabelColumns(TableViewer tableViewer) {
		TableViewerColumn column = createColumnFor(tableViewer, tableColumns[0]);
		/*new ColumnViewerComparator(tableViewer, column) {

			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				RegistEntity r1 = (RegistEntity) e1;
				RegistEntity r2 = (RegistEntity) e2;
				return r1.getDeviceName().compareToIgnoreCase(r2.getDeviceName());
			}

		};*/
		column = createColumnFor(tableViewer, tableColumns[1]);
		/*new ColumnViewerComparator(tableViewer, column) {

			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				RegistEntity r1 = (RegistEntity) e1;
				RegistEntity r2 = (RegistEntity) e2;
				return r1.getDeviceIP().compareToIgnoreCase(r2.getDeviceIP());
			}

		};*/
	}

	private TableViewerColumn createColumnFor(TableViewer viewer, String label) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.setLabelProvider(new ColumnLabelProvider(){
			
			@Override
			public Color getBackground(Object element) {
				return new Color(Display.getDefault(),0x00 ,0x00,0xff);
			}	
		});
		TableColumn tableColumn = column.getColumn();
		tableColumn.setAlignment(SWT.CENTER);
		tableColumn.setWidth(200);
		tableColumn.setText(label);
		tableColumn.setMoveable(true);
		return column;
	}
	
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
	private class RegistEntityLableProvider extends LabelProvider implements ITableLabelProvider  {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof RegistEntity) {
				RegistEntity entity = (RegistEntity) element;
				switch (columnIndex) {
				case 0:
					return entity.getName();
				case 1:
					return entity.getIor();
				}
			}
			return "";
		}
	}
	private class RegistEntityContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[0];
		}

	}
}
