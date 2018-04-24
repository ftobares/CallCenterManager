package com.callcenter.model;

public class Empleado implements Comparable<Empleado>{
	
	private String nombre;
	private Llamada llamadaEnAtencion;
	private int prioridad;

	public Empleado(String n, int p){
		this.nombre = n;
		this.prioridad = p;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	
	
	public Llamada getLlamadaEnAtencion() {
		return llamadaEnAtencion;
	}

	public void setLlamadaEnAtencion(Llamada llamadaEnAtencion) {
		this.llamadaEnAtencion = llamadaEnAtencion;
	}
	
	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}	

	@Override
	public int compareTo(Empleado o) {
		if(this.prioridad > o.getPrioridad()){
			return 1;
		}else if(this.prioridad < o.getPrioridad()){
			return -1;
		}else{
			return 0;
		}		
	}

}
