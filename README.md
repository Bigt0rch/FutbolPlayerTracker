# FutbolPlayerTracker

## Autores
* Pedro Luis Guerrica-Echevarría Agudo [`@PedroLuisGEA`](https://github.com/PedroLuisGEA)
* Manuela Bautista Javier [`@manubj`](https://github.com/manubj)
* Guillermo Franco Gimeno [`@GF3000`](https://github.com/GF3000)
* Francisco Javier de Andrés Sánchez [`@Jotta03`](https://github.com/Jotta03)
* Víctor Ramírez Castaño [`@Bigt0rch`](https://github.com/Bigt0rch)


## Configuracion agentes
Los agentes se deben lanzar en el mismo orden que vienen aqui descritos
1. `AgenteBuscador` 
    - **Main class**: jade.boot 
    - **Program arguments**: -gui agenteBuscador:buscador.AgenteBuscador
2. `AgenteProcesador` 
    - **Main class**: jade.boot 
    - **Program arguments**: -main false -host 127.0.0.1 agenteProcesador:procesador.AgenteProcesador
    - **VM arguments**: ---add-opens java.base/java.lang=ALL-UNNAMED
3. `AgenteVisualizador` 
    - **Main class**: jade.boot 
    - **Program arguments**: -main false -host 127.0.0.1 agenteVisualizador:visualizador.AgenteVisualizador

## Funciones del sistema
* **Jugador**: permite obtener las estadisticas de un jugador de campo expresadas en funcion de 90 minutos de juegos para realizar comparativas más justas. En el caso de los porteros utilizará sus estadisticas globales.
* **Comparar**: permite obtener los datos de dos jugadores distintos en el mismo formato que en jugador y observar una comparación directa.
* **Equipo**: obtiene los datos del equipo indicado durante esta temporada.
* **Correlaciones**: muestra graficas para estudiar la correlacion entre ciertas estadisticas de los jugadores, junto con la p de Pearson correspondiente.
* **Clustering**: Agrupa a los jugadores de campo que han participado en al menos un partido en los grupos que el usuario indique, emplea el algoritmo de K means.
* **Actualizar**: ordena al Agente Buscador, que es el que proporciona los datos, que actualice su base de datos haciendo web scrapping de la página web de LaLiga. Esta accion puede llevar alrededor de 20 mins. 

## Uso de los Scripts Python
1. Tener instalado Python
2. Instalar las librerías necesarias con 
`pip install -r requirements.txt`
3. Uso `python python_tools/getPhoto.py <url> (<output_file>)` y `python python_tools/scrapLaLiga.py <output_name> (<num_jugadores>)`
