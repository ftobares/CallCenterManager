# CallCenterManager
Demo simple de atención de llamadas en un call center

## Instalación
1) Ejecutar con Maven; *eclipse:clean eclipse:eclipse*
2) Convertir el proyecto a Maven

## Utilización
Para poder empezar a recibir llamadas se deberá tener instanciadas dos clases. Por un lado **QueueService** la cual se encarga de manejar la cola de empleados que se encuentran desocupados. Y por el otro lado **Dispatcher** la cual se encarga de manejar y asignar las llamadas a los empleados.

Para que la aplicación funcione correctamente deben haber empleados logueados, esto se realiza mediante el método QueueService.encolarEmpleado, pasandole como parámetro una instancia de la clase **Empleado**. La cola de empleados sera una cola con prioridades, la cual será determinada por el atributo prioridad.

Para poder atender llamadas, se debe invocar el método Dispatcher.dispatchCall, pasandole como parámetro el tipo int con el id de la llamada.

La aplicación puede procesar llamadas de forma concurrente. El número de llamadas será determinado por la cantidad de empleados que puedan atender. Primero siendo asignados a los Operadores, luego a él/los Supervisores y por ultimo al Director.
Las llamadas que no pueden ser atendidas quedarán en espera (es decir el Thread que hizo dicho invocación quedará en pausa con wait()).
Una vez un empleado se ha liberado, será incorporado nuevamente a la cola de empleados libres y se informará para que se pueda atender la siguiente llamada. (es decir el Thread que termina la invocación lo hará con notify() de la cola de empleados).

## Suposiciones
Se asume que habrá una capa intermedia (microservicio, app web, etc) entre algún tipo de llamador (IVR) y esta aplicación.
Dicha capa será la encargada de instanciar el Dispatcher y enviarle las llamadas.
Además como la clase **Dispatcher** requiere de una servicio que maneje la cola de empleados, el logueo, etc. Dicha tarea se realizará antes. En caso de no hacerlo, la aplicación lanzara la excepción **SinEmpleadosException**

## Unit Test
1) Test básico en el cual se tienen 10 llamadas entrantes de forma concurrente. 4 Operadores, 1 Supervisor y 1 Director.

2) Test por error. Se intenta instaciar el Dispatcher sin tener empleados logueados.

3) Test extendido en el cual se reciben más de 10 llamadas de forma concurrente.
