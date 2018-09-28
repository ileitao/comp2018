#!/bin/bash
#./yacc.exe -Jnorun -Jnodebug -Jthrows=IOException -Jsemantic=Referenceable Gramatica.y
# Opción a correr en Windows
./yacc.exe -Jnorun -Jnodebug -Jpackage=compilador.analizadorsintactico -Jthrows=IOException -v -J Gramatica.y
# Opción a correr en Linux (Requiere wine)
#wine yacc.exe -Jnorun -Jnodebug -Jpackage=compilador.analizadorsintactico -Jthrows=IOException -v -J Gramatica.y
