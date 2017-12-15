package ro.kuberam.getos.modules.tableEditor.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.writers.CSVWriter;

public class DynamicTable extends Application {

	private TableView<ObservableList<StringProperty>> table = new TableView<>();
	private ContextMenu columnContextMenu;

	private char nextChar = 'A';

	@Override
	public void start(Stage stage) {
		final BorderPane root = new BorderPane();
		table = new TableView<>();
		table.setEditable(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setFixedCellSize(Region.USE_COMPUTED_SIZE);

		columnContextMenu = new ContextMenu();
		MenuItem mi1 = new MenuItem("Delete column");
		columnContextMenu.getItems().add(mi1);

		// tableView.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {
		// @Override
		// public TableRow<Person> call(TableView<Person> tableView2) {
		// final TableRow<Person> row = new TableRow<>();
		// row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent event) {
		// final int index = row.getIndex();
		// if (index >= 0 && index < tableView.getItems().size() &&
		// tableView.getSelectionModel().isSelected(index) ) {
		// tableView.getSelectionModel().clearSelection();
		// event.consume();
		// }
		// }
		// });
		// return row;
		// }
		// });

		// ObjectProperty<TableRow<MyRowClass>> lastSelectedRow = new
		// SimpleObjectProperty<>();
		//
		// myTableView.setRowFactory(tableView -> {
		// TableRow<MyRowClass> row = new TableRow<MyRowClass>();
		//
		// row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
		// if (isNowSelected) {
		// lastSelectedRow.set(row);
		// }
		// });
		// return row;
		// });
		//
		//
		// stage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, new
		// EventHandler<MouseEvent>() {
		//
		// @Override
		// public void handle(MouseEvent event) {
		// if (lastSelectedRow.get() != null) {
		// Bounds boundsOfSelectedRow =
		// lastSelectedRow.get().localToScene(lastSelectedRow.get().getLayoutBounds());
		// if (boundsOfSelectedRow.contains(event.getSceneX(), event.getSceneY()) ==
		// false) {
		// myTableView.getSelectionModel().clearSelection();
		// }
		// }
		// }
		// });

		populateTable("file:///home/claudius/comune.txt");

		root.setCenter(table);
		Scene scene = new Scene(root, 1000, 700);
		stage.setScene(scene);
		stage.show();
	}

	private void populateTable(final String urlSpec) {
		table.getColumns().clear();
		table.setPlaceholder(new Label("Loading..."));
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				technology.tabula.Table tabulaTable = getTabulaTable(new File("/home/claudius/comune.pdf"), 7);

				Platform.runLater(() -> {
					try {
						createIndexColumn();

						int columnNumber = tabulaTable.getCols().size();

						for (int i = 0; i < columnNumber; i++) {
							createDataColumn(i + 1);
						}

						for (List<RectangularTextContainer> row : tabulaTable.getRows()) {
							ObservableList<StringProperty> rowData = FXCollections.observableArrayList(
									row.stream().map(columnValue -> new SimpleStringProperty(columnValue.getText()))
											.collect(Collectors.toList()));

							table.getItems().add(rowData);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});

				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
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
		        }
		    };
		});
		indexColumn.setSortable(false);

		table.getColumns().add(indexColumn);
	}

	private void createDataColumn(final int columnIndex) {
		TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();

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
		column = setColumnHeader(columnIndex, column);

		table.getColumns().add(column);
	}

	private TableColumn<ObservableList<StringProperty>, String> setColumnHeader(int columnIndex,
			TableColumn<ObservableList<StringProperty>, String> column) {
		String mapChar = String.valueOf(nextChar++);

		Label columnHeader = new Label(mapChar);
		columnHeader.setPrefWidth(Double.MAX_VALUE);
		columnHeader.setId(Integer.toString(columnIndex));

		columnHeader.setOnMouseClicked(event -> {
			String columnHeaderLabelId = ((Label) event.getSource()).getId();
			TableColumn<ObservableList<StringProperty>, String> currentColumn = (TableColumn<ObservableList<StringProperty>, String>) table
					.getColumns().get(Integer.parseInt(columnHeaderLabelId));
			table.getSelectionModel().clearSelection();
			table.getSelectionModel().selectRange(0, currentColumn, table.getItems().size() - 1, currentColumn);

			if (event.getButton() == MouseButton.SECONDARY) {
				columnContextMenu.show(table, event.getScreenX(), event.getScreenY());
			}

			event.consume();
		});

		// column.setCellValueFactory(new MapValueFactory(mapChar));
		column.setGraphic(columnHeader);

		return column;
	}

	private BufferedReader getReaderFromUrl(String urlSpec) throws Exception {
		URL url = new URL(urlSpec);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		return new BufferedReader(new InputStreamReader(in));
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

			StringBuilder sb = new StringBuilder();
			(new CSVWriter()).write(sb, tabulaTable);
			String s = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tabulaTable;
	}

	public static void main(String[] args) {
		launch(args);
	}

}