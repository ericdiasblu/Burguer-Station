package gui;

public class InfoPedidos {
	private int id;
	private int mesa;
	private String itens;
	private int quantidade;
	private String hora;
	private String status;

	public InfoPedidos(int id, int mesa, String itens, int quantidade, String hora, String status) {
		this.id = id;
		this.mesa = mesa;
		this.itens = itens;
		this.quantidade = quantidade;
		this.hora = hora;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public int getMesa() {
		return mesa;
	}

	public String getItens() {
		return itens;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public String getHora() {
		return hora;
	}

	public String getStatus() {
		return status;
	}
}