package com.callcenter.model;

public class Director extends Empleado {
	
	private static int prioridad = 3;
	
	public Director(String nombre){
		super(nombre,prioridad);
	}
}
