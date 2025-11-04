package org.example;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Una calculadora sencilla que demuestra el uso de componentes básicos de Swing.
 */
public class Calculadora extends JFrame implements ActionListener {

    private static final String[] TECLAS = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "C", "+"
    };

    private final JTextField display;
    private double acumulador = 0;
    private String operacionPendiente = "";
    private boolean reiniciarDisplay = false;

    public Calculadora() {
        super("Calculadora Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 420);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        cabecera.setBackground(new Color(240, 240, 240));

        JLabel titulo = new JLabel("Calculadora Básica", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(new Color(33, 33, 33));
        cabecera.add(titulo, BorderLayout.NORTH);

        display = new JTextField("0");
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 28));
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cabecera.add(display, BorderLayout.CENTER);

        add(cabecera, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 4, 8, 8));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotones.setBackground(new Color(250, 250, 250));

        for (String texto : TECLAS) {
            JButton boton = crearBoton(texto);
            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.CENTER);

        JButton botonIgual = crearBoton("=");
        botonIgual.setPreferredSize(new Dimension(0, 60));
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panelInferior.add(botonIgual, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 18));
        boton.setFocusPainted(false);
        boton.addActionListener(this);
        if (esOperacion(texto) || "=".equals(texto)) {
            boton.setBackground(new Color(66, 133, 244));
            boton.setForeground(Color.WHITE);
        } else if ("C".equals(texto)) {
            boton.setBackground(new Color(219, 68, 55));
            boton.setForeground(Color.WHITE);
        } else {
            boton.setBackground(new Color(245, 245, 245));
        }
        return boton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("C".equals(comando)) {
            limpiar();
            return;
        }

        if ("=".equals(comando)) {
            calcularResultado();
            return;
        }

        if (esOperacion(comando)) {
            prepararOperacion(comando);
        } else {
            agregarDigito(comando);
        }
    }

    private void agregarDigito(String digito) {
        if (reiniciarDisplay) {
            display.setText(".".equals(digito) ? "0." : digito);
            reiniciarDisplay = false;
            return;
        }

        if (".".equals(digito)) {
            if (display.getText().contains(".")) {
                return;
            }
            display.setText(display.getText() + ".");
            return;
        }

        if ("0".equals(display.getText())) {
            display.setText(digito);
        } else {
            display.setText(display.getText() + digito);
        }
    }

    private void prepararOperacion(String operacion) {
        acumulador = Double.parseDouble(display.getText());
        operacionPendiente = operacion;
        reiniciarDisplay = true;
    }

    private void calcularResultado() {
        if (operacionPendiente.isEmpty()) {
            return;
        }

        double actual = Double.parseDouble(display.getText());
        double resultado;

        switch (operacionPendiente) {
            case "+":
                resultado = acumulador + actual;
                break;
            case "-":
                resultado = acumulador - actual;
                break;
            case "*":
                resultado = acumulador * actual;
                break;
            case "/":
                if (actual == 0) {
                    display.setText("Error");
                    operacionPendiente = "";
                    reiniciarDisplay = true;
                    return;
                }
                resultado = acumulador / actual;
                break;
            default:
                return;
        }

        display.setText(formatearResultado(resultado));
        operacionPendiente = "";
        reiniciarDisplay = true;
    }

    private void limpiar() {
        display.setText("0");
        acumulador = 0;
        operacionPendiente = "";
        reiniciarDisplay = true;
    }

    private boolean esOperacion(String texto) {
        return "+-*/".contains(texto) && texto.length() == 1;
    }

    private String formatearResultado(double valor) {
        if (valor == (long) valor) {
            return String.format("%d", (long) valor);
        }
        return String.format("%s", valor);
    }
}
