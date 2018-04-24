package com.callcenter.model;

public class Supervisor extends Empleado {
	
	private static int prioridad = 2;
	
	public Supervisor(String nombre){
		super(nombre,prioridad);
	}
}