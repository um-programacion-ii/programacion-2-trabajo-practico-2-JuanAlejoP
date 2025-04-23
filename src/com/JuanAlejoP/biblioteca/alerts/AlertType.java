package com.JuanAlejoP.biblioteca.alerts;

/**
 * Enumeración de tipos de alerta que pueden generarse en el sistema de notificaciones.
 * <ul>
 *   <li>{@link #INFO} para mensajes informativos.</li>
 *   <li>{@link #WARNING} para advertencias que requieren atención.</li>
 *   <li>{@link #ERROR} para errores críticos que indican fallos en el sistema o procesos.</li>
 * </ul>
 */
public enum AlertType {
    /** Tipo de alerta informativa, utilizada para notificaciones rutinarias sin impacto negativo inmediato. */
    INFO,

    /** Tipo de alerta de advertencia, utilizada para señalar posibles problemas o situaciones que requieren revisión. */
    WARNING,

    /** Tipo de alerta de error, utilizada para indicar fallos o condiciones críticas que deben resolverse. */
    ERROR
}