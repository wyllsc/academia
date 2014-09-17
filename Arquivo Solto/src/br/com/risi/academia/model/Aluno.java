package br.com.risi.academia.model;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
	
	private String nome;
	private String dataInicio;
	private String dataFinal;
	private String tipoPrograma;
	private String Professor;
	private String cref;
	private String observacao;
	private List<Serie> listaSerie;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	public String getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getTipoPrograma() {
		return tipoPrograma;
	}
	public void setTipoPrograma(String tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}
	public String getProfessor() {
		return Professor;
	}
	public void setProfessor(String professor) {
		Professor = professor;
	}
	public String getCref() {
		return cref;
	}
	public void setCref(String cref) {
		this.cref = cref;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public List<Serie> getListaSerie() {
		if (listaSerie == null) {
			listaSerie = new ArrayList<Serie>();
		}
		return listaSerie;
	}
	public void setListaSerie(List<Serie> listaSerie) {
		this.listaSerie = listaSerie;
	}
	

}
