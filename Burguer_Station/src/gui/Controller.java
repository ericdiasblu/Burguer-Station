package gui;

import java.io.IOException;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Controller {

	private Parent root;
	private Stage stage;
	private Scene scene;

	@FXML
	private TextField txtMesa;

	@FXML
	private TextField txtHora;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnBuscar;

	@FXML
	private Button btnRecarregar;

	@FXML
	private Button btnReturn;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnDeletar;

	@FXML
	private ComboBox<String> comboBoxBurgers;

	@FXML
	private ComboBox<String> comboBoxStatus;

	@FXML
	private Spinner<Integer> spinnerQtd;

	@FXML
	private TableView<InfoPedidos> table;

	@FXML
	private TableColumn<InfoPedidos, Integer> IDColmn;

	@FXML
	private TableColumn<InfoPedidos, Integer> MesaColmn;

	@FXML
	private TableColumn<InfoPedidos, String> ItensColmn;

	@FXML
	private TableColumn<InfoPedidos, Integer> QuantidadeColmn;

	@FXML
	private TableColumn<InfoPedidos, String> HorarioColmn;

	@FXML
	private TableColumn<InfoPedidos, String> StatusColmn;

	@FXML
	public void initialize() {
		btnBuscar.setOnAction(event -> buscarPedido());
		btnSalvar.setOnAction(event -> OnBtnSalvar());
		btnRecarregar.setOnAction(event -> gerarRelatorio());
		btnEditar.setOnAction(event -> editarPedido());
		btnDeletar.setOnAction(event -> deletarPedido());

		gerarRelatorio();

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1);
		spinnerQtd.setValueFactory(valueFactory);

		comboBoxBurgers.getItems().addAll("Cheeseburger", "Veggie Burger", "Chicken Burger");
		comboBoxBurgers.setValue("Cheeseburger");

		comboBoxStatus.getItems().addAll("Em preparação", "Pronto", "Entregue");
		comboBoxStatus.setValue("Em preparação");

		// Adicionar um listener para a seleção da TableView
		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				txtMesa.setText(String.valueOf(newValue.getMesa()));
				comboBoxBurgers.setValue(newValue.getItens());
				spinnerQtd.getValueFactory().setValue(newValue.getQuantidade());
				txtHora.setText(newValue.getHora());
				comboBoxStatus.setValue(newValue.getStatus());
			}
		});
	}

	@FXML
	public void OnBtnSalvar() {

		try {

			String itens = comboBoxBurgers.getValue();
			int mesa = Integer.parseInt(txtMesa.getText());
			int quantidade = spinnerQtd.getValue();
			String hora = txtHora.getText();
			String status = comboBoxStatus.getValue();

			SQLCommands dbOps = new SQLCommands();
			dbOps.inserirDados(mesa, itens, quantidade, hora, status);

			JOptionPane.showMessageDialog(null, "Dados inseridos com sucesso!");

			gerarRelatorio();

		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "Erro desconhecido: " + e2.getMessage());
		}

	}

	@FXML
	public void gerarRelatorio() {
		try {
			SQLCommands dbOps = new SQLCommands();
			ObservableList<InfoPedidos> dados = dbOps.gerarRelatorio();

			// Configurar as colunas da TableView
			IDColmn.setCellValueFactory(new PropertyValueFactory<>("id"));
			MesaColmn.setCellValueFactory(new PropertyValueFactory<>("mesa"));
			ItensColmn.setCellValueFactory(new PropertyValueFactory<>("itens"));
			QuantidadeColmn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
			HorarioColmn.setCellValueFactory(new PropertyValueFactory<>("hora"));
			StatusColmn.setCellValueFactory(new PropertyValueFactory<>("status"));

			// Preencher a TableView com os dados
			table.setItems(dados);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " + e.getMessage());
		}

	}

	@FXML
	private TextField txtPedidoID; // Campo de texto onde o usuário pode inserir o ID do pedido.

	@FXML
	public void buscarPedido() {
		try {
			int id = Integer.parseInt(txtPedidoID.getText());

			SQLCommands dbOps = new SQLCommands();
			InfoPedidos pedido = dbOps.buscarPedido(id);

			if (pedido != null) {
				// Se o pedido for encontrado, atualize a TableView para mostrar apenas esse
				// pedido
				ObservableList<InfoPedidos> dados = FXCollections.observableArrayList(pedido);
				table.setItems(dados);

				// Se desejar, pode preencher outros campos da interface
				txtMesa.setText(String.valueOf(pedido.getMesa()));
				comboBoxBurgers.setValue(pedido.getItens());
				spinnerQtd.getValueFactory().setValue(pedido.getQuantidade());
				txtHora.setText(pedido.getHora());
				comboBoxStatus.setValue(pedido.getStatus());
			} else {
				JOptionPane.showMessageDialog(null, "Pedido não encontrado com o ID fornecido.");
			}

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "ID inválido. Por favor, insira um número inteiro.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar o pedido: " + e.getMessage());
		}
	}

	@FXML
	public void trocarTela(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void editarPedido() {
		InfoPedidos pedidoSelecionado = table.getSelectionModel().getSelectedItem();

		if (pedidoSelecionado != null) {
			// Obter os dados do pedido selecionado
			int id = pedidoSelecionado.getId();
			int mesa = Integer.parseInt(txtMesa.getText());
			String itens = comboBoxBurgers.getValue();
			int quantidade = spinnerQtd.getValue();
			String hora = txtHora.getText();
			String status = comboBoxStatus.getValue();

			SQLCommands dbOps = new SQLCommands();
			dbOps.atualizarDados(id, mesa, itens, quantidade, hora, status);

			JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");

			// Atualizar a TableView
			gerarRelatorio();
		} else {
			JOptionPane.showMessageDialog(null, "Nenhum pedido selecionado.");
		}
	}

	@FXML
	private void deletarPedido() {
		InfoPedidos pedidoSelecionado = table.getSelectionModel().getSelectedItem();

		if (pedidoSelecionado != null) {
			int id = pedidoSelecionado.getId();

			SQLCommands dbOps = new SQLCommands();
			dbOps.deletarPedido(id);

			JOptionPane.showMessageDialog(null, "Pedido deletado com sucesso!");

			// Atualizar a TableView
			gerarRelatorio();
		} else {
			JOptionPane.showMessageDialog(null, "Nenhum pedido selecionado para deletar.");
		}
	}

}
