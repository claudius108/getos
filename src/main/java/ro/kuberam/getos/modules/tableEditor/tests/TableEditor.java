package ro.kuberam.getos.modules.tableEditor.tests;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.extractors.BasicExtractionAlgorithm;

public class TableEditor extends Application {

	private TableView<ObservableList<StringProperty>> table = new TableView<>();
	final ObservableList<ObservableList<StringProperty>> data = FXCollections.observableArrayList();
	private char nextChar = 'A';

	@Override
	public void start(Stage stage) {
		final BorderPane root = new BorderPane();
		table = new TableView<>();
		table.setEditable(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		populateTable("file:///home/claudius/comune.txt");

		root.setCenter(table);
		Scene scene = new Scene(root, 1000, 700);
		scene.getStylesheets().add("/ro/kuberam/getos/modules/tableEditor/TableEditor.css");
		stage.setScene(scene);
		stage.show();
	}

	private void populateTable(final String urlSpec) {
		table.getColumns().clear();
		table.setPlaceholder(new Label("Loading..."));

		technology.tabula.Table tabulaTable = getTabulaTable(new File("/home/claudius/comune.pdf"), 7);

		createIndexColumn();

		int columnNumber = tabulaTable.getCols().size();

		for (int i = 0; i < columnNumber; i++) {
			createDataColumn(i + 1);
		}

		for (List<RectangularTextContainer> row : tabulaTable.getRows()) {
			ObservableList<StringProperty> rowData = FXCollections.observableArrayList(row.stream()
					.map(columnValue -> new SimpleStringProperty(columnValue.getText())).collect(Collectors.toList()));

			data.addAll(rowData);
		}
		
		table.setItems(data);
		
		System.out.println(data);

		// Task<Void> task = new Task<Void>() {
		// @Override
		// protected Void call() throws Exception {
		// technology.tabula.Table tabulaTable = getTabulaTable(new
		// File("/home/claudius/comune.pdf"), 7);
		//
		// Platform.runLater(() -> {
		// try {
		// createIndexColumn();
		//
		// int columnNumber = tabulaTable.getCols().size();
		//
		// for (int i = 0; i < columnNumber; i++) {
		// createDataColumn(i + 1);
		// }
		//
		// for (List<RectangularTextContainer> row : tabulaTable.getRows()) {
		// ObservableList<StringProperty> rowData = FXCollections.observableArrayList(
		// row.stream().map(columnValue -> new
		// SimpleStringProperty(columnValue.getText()))
		// .collect(Collectors.toList()));
		//
		// table.getItems().add(rowData);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// });
		//
		// return null;
		// }
		// };
		// Thread thread = new Thread(task);
		// thread.setDaemon(true);
		// thread.start();
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
					deleteColumnItem.setOnAction(e -> table.getItems().remove(getIndex()));
					contextMenu.getItems().add(deleteColumnItem);
					setContextMenu(contextMenu);

					setOnMouseClicked(event -> {
						table.getSelectionModel().clearSelection();
						table.getSelectionModel().select(getIndex());

						if (event.getButton() == MouseButton.SECONDARY) {
							contextMenu.show(table, event.getScreenX(), event.getScreenY());
						}

						event.consume();
					});
				}
			};
		});
		indexColumn.setSortable(false);
		indexColumn.setMinWidth(50.0d);
		indexColumn.setMaxWidth(50.0d);

		table.getColumns().add(indexColumn);
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

		table.getColumns().add(column);
	}

	private void setColumnHeader(int columnIndex, TableColumn<ObservableList<StringProperty>, String> column) {
		String mapChar = String.valueOf(nextChar++);

		Label columnHeader = new Label(mapChar);
		columnHeader.setPrefWidth(Double.MAX_VALUE);

		ContextMenu contextMenu = new ContextMenu();
		MenuItem deleteColumnItem = new MenuItem("Remove column");
		deleteColumnItem.setOnAction(e -> table.getColumns().remove(column));
		contextMenu.getItems().add(deleteColumnItem);
		columnHeader.setContextMenu(contextMenu);

		columnHeader.setOnMouseClicked(event -> {
			table.getSelectionModel().clearSelection();
			table.getSelectionModel().selectRange(0, column, table.getItems().size() - 1, column);

			if (event.getButton() == MouseButton.SECONDARY) {
				contextMenu.show(table, event.getScreenX(), event.getScreenY());
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

	public static void main(String[] args) {
		launch(args);
	}

}