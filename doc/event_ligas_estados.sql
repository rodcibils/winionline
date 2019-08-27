DELIMITER ;;

CREATE DEFINER=`root`@`localhost` 
EVENT `cambiarEstadoLiga` 
ON SCHEDULE EVERY 1 DAY STARTS '2019-07-12 00:00:00' 
ON COMPLETION PRESERVE ENABLE 
DO 
BEGIN
   UPDATE ligas SET estado = 4 /*cambiar a estado iniciada*/
   WHERE fecha_inicio <= (select CURRENT_DATE)
   AND CURRENT_DATE() < fecha_fin;   
   UPDATE ligas SET estado = 5 /*cambiar a estado terminada*/
   WHERE fecha_fin <= (select CURRENT_DATE);
END ;;

DELIMITER ;