package controller;

import model.ConversionRecord;
import service.Service;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Named("converterBean")
@ViewScoped
public class ConverterDiv implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_HISTORIAL = 5;

    @Inject
    private Service exchangeService;

    private BigDecimal montoInput;
    private String tipoSeleccionado;
    private ConversionRecord ultimaConversion;
    private LinkedList<ConversionRecord> historial;

    @PostConstruct
    public void init() {
        this.tipoSeleccionado = "COP_TO_USD";
        this.historial = new LinkedList<>();
    }

    public void convertir() {
        if (montoInput == null || montoInput.compareTo(BigDecimal.ZERO) <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validación", "El monto tiene ser mayor a cero."));
            return;
        }

        ultimaConversion = exchangeService.realizarConversion(montoInput, tipoSeleccionado);

        // ACA ponemos lo de inserta al inicio de la lista y limitar a los últimos 5 registros
        historial.addFirst(ultimaConversion);
        if (historial.size() > MAX_HISTORIAL) {
            historial.removeLast();
        }

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Conversión Exitosa", "Cálculo procesado en tiempo real."));
    }

    // Getters y Setters
    public BigDecimal getMontoInput() { return montoInput; }
    public void setMontoInput(BigDecimal montoInput) { this.montoInput = montoInput; }

    public String getTipoSeleccionado() { return tipoSeleccionado; }
    public void setTipoSeleccionado(String tipoSeleccionado) { this.tipoSeleccionado = tipoSeleccionado; }

    public ConversionRecord getUltimaConversion() { return ultimaConversion; }
    public List<ConversionRecord> getHistorial() { return historial; }

    public BigDecimal getTasaActual() {
        return exchangeService.getTasaUsdToCop();
    }
}