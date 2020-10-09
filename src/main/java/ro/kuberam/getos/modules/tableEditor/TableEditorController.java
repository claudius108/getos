package ro.kuberam.getos.modules.tableEditor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jdom2.Element;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import ro.kuberam.getos.DocumentModel;
import ro.kuberam.getos.controller.factory.RendererController;
import ro.kuberam.getos.modules.editorTab.EditorEvent;
import ro.kuberam.getos.modules.editorTab.EditorModel;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.extractors.BasicExtractionAlgorithm;

public final class TableEditorController extends RendererController {

	@FXML
	private ComboBox<String> selectEditorCombobox;

	@FXML
	private Button extractTablesButton;

	@FXML
	private ScrollPane centerSourcePane;

	@FXML
	private TableView<ObservableList<StringProperty>> contentSourcePane;

	private char nextChar = 'A';

	public TableEditorController(Application application, Stage stage, DocumentModel documentModel,
			EditorModel editorModel) {
		super(application, stage, documentModel, editorModel);
	}

	@FXML
	public void initialize() {
		editorModel.eventBus.addEventHandler(EditorEvent.GO_TO_PAGE, event -> {
			// contentSourcePane.setImage(getSourceDocumentModel().goToPage((int)
			// event.getData()));

			event.consume();
		});

		contentSourcePane.setEditable(true);
		contentSourcePane.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		contentSourcePane.getSelectionModel().setCellSelectionEnabled(true);
		contentSourcePane.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		int pageNumber = getSourceDocumentModel().currentPage();
		List<Element> tableElements = (List<Element>) getSourceDocumentModel().extractTablesFromPage(pageNumber);

		for (Element tableElement : tableElements) {
			List<Element> rows = tableElement.getChild("tbody").getChildren();
			int columnsNumber = rows.get(0).getChildren().size();

			contentSourcePane.getColumns().clear();
			contentSourcePane.setPlaceholder(new Label("Loading..."));

			createIndexColumn();

			for (int i = 0; i < columnsNumber; i++) {
				createDataColumn(i + 1);
			}

			for (Element row : rows) {
				List<Element> columns = row.getChildren();

				ObservableList<StringProperty> rowData = FXCollections.observableArrayList(columns.stream()
						.map(column -> new SimpleStringProperty(column.getText())).collect(Collectors.toList()));

				contentSourcePane.getItems().add(rowData);
			}
		}

		editorModel.eventBus.fireEvent("document-opened", getSourceDocumentModel().path());
	}

	private void createIndexColumn() {
		TableColumn<ObservableList<StringProperty>, String> indexColumn = new TableColumn<>();

		indexColumn.setCellFactory(column -> {
			return new TableCell<ObservableList<StringProperty>, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem((String) item, empty);
					setGraphic(null);
					setText(empty ? null : Integer.toString(getIndex() + 1));
					getStyleClass().add("indexColumnCell");

					ContextMenu contextMenu = new ContextMenu();
					MenuItem deleteColumnItem = new MenuItem("Remove row");
					deleteColumnItem.setOnAction(e -> contentSourcePane.getItems().remove(getIndex()));
					contextMenu.getItems().add(deleteColumnItem);
					setContextMenu(contextMenu);

					setOnMouseClicked(event -> {
						contentSourcePane.getSelectionModel().clearSelection();
						contentSourcePane.getSelectionModel().select(getIndex());

						if (event.getButton() == MouseButton.SECONDARY) {
							contextMenu.show(contentSourcePane, event.getScreenX(), event.getScreenY());
						}

						event.consume();
					});
				}
			};
		});
		indexColumn.setSortable(false);
		indexColumn.setMinWidth(50.0d);
		indexColumn.setMaxWidth(50.0d);

		contentSourcePane.getColumns().add(indexColumn);
	}

	private void createDataColumn(final int columnIndex) {
		final TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();

		column.setCellValueFactory(cellData -> {
			ObservableList<StringProperty> values = cellData.getValue();
			if (columnIndex >= values.size()) {
				return new SimpleStringProperty("");
			} else {
				return cellData.getValue().get(columnIndex);
			}
		});
		column.setCellFactory(TextFieldTableCell.forTableColumn());
		column.setOnEditCommit(cellEditEvent -> {
			int editedRowIndex = cellEditEvent.getTablePosition().getRow();
			int editedColumnIndex = cellEditEvent.getTablePosition().getColumn();

			cellEditEvent.getTableView().getItems().get(editedRowIndex).set(editedColumnIndex,
					new SimpleStringProperty(cellEditEvent.getNewValue()));
		});
		column.setSortable(false);
		setColumnHeader(columnIndex, column);

		contentSourcePane.getColumns().add(column);
	}

	private void setColumnHeader(int columnIndex, TableColumn<ObservableList<StringProperty>, String> column) {
		String mapChar = String.valueOf(nextChar++);

		Label columnHeader = new Label(mapChar);
		columnHeader.setPrefWidth(Double.MAX_VALUE);

		ContextMenu contextMenu = new ContextMenu();
		MenuItem deleteColumnItem = new MenuItem("Remove column");
		deleteColumnItem.setOnAction(e -> contentSourcePane.getColumns().remove(column));
		contextMenu.getItems().add(deleteColumnItem);
		columnHeader.setContextMenu(contextMenu);

		columnHeader.setOnMouseClicked(event -> {
			contentSourcePane.getSelectionModel().clearSelection();
			contentSourcePane.getSelectionModel().selectRange(0, column, contentSourcePane.getItems().size() - 1,
					column);

			if (event.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(contentSourcePane, event.getScreenX(), event.getScreenY());
			}

			event.consume();
		});

		column.setGraphic(columnHeader);
	}

	private technology.tabula.Table getTabulaTable(File file, int pageNumber) {
		ObjectExtractor oe = null;
		technology.tabula.Table tabulaTable = null;

		try {
			PDDocument document = PDDocument.load(file);
			oe = new ObjectExtractor(document);
			Page page = oe.extract(pageNumber);
			oe.close();

			BasicExtractionAlgorithm bea = new BasicExtractionAlgorithm();
			tabulaTable = bea.extract(page).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tabulaTable;
	}
}
