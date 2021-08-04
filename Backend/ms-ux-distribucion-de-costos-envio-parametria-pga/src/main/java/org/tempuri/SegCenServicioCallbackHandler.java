/**
 * SegCenServicioCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 */
package org.tempuri;


/**
 *  SegCenServicioCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class SegCenServicioCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public SegCenServicioCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public SegCenServicioCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for validarEstadoCuenta method
     * override this method for handling normal response from validarEstadoCuenta operation
     */
    public void receiveResultvalidarEstadoCuenta(
        org.tempuri.SegCenServicioStub.ValidarEstadoCuentaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from validarEstadoCuenta operation
     */
    public void receiveErrorvalidarEstadoCuenta(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for verificaUsuarioObtienePermisosWithoutIPHostName method
     * override this method for handling normal response from verificaUsuarioObtienePermisosWithoutIPHostName operation
     */
    public void receiveResultverificaUsuarioObtienePermisosWithoutIPHostName(
        org.tempuri.SegCenServicioStub.VerificaUsuarioObtienePermisosWithoutIPHostNameResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from verificaUsuarioObtienePermisosWithoutIPHostName operation
     */
    public void receiveErrorverificaUsuarioObtienePermisosWithoutIPHostName(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for obtenerNombreUsuario method
     * override this method for handling normal response from obtenerNombreUsuario operation
     */
    public void receiveResultobtenerNombreUsuario(
        org.tempuri.SegCenServicioStub.ObtenerNombreUsuarioResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from obtenerNombreUsuario operation
     */
    public void receiveErrorobtenerNombreUsuario(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for verificaUsuarioObtienePermisos method
     * override this method for handling normal response from verificaUsuarioObtienePermisos operation
     */
    public void receiveResultverificaUsuarioObtienePermisos(
        org.tempuri.SegCenServicioStub.VerificaUsuarioObtienePermisosResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from verificaUsuarioObtienePermisos operation
     */
    public void receiveErrorverificaUsuarioObtienePermisos(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for obtenerRolesUsuarioAplicacion method
     * override this method for handling normal response from obtenerRolesUsuarioAplicacion operation
     */
    public void receiveResultobtenerRolesUsuarioAplicacion(
        org.tempuri.SegCenServicioStub.ObtenerRolesUsuarioAplicacionResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from obtenerRolesUsuarioAplicacion operation
     */
    public void receiveErrorobtenerRolesUsuarioAplicacion(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for obtenerAccesosApp method
     * override this method for handling normal response from obtenerAccesosApp operation
     */
    public void receiveResultobtenerAccesosApp(
        org.tempuri.SegCenServicioStub.ObtenerAccesosAppResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from obtenerAccesosApp operation
     */
    public void receiveErrorobtenerAccesosApp(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for agregarLogCambios method
     * override this method for handling normal response from agregarLogCambios operation
     */
    public void receiveResultagregarLogCambios(
        org.tempuri.SegCenServicioStub.AgregarLogCambiosResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from agregarLogCambios operation
     */
    public void receiveErroragregarLogCambios(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for agregarLogLogon method
     * override this method for handling normal response from agregarLogLogon operation
     */
    public void receiveResultagregarLogLogon(
        org.tempuri.SegCenServicioStub.AgregarLogLogonResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from agregarLogLogon operation
     */
    public void receiveErroragregarLogLogon(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for registrarLogLogout method
     * override this method for handling normal response from registrarLogLogout operation
     */
    public void receiveResultregistrarLogLogout(
        org.tempuri.SegCenServicioStub.RegistrarLogLogoutResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from registrarLogLogout operation
     */
    public void receiveErrorregistrarLogLogout(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for obtenerRolesAplicacion method
     * override this method for handling normal response from obtenerRolesAplicacion operation
     */
    public void receiveResultobtenerRolesAplicacion(
        org.tempuri.SegCenServicioStub.ObtenerRolesAplicacionResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from obtenerRolesAplicacion operation
     */
    public void receiveErrorobtenerRolesAplicacion(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for validarUsuarioApp method
     * override this method for handling normal response from validarUsuarioApp operation
     */
    public void receiveResultvalidarUsuarioApp(
        org.tempuri.SegCenServicioStub.ValidarUsuarioAppResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from validarUsuarioApp operation
     */
    public void receiveErrorvalidarUsuarioApp(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for verificarExistenciaUsuario method
     * override this method for handling normal response from verificarExistenciaUsuario operation
     */
    public void receiveResultverificarExistenciaUsuario(
        org.tempuri.SegCenServicioStub.VerificarExistenciaUsuarioResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from verificarExistenciaUsuario operation
     */
    public void receiveErrorverificarExistenciaUsuario(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for obtenerUsuario method
     * override this method for handling normal response from obtenerUsuario operation
     */
    public void receiveResultobtenerUsuario(
        org.tempuri.SegCenServicioStub.ObtenerUsuarioResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from obtenerUsuario operation
     */
    public void receiveErrorobtenerUsuario(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for verificaUsuarioObtienePermisosWithoutSoloPermitidasIPHostName method
     * override this method for handling normal response from verificaUsuarioObtienePermisosWithoutSoloPermitidasIPHostName operation
     */
    public void receiveResultverificaUsuarioObtienePermisosWithoutSoloPermitidasIPHostName(
        org.tempuri.SegCenServicioStub.VerificaUsuarioObtienePermisosWithoutSoloPermitidasIPHostNameResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from verificaUsuarioObtienePermisosWithoutSoloPermitidasIPHostName operation
     */
    public void receiveErrorverificaUsuarioObtienePermisosWithoutSoloPermitidasIPHostName(
        java.lang.Exception e) {
    }
}
