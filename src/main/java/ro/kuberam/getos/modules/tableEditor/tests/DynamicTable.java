package ro.kuberam.getos.modules.tableEditor.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class DynamicTable extends Application {

	TableView<ObservableList<StringProperty>> table = new TableView<>();
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
		
//		tableView.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {  
//	        @Override  
//	        public TableRow<Person> call(TableView<Person> tableView2) {  
//	            final TableRow<Person> row = new TableRow<>();  
//	            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
//	                @Override  
//	                public void handle(MouseEvent event) {  
//	                    final int index = row.getIndex();  
//	                    if (index >= 0 && index < tableView.getItems().size() && tableView.getSelectionModel().isSelected(index)  ) {
//	                        tableView.getSelectionModel().clearSelection();
//	                        event.consume();  
//	                    }  
//	                }  
//	            });  
//	            return row;  
//	        }  
//	    });
		
		
//		ObjectProperty<TableRow<MyRowClass>> lastSelectedRow = new SimpleObjectProperty<>();
//
//	    myTableView.setRowFactory(tableView -> {
//	        TableRow<MyRowClass> row = new TableRow<MyRowClass>();
//
//	        row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
//	            if (isNowSelected) {
//	                lastSelectedRow.set(row);
//	            } 
//	        });
//	        return row;
//	    });
//
//
//	    stage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//
//	        @Override
//	        public void handle(MouseEvent event) {
//	            if (lastSelectedRow.get() != null) {
//	                Bounds boundsOfSelectedRow = lastSelectedRow.get().localToScene(lastSelectedRow.get().getLayoutBounds());
//	                if (boundsOfSelectedRow.contains(event.getSceneX(), event.getSceneY()) == false) {
//	                    myTableView.getSelectionModel().clearSelection();
//	                }
//	            }
//	        }
//	    });		

		populateTable(table, "file:///home/claudius/comune.txt");

		root.setCenter(table);
		Scene scene = new Scene(root, 1000, 700);
		stage.setScene(scene);
		stage.show();
	}

	private void populateTable(final TableView<ObservableList<StringProperty>> table, final String urlSpec) {
		table.getItems().clear();
		table.getColumns().clear();
		table.setPlaceholder(new Label("Loading..."));
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				BufferedReader in = getReaderFromUrl(urlSpec);

				// Data:
				String dataLine;
				while ((dataLine = in.readLine()) != null) {
					final String[] dataValues = dataLine.split(",", -1);
					Platform.runLater(() -> {
						// Add additional columns if necessary:
						for (int columnIndex = table.getColumns()
								.size(); columnIndex < dataValues.length; columnIndex++) {
							table.getColumns().add(createColumn(columnIndex));
						}
						// Add data to table:
						ObservableList<StringProperty> data = FXCollections.observableArrayList();
						for (String value : dataValues) {
							data.add(new SimpleStringProperty(value));
						}
						table.getItems().add(data);
					});
				}
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.setDaemon(true);
		thread.start();
	}

	private TableColumn<ObservableList<StringProperty>, String> createColumn(final int columnIndex) {
		TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
//		String title = String.valueOf(nextChar++);

//		column.setText(title);
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

		return column;
	}
	
	private TableColumn<ObservableList<StringProperty>, String> setColumnHeader(int columnIndex,
			TableColumn<ObservableList<StringProperty>, String> column) {
		String mapChar = String.valueOf(nextChar++);

		Label columnHeader = new Label(mapChar);
		columnHeader.setPrefWidth(Double.MAX_VALUE);
		columnHeader.setId(Integer.toString(columnIndex));
		columnHeader.getStyleClass().clear();

		columnHeader.setOnMouseClicked(event -> {
			String columnHeaderLabelId = ((Label) event.getSource()).getId();
			TableColumn<ObservableList<StringProperty>, String> currentColumn = (TableColumn<ObservableList<StringProperty>, String>) table
					.getColumns().get(Integer.parseInt(columnHeaderLabelId));
			table.getSelectionModel().clearSelection();
			table.getSelectionModel().selectRange(0, currentColumn, table.getItems().size() - 1, currentColumn);
			column.setUserData(true);
			
			event.consume();
		});

		// column.setCellValueFactory(new MapValueFactory(mapChar));
		column.setGraphic(columnHeader);
		column.setSortable(false);

		return column;
	}

	private BufferedReader getReaderFromUrl(String urlSpec) throws Exception {
		URL url = new URL(urlSpec);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		return new BufferedReader(new InputStreamReader(in));
	}

	private ObservableList<Map<String, String>> generateDataInMap(final String urlSpec) {
		BufferedReader in;
		ObservableList<Map<String, String>> allData = FXCollections.observableArrayList();

		try {
			in = getReaderFromUrl(urlSpec);

			String dataLine;
			while ((dataLine = in.readLine()) != null) {

				final String[] dataValues = dataLine.split(",", -1);

				for (int i = 1; i < dataValues.length; i++) {
					Map<String, String> dataRow = new HashMap<>();

					String value = dataValues[i];

					dataRow.put(String.valueOf(nextChar++), value);

					allData.add(dataRow);
				}

				nextChar = 'A';
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allData;
	}

	public static void main(String[] args) {
		launch(args);
	}

}