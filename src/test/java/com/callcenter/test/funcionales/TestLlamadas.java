package com.callcenter.test.funcionales;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.callcenter.business.exception.SinEmpleadosException;
import com.callcenter.manager.Dispatcher;
import com.callcenter.model.Director;
import com.callcenter.model.Empleado;
import com.callcenter.model.Operador;
import com.callcenter.model.Supervisor;
import com.callcenter.queue.QueueService;

public class TestLlamadas {

	private Runnable MyRunnable(Dispatcher dispatcher, int idLlamada) {
		final int id = idLlamada;
		final Dispatcher disp = dispatcher;
		Runnable myRunnable = new Runnable() {
			public void run() {
				disp.dispatchCall(id);
			}
		};
		return myRunnable;
	}

	/**
	 * Test basico:<br>
	 * * 10 llamadas entrantes<br>
	 * * 4 operadores<br>
	 * * 1 supervisor<br>
	 * * 1 director<br>
	 * @throws Exception 
	 * */
	@Test
	public void procesarLlamadas() throws Exception {

		/** Logueo los empleados */
		Empleado director = new Director("Director Jose");
		Empleado supervisor = new Supervisor("Supervisor Francisco");

		Empleado operador1 = new Operador("Operador Manuel");
		Empleado operador2 = new Operador("Operador Gabriel");
		Empleado operador3 = new Operador("Operador Horacio");
		Empleado operador4 = new Operador("Operador Mario");
		
		QueueService qService = new QueueService();
		qService.encolarEmpleado(director);
		qService.encolarEmpleado(supervisor);
		qService.encolarEmpleado(operador1);
		qService.encolarEmpleado(operador2);
		qService.encolarEmpleado(operador3);
		qService.encolarEmpleado(operador4);
		
		/**
		 * Instanciar dispatcher
		 * */
		final Dispatcher dispatcher = new Dispatcher(qService);

		/** Llamadas entrantes */
		Thread th1 = new Thread(MyRunnable(dispatcher, 1));
		Thread th2 = new Thread(MyRunnable(dispatcher, 2));
		Thread th3 = new Thread(MyRunnable(dispatcher, 3));
		Thread th4 = new Thread(MyRunnable(dispatcher, 4));
		Thread th5 = new Thread(MyRunnable(dispatcher, 5));
		Thread th6 = new Thread(MyRunnable(dispatcher, 6));
		Thread th7 = new Thread(MyRunnable(dispatcher, 7));
		Thread th8 = new Thread(MyRunnable(dispatcher, 8));
		Thread th9 = new Thread(MyRunnable(dispatcher, 9));
		Thread th10 = new Thread(MyRunnable(dispatcher, 10));

		// starts
		th1.start();
		th2.start();
		th3.start();
		th4.start();
		th5.start();
		th6.start();
		th7.start();
		th8.start();
		th9.start();
		th10.start();

		// joins
		th1.join();
		th2.join();
		th3.join();
		th4.join();
		th5.join();
		th6.join();
		th7.join();
		th8.join();
		th9.join();
		th10.join();

		assertEquals(10,dispatcher.getCallCounter());
	}
	
	/**
	 * Test de error:<br>
	 * Se valida que falle la aplicación al intentar instanciar la clase Dispatcher sin empleados logueados.
	 * @throws Exception 
	 * */
	@Test
	public void llamadasSinEmpleados() throws Exception {

		QueueService qService = new QueueService();		
		
		/**
		 * Instanciar dispatcher
		 * */
		try{
			final Dispatcher dispatcher = new Dispatcher(qService);
		}catch(SinEmpleadosException ex){
			assertEquals(SinEmpleadosException.class, ex.getClass());
		}catch(Exception e){
			throw new Exception(e);
		}
	}
	
	/**
	 * Test basico extendido:<br>
	 * * 25 llamadas entrantes<br>
	 * * 4 operadores<br>
	 * * 1 supervisor<br>
	 * * 1 director<br>
	 * @throws Exception 
	 * */
	@Test
	public void procesarMasivamenteLlamadas() throws Exception {
		
		int threadCount = 25;
		long timeOut = 350;

		/** Logueo los empleados */
		Empleado director = new Director("Director Jose");
		Empleado supervisor = new Supervisor("Supervisor Francisco");

		Empleado operador1 = new Operador("Operador Manuel");
		Empleado operador2 = new Operador("Operador Gabriel");
		Empleado operador3 = new Operador("Operador Horacio");
		Empleado operador4 = new Operador("Operador Mario");
		
		QueueService qService = new QueueService();
		qService.encolarEmpleado(director);
		qService.encolarEmpleado(supervisor);
		qService.encolarEmpleado(operador1);
		qService.encolarEmpleado(operador2);
		qService.encolarEmpleado(operador3);
		qService.encolarEmpleado(operador4);
		
		/**
		 * Instanciar dispatcher
		 * */
		final Dispatcher dispatcher = new Dispatcher(qService);

		/** Llamadas entrantes */
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		
		for(int i=1; i<=threadCount;i++){
			executor.execute(MyRunnable(dispatcher, i));
		}
		
		executor.awaitTermination(timeOut, TimeUnit.SECONDS);
		
		executor.shutdown();

		assertEquals(threadCount,dispatcher.getCallCounter());
	}	
}
