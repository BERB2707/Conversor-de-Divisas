package service;

import model.ConversionRecord;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;

@ApplicationScoped
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal tasaUsdToCop;

    @PostConstruct
    public void init() {
        cargarTasaDeCambio();
    }

    private void cargarTasaDeCambio() {
        String envRate = System.getenv("EXCHANGE_RATE_USD_COP");
        if (envRate != null && !envRate.isBlank()) {
            this.tasaUsdToCop = new BigDecimal(envRate.trim());
            return;
        }

        // Hacemos la conexion para que solo modif den conf el valor del dolar y no todo el cod
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                String rateStr = prop.getProperty("exchange.rate.usd.to.cop", "3229.53");
                this.tasaUsdToCop = new BigDecimal(rateStr.trim());
            } else {
                this.tasaUsdToCop = new BigDecimal("3229.53");
            }
        } catch (Exception e) {
            this.tasaUsdToCop = new BigDecimal("3229.53");
        }
    }

    public ConversionRecord realizarConversion(BigDecimal monto, String tipo) {
        BigDecimal resultado;
        String etiquetaTipo;

        if ("COP_TO_USD".equals(tipo)) {
            resultado = monto.divide(tasaUsdToCop, 2, RoundingMode.HALF_UP);
            etiquetaTipo = "COP ➔ USD";
        } else {
            resultado = monto.multiply(tasaUsdToCop).setScale(2, RoundingMode.HALF_UP);
            etiquetaTipo = "USD ➔ COP";
        }

        return new ConversionRecord(etiquetaTipo, monto, resultado, tasaUsdToCop);
    }

    public BigDecimal getTasaUsdToCop() {
        return tasaUsdToCop;
    }
}