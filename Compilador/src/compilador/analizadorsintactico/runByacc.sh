#!/bin/bash
#./yacc.exe -Jnorun -Jnodebug -Jthrows=IOException -Jsemantic=Referenceable Gramatica.y
./yacc.exe -Jnorun -Jnodebug -Jpackage=compilador.analizadorsintactico -Jthrows=IOException -J Gramatica.y