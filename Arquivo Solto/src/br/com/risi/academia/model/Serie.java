package br.com.risi.academia.model;

import java.util.ArrayList;
import java.util.List;

public class Serie {

	private String tipoSerie;
	private String numero;
	private String exercicio;
	private String observacao;
	private String serie;
	private String repeticao;
	private String carga;
	private List<Serie> listaHash;

	public String getExercicio() {
		return exercicio;
	}

	public void setExercicio(String exercicio) {
		this.exercicio = exercicio;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getRepeticao() {
		return repeticao;
	}

	public void setRepeticao(String repeticao) {
		this.repeticao = repeticao;
	}

	public String getCarga() {
		return carga;
	}

	public void setCarga(String carga) {
		this.carga = carga;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public List<Serie> getListaHash() {
		if (listaHash == null) {
			listaHash = new ArrayList<Serie>();
		}
		return listaHash;
	}

	public void setListaHash(List<Serie> listaHash) {
		this.listaHash = listaHash;
	}

	public String getTipoSerie() {
		return tipoSerie;
	}

	public void setTipoSerie(String tipoSerie) {
		this.tipoSerie = tipoSerie;
	}
}
