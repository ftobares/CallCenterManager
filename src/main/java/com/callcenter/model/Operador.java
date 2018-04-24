package com.callcenter.model;

public class Operador extends Empleado {
	
	private static int prioridad = 1;
	
	public Operador(String nombre){
		super(nombre,prioridad);
	}
}
