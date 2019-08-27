CREATE DEFINER=`root`@`localhost` 
EVENT `limpiarSolicitudesAmistoso` 
ON SCHEDULE EVERY 1 DAY STARTS '2019-07-23 00:00:00' 
ON COMPLETION PRESERVE ENABLE 
DO 
	delete from solicitudes where
		vencimiento < CURRENT_DATE() and estado = 6;