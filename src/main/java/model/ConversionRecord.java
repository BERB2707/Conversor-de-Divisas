package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tipoConversion;
    private BigDecimal montoOrigen;
    private BigDecimal montoResultado;
    private BigDecimal tasaAplicada;
    private String fechaHora;

    public ConversionRecord(String tipoConversion, BigDecimal montoOrigen, BigDecimal montoResultado, BigDecimal tasaAplicada) {
        this.tipoConversion = tipoConversion;
        this.montoOrigen = montoOrigen;
        this.montoResultado = montoResultado;
        this.tasaAplicada = tasaAplicada;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public String getTipoConversion() { return tipoConversion; }
    public BigDecimal getMontoOrigen() { return montoOrigen; }
    public BigDecimal getMontoResultado() { return montoResultado; }
    public BigDecimal getTasaAplicada() { return tasaAplicada; }
    public String getFechaHora() { return fechaHora; }
}