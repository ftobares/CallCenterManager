package com.callcenter.manager;

import java.util.Random;

import org.apache.log4j.Logger;

import com.callcenter.business.exception.SinEmpleadosException;
import com.callcenter.model.Empleado;
import com.callcenter.model.Llamada;
import com.callcenter.queue.QueueService;

/**
 * Clase encargada de manejar las llamadas entrantes.
 * Para despachar las llamadas se debe instanciar y luego invocar el metodo dispatchCall(int idLlamada)
 * */
public class Dispatcher {

	private static final Logger log = Logger.getLogger(Dispatcher.class);

	private QueueService qService;
	
	/**
	 * Contador de llamadas atendidas.
	 * */
	private int callCounter;
	
	private void incrementCallCounter(){
		this.callCounter++;
	}
	
	public int getCallCounter(){
		return this.callCounter;
	}
	
	public QueueService getqService() {
		return qService;
	}

	public Dispatcher(QueueService qServ) throws Exception {
		log.info("#### INICIALIZO QUEUE EMPLEADOS ####");
		if(qServ.getColaEmpleados().isEmpty()){
			throw new SinEmpleadosException("ERROR - Se debe loguear empleados para atender las llamadas");
		}else{
			this.qService = qServ;
		}
	}
	
	/**
	 * Metodo encargado de despachar las llamadas a partir de un codigo de llamada.
	 * Pre-requisito: 
	 * 		Los empleados deben loguearse utilizando EmpleadoQueueService.loguearEmpleado
	 * */
	public void dispatchCall(int idLlamada) {

		/**
		 * Instanciar la llamada
		 * */
		Llamada llamada = new Llamada(idLlamada);
		
		Empleado empleado;
		synchronized(qService.getColaEmpleados()){
			
			log.info("#### Despachar llamada entrante id " + llamada.getIdLlamada()
					+ " ####");
			
			/** Valido que haya empleados libres. 
			 *  Si no hay empleados libres, dejo el thread de la llamda en espera con wait().
			 * */
			if(!qService.hayEmpleadoLibre()){
				try {
					qService.getColaEmpleados().wait();
				} catch (InterruptedException e) {
					log.error("#### ERROR en el wait de dispatchCall " + idLlamada
							+ " ####", e);
					System.err.println ("Error en el wait de dispatchCall: "+ e.toString());
				}
			}
			
			/**
			 * Tomo un empleado segun la prioridad. Tomo el primer operador que esta
			 * libre, Si no hay libre tomo al supervisor, Si no esta libre tomo al
			 * director.
			 * */
			empleado = qService.getEmpleado();	
			
			/**
			 * Al empleado le asigno la llamada
			 * */
			empleado.setLlamadaEnAtencion(llamada);

			log.info("#### El empleado " + empleado.getNombre()
						+ " toma la llamada " + llamada.getIdLlamada() + " ####");
		}		

		/**
		 * Este metodo simula la atención de la llamada y la ejecución de las
		 * tareas que debería realizar el empleado.
		 * */
		atenderLlamada(llamada.getIdLlamada());

		synchronized(qService.getColaEmpleados()){
			/**
			 * Libero al empleado
			 * */
			qService.encolarEmpleado(empleado);
			
			/**
			 * Notifico que hay un empleado libre, de modo que 
			 * los threads con llamadas en espera puedan continuar.
			 * */
			qService.getColaEmpleados().notify();
		}
	}

	/**
	 * Simula la atencion de la llamada. 
	 * La llamada dura un tiempo aleatorio entre 5 y 10 segundos
	 * */
	private void atenderLlamada(int idLlamada) {
	
		Random random = new Random();

		synchronized (random) {
			log.info("#### Atiendo llamada entrante id " + idLlamada + " ####");
			
			int duracionLlamada = random.nextInt(10);
			duracionLlamada = (duracionLlamada <= 5) ? duracionLlamada+5: duracionLlamada;

			try {
				Thread.sleep(duracionLlamada*1000);
			} catch (InterruptedException e) {
				log.error("#### ERROR al despachar llamada " + idLlamada
						+ " ####", e);
				System.err.println ("Error Sleep en atenderLlamada(): "+ e.toString());
			}
			
			incrementCallCounter();

			log.info("#### La llamada id " + idLlamada + " tuvo una duracion de "
					+ duracionLlamada + " ####");			
		}		
	}

}
