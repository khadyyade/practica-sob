package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.CustomerDTO;

/**
 * Interfaz del servicio de Customer.
 * 
 * Define las operaciones relacionadas con la autenticación de clientes.
 * Usamos una interfaz para seguir el patrón que usa el profesor
 * y poder cambiar la implementación si hace falta (por ejemplo, para tests).
 * 
 * La implementación real está en CustomerServiceImpl.
 */
public interface CustomerService {

    /**
     * Comprueba si las credenciales (usuario y contraseña) son correctas.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @return true si las credenciales son válidas, false si no
     */
    boolean authenticate(String username, String password);

    /**
     * Obtiene los datos del usuario autenticado.
     * 
     * @param authHeader header de autorización "Basic base64(user:pass)"
     * @return CustomerDTO con los datos del usuario, o null si hay error
     */
    CustomerDTO getAuthenticatedCustomer(String authHeader);
}
