# TP Final Java 2019 - UTN FRRO

## Modelo de Dominio

![MD](https://raw.githubusercontent.com/rodcibils/winionline/master/doc/MD.png)

## Casos de Uso

    • ABM Usuario
    • ABM Liga
    • Inscribirse a Liga
    • Solicitar Partido Amistoso
    • Solicitar Partido Liga
    • Confirmar Partido
    • Registrar Resultado Partido
        ◦ Cualquier jugador puede registrar el resultado, solo se realiza una vez.
    • Disputar Resultado Partido
        ◦ El jugador que no registro el resultado puede disputarlo si considera que es erroneo.
    • Subir Evidencia Partido Disputado
        ◦ Ambos jugadores pueden presentar evidencia (screenshots o videos subidos a alguna web) para respaldar su caso en la disputa.
    • Votar Disputa Partido
        ◦ Todos los usuarios con mas de 15 partidos jugados de antigüedad pueden votar que jugador consideran el ganador en la disputa.
    • Apelar Disputa Partido
        ◦ El jugador desfavorecido en la disputa la puede apelar como ultima opcion.
    • Dictamen Apelacion Partido
        ◦ Un tribunal compuesto de 5 usuarios administrador votan a quien consideran ganador como ultima palabra sobre el caso.
    • Reporte Liga
        ◦ Posiciones, puntos, victorias, empates, derrotas, goles a favor, goles en contra de todos los usuarios que participaron de una liga.
    • Reporte Disputas
        ◦ Reporte de conteo de votos y usuarios que votaron en cada disputa.
    • Reporte Apelaciones
        ◦ Reporte de resultado final de tribunal para las apelaciones.
    • Reporte Usuario
        ◦ Historial de amistosos y de participaciones en liga de un usuario
