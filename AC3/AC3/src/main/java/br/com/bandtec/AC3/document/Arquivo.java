package br.com.bandtec.AC3.document;

import br.com.bandtec.AC3.iterator.ListaObj;
import br.com.bandtec.AC3.model.Deck;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Arquivo {

    public static void gravaRegistro (String nomeArq, String registro) {
        BufferedWriter saida = null;
        try {
            // o argumento true é para indicar que o arquivo não será sobrescrito e sim
            // gravado com append (no final do arquivo)
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        try {
            saida.append(registro + "\n");
            saida.close();

        } catch (IOException e) {
            System.err.printf("Erro ao gravar arquivo: %s.\n", e.getMessage());
        }
    }

    public void gravaLista(ListaObj<Deck> listaDeck, String nomeArquivo) {
        nomeArquivo += ".txt";

        String header = "";
        String corpo = "";
        String trailer = "";
        int contRegDados = 0;

        Date dataDeHoje = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        header += "00CARTA20210001" + formatter.format(dataDeHoje) + "01";

        gravaRegistro(nomeArquivo, header);

        for (int i = 0; i < listaDeck.getTamanho(); i++) {
            Deck deck = listaDeck.getElemento(i);

            corpo += "02";
            corpo += String.format("%-20s", deck.getNomeDeck());
            corpo += String.format("%03d", deck.getQtdCartas());
            corpo += String.format("%07.2f", deck.getValorDeck());

            contRegDados++;
            gravaRegistro(nomeArquivo,corpo);
            corpo = "";
        }

        trailer += "01";
        trailer += String.format("%05d", contRegDados);
        gravaRegistro(nomeArquivo,trailer);
    }

    public void leArquivo(String nomeArq) {
        BufferedReader entrada = null;
        String registro;
        String tipoRegistro;
        String nomeDeck;
        double valorDeck;
        int qtdCartas, contRegistro=0;

        // Abre o arquivo
        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }

        // Lê os registros do arquivo
        try {
            // Lê um registro
            registro = entrada.readLine();

            while (registro != null) {
                // Obtém o tipo do registro
                tipoRegistro = registro.substring(0, 2); // obtém os 2 primeiros caracteres do registro
                //    012345
                //    00NOTA
                if (tipoRegistro.equals("00")) {
                    System.out.println("Header");
                    System.out.println("Tipo de arquivo: " + registro.substring(2, 5));
                    int periodoLetivo= Integer.parseInt(registro.substring(5,13));
                    System.out.println("Período letivo: " + periodoLetivo);
                    System.out.println("Data/hora de geração do arquivo: " + registro.substring(13,21));
                    System.out.println("Versão do layout: " + registro.substring(22,24));
                }
                else if (tipoRegistro.equals("01")) {
                    System.out.println("\nTrailer");
                    int qtdRegistro = Integer.parseInt(registro.substring(2,5));
                    if (qtdRegistro == contRegistro) {
                        System.out.println("Quantidade de registros gravados compatível com quantidade lida");
                    }
                    else {
                        System.out.println("Quantidade de registros gravados não confere com quantidade lida");
                    }
                }
                else if (tipoRegistro.equals("02")) {
                    if (contRegistro == 0) {
                        System.out.println();
                        System.out.printf("%-20s %13s %13s\n",
                                "NOME DECK",
                                "QTD DE CARTAS",
                                "VALOR DO DECK");

                    }

                    nomeDeck = registro.substring(2,20);
                    qtdCartas = Integer.parseInt(registro.substring(20,23));
                    valorDeck = Double.parseDouble(registro.substring(23,30).replace(',','.'));

                    System.out.printf("%-20s %13s %13s\n", nomeDeck, qtdCartas, valorDeck);
                    contRegistro++;
                }
                else {
                    System.out.println("Tipo de registro inválido");
                }

                // lê o próximo registro
                registro = entrada.readLine();
            }

            // Fecha o arquivo
            entrada.close();
        } catch (IOException e) {
            System.err.printf("Erro ao ler arquivo: %s.\n", e.getMessage());
        }

    }
}
