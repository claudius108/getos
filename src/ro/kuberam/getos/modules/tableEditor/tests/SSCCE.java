package ro.kuberam.getos.modules.tableEditor.tests;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SSCCE extends Application {

	private TableView<ObservableList<String>> table;
	private char nextChar = 'A';

	@Override
	public void start(Stage stage) {

		DataGenerator dataGenerator = new DataGenerator();

		int N_COLS = 5;
		int N_ROWS = 1_000;
		
		table = new TableView<>();
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.setEditable(true);

		// add the index column
		TableColumn<ObservableList<String>, String> indexColumn = new TableColumn<ObservableList<String>, String>();
		indexColumn = setColumnHeader(0, indexColumn);

//		indexColumn.setCellFactory(
//				new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
//					@Override
//					public TableCell<ObservableList<String>, String> call(
//							TableColumn<ObservableList<String>, String> p) {
//						return new TableCell<ObservableList<String>, String>() {
//							@Override
//							public void updateItem(String item, boolean empty) {
//								super.updateItem((String) item, empty);
//								setGraphic(null);
//								setText(empty ? null : getIndex() + 1 + "");
//							}
//						};
//					}
//				});
		table.getColumns().add(indexColumn);

		// add columns
		for (int i = 0; i < N_COLS; i++) {
			String mapChar = String.valueOf(nextChar++);
			
			final int finalIdx = i;
			TableColumn<ObservableList<String>, String> column = new TableColumn<>(mapChar);
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)));
			
//			column.setOnEditCommit(
//				    new EventHandler<CellEditEvent<ObservableList<String>, String>>() {
//				        @Override
//				        public void handle(CellEditEvent<ObservableList<String>, String> t) {
////				            ((ObservableList<String>) t.getTableView().getItems().get(
////				                t.getTablePosition().getRow());
//				        }
//				    }
//				);

			column.setSortable(false);

			table.getColumns().add(column);
		}

		// table.getSelectionModel().selectedItemProperty().addListener(new
		// ChangeListener<ObservableList<String>>() {
		// @Override
		// public void changed(ObservableValue<? extends DataGenerator> observable,
		// DataGenerator oldValue,
		// DataGenerator newValue) {
		//
		// }
		// });

		// add data
		for (int i = 0; i < N_ROWS; i++) {
			table.getItems().add(FXCollections.observableArrayList(dataGenerator.getNext(N_COLS)));
		}

		Scene scene = new Scene(table);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

	private TableColumn<ObservableList<String>, String> setColumnHeader(int columnIndex,
			TableColumn<ObservableList<String>, String> column) {
		String mapChar = String.valueOf(nextChar++);

		Label columnHeader = new Label(mapChar);
		columnHeader.setId(Integer.toString(columnIndex));

		columnHeader.setOnMouseReleased(ev -> {
			String columnHeaderLabelId = ((Label) ev.getSource()).getId();
			TableColumn<ObservableList<String>, String> currentColumn = (TableColumn<ObservableList<String>, String>) table
					.getColumns().get(Integer.parseInt(columnHeaderLabelId));
			table.getSelectionModel().selectRange(0, currentColumn, table.getItems().size() - 1, currentColumn);
		});

		// column.setCellValueFactory(new MapValueFactory(mapChar));
		column.setGraphic(columnHeader);
		column.setSortable(false);

		return column;
	}

	class DataGenerator {
		private final String[] LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc tempus cursus diam ac blandit. Ut ultrices lacus et mattis laoreet. Morbi vehicula tincidunt eros lobortis varius. Nam quis tortor commodo, vehicula ante vitae, sagittis enim. Vivamus mollis placerat leo non pellentesque. Nam blandit, odio quis facilisis posuere, mauris elit tincidunt ante, ut eleifend augue neque dictum diam. Curabitur sed lacus eget dolor laoreet cursus ut cursus elit. Phasellus quis interdum lorem, eget efficitur enim. Curabitur commodo, est ut scelerisque aliquet, urna velit tincidunt massa, tristique varius mi neque et velit. In condimentum quis nisi et ultricies. Nunc posuere felis a velit dictum suscipit ac non nisl. Pellentesque eleifend, purus vel consequat facilisis, sapien lacus rutrum eros, quis finibus lacus magna eget est. Nullam eros nisl, sodales et luctus at, lobortis at sem."
				.split(" ");

		private int curWord = 0;

		List<String> getNext(int nWords) {
			List<String> words = new ArrayList<>();

			for (int i = 0; i < nWords; i++) {
				if (curWord == Integer.MAX_VALUE) {
					curWord = 0;
				}

				words.add(LOREM[curWord % LOREM.length]);
				curWord++;
			}

			return words;
		}
	}
}