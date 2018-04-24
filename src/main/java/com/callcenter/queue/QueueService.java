package com.callcenter.queue;

import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.callcenter.model.Empleado;

/**
 * Clase encargada de manejar la cola de empleados libres.
 * Como las llamadas deben ser atendidas segun un orden (Operadores > Supervisor > Director),
 * se utiliza una cola con prioridad (PriorityQueue)
 * */
public class QueueService {
	
	private static final Logger log = Logger.getLogger(QueueService.class);
	
	/* Cola de Empleados Libres */
	private Queue<Empleado> colaEmpleados;
	
	public Queue<Empleado> getColaEmpleados() {
		return colaEmpleados;
	}
	
	public QueueService(){
		this.colaEmpleados = new PriorityQueue<Empleado>();		
	}
	
	/**
	 * Verifico si en la cola de empleados libres hay alguno.
	 * Para evitar que mas de una llamada consulte al mismo tiempo
	 * pongo al mentodo como synchronized
	 * */
	public Boolean hayEmpleadoLibre(){
		return !colaEmpleados.isEmpty();
	}
	
	/**
	 * Como el empleado esta libre lo asigno a la cola de empleados libres
	 * */
	public synchronized void encolarEmpleado(Empleado empleado){
		log.info("#### El empleado "+empleado.getNombre()+" esta libre ####");
		colaEmpleados.add(empleado);
	}

	public synchronized Empleado getEmpleado() {
		return colaEmpleados.poll();
	}
}
